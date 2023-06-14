#include<sys/socket.h>
#include<unistd.h>
#include<netdb.h>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>
#include<err.h>
#include<getopt.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define MAX_BUF 300

int Getaddrinfo(const char *hostname, const char *service, const struct addrinfo *hints, struct addrinfo **result){
    int n;
    n = getaddrinfo(hostname, service, hints, result);
    if(n) errx(1, "%s", gai_strerror(n));

    return(n);
}

int Socket(int family, int type, int protocol){
    int n;
    if((n = socket(family, type, protocol)) == -1){
        err(1, "Fatal error while creating socket.");
    }

    return(n);
}

int Bind(int sockfd, const struct sockaddr *myaddr, int addrlen){
    int n;
    if((n = bind(sockfd, myaddr, addrlen)) == -1){
        err(1, "Fatal error while binding.");
    }

    return (n);
}

int main(int argc, char *argv[]){

    int ch;
    char *port = "1234";

    while((ch = getopt(argc, argv, "p:s:")) != -1){
        switch (ch){
            case 'p':
                port = optarg;
                break;
            default:
                break;
        }
    }

    int sfd;
    struct sockaddr claddr;
    struct addrinfo hints, *res;

    char buf[MAX_BUF];
    socklen_t cllen;
    int msglen;
    int error;

    memset(&hints, 0, sizeof(hints));

    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM;
    hints.ai_flags = AI_PASSIVE;

    int n;
     n = getaddrinfo(NULL, port, &hints, &res);
    if(n) errx(1, "%s", gai_strerror(n));


    sfd = Socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    Bind(sfd, res->ai_addr, res->ai_addrlen);
 
    cllen = sizeof(claddr);

      while(1){
        msglen = recvfrom(sfd, buf, MAX_BUF, 0, &claddr, &cllen);

        if(msglen == -1){
            errx(1, "%s", "Error while recieving data.");
        }
        char *id = malloc(2);
        char poruka[257];
        strncpy(id, buf + 2, 2);
        strncpy(poruka, buf + 4, msglen -4);
        

        struct addrinfo hintsUpit, *resUpit, *pamti;
        memset(&hintsUpit, 0, sizeof(hintsUpit));

        hintsUpit.ai_family = AF_INET;
        hintsUpit.ai_socktype = SOCK_DGRAM;

        error = getaddrinfo(poruka , NULL, &hintsUpit, &resUpit);
    
        if(error){
            char bufs[2];
            bufs[0] = 1;
            bufs[1] = 0;

            error = sendto(sfd, bufs, 2, 0, (const struct sockaddr *)&claddr, sizeof(claddr));    
            if(error == -1){
                errx(1, "%s", "Error while sending data.");
            }
        }else{
            char addrstr4[INET_ADDRSTRLEN];
            char buf[4 + 4*20];

            buf[0] = 0;

            //Nakon MI: zaboravljeni printf()
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            struct sockaddr_in* ipv4 = (struct sockaddr_in *)&claddr;
            inet_ntop(AF_INET, &(ipv4->sin_addr), addrstr4, INET_ADDRSTRLEN);
            printf("Zahtjev: %s primljen s IP adrese %s port %d\n", poruka, addrstr4, ntohs(((const struct sockaddr_in *)&claddr)->sin_port));
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

            pamti = resUpit;
            uint8_t brojAdresa = 0;
            while(resUpit){
                inet_ntop(
                resUpit->ai_family,
                &((struct sockaddr_in *) resUpit->ai_addr)->sin_addr,
                addrstr4,
                INET_ADDRSTRLEN
                );

                inet_pton(AF_INET, addrstr4, buf + 4 + brojAdresa * 32);
                    
                resUpit = resUpit->ai_next;
                brojAdresa = brojAdresa + 1;
            }

            buf[1] = brojAdresa;

            memcpy(buf +2, id, 2);

            error = sendto(sfd, buf, 4 + brojAdresa * 32, 0, (const struct sockaddr *)&claddr, sizeof(claddr));    
            if(error == -1){
                errx(1, "%s", "Error while sending data.");
            }
        }

        
    }
}