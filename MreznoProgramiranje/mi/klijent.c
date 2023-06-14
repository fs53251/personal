#include<sys/socket.h>
#include<unistd.h>
#include<netdb.h>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>
#include<err.h>
#include<getopt.h>
#include <arpa/inet.h>

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
    char *host = NULL;
    char *port = "1234";

    while((ch = getopt(argc, argv, "p:s:")) != -1){
        switch (ch){
            case 'p':
                port = optarg;
                break;
            case 's':
                host = optarg;
                break;
            default:
                break;
        }
    }

    int sockfd;
    struct sockaddr sock;
    struct addrinfo hints, *res;

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM;

    Getaddrinfo(host, port, &hints, &res);

    sockfd = Socket(res->ai_family, res-> ai_socktype, res->ai_protocol);


    char unos[256];
    char buf[2 + 2 + 257];
    char *id = "AB";
    int error;

    while(1){
        scanf("%s",unos);

        buf[0] = 0;
        buf[1] = 0;

        memcpy(buf + 2, id, 2);
        memcpy(buf + 4, unos, strlen(unos));
        buf[2 + 2 + strlen(unos)] = '\0';
        
        error = sendto(sockfd, buf, 2 + 2 + strlen(unos) + 1, 0, res->ai_addr, res->ai_addrlen);


        struct sockaddr claddr;
        char buf[1024];
        socklen_t cllen;
        cllen = sizeof(claddr);
        int msglen;
        msglen = recvfrom(sockfd, buf, 1024, 0, &claddr, &cllen);

        printf("Primio status: %d\n", buf[0]);
        char *idprimljeni = malloc(3);
        strncpy(idprimljeni, buf+2, 2);
        idprimljeni[2] = '\0';
        printf("primio broj adresa: %d\n", buf[1]);

        /*++++++++++++++++++++++++++++++++*/
        if(buf[1] == 0){
            printf("Ne mogu pronaci niti jednu adresu.\n");
        }
        if(strncmp(id, buf + 2, 2) != 0){
            printf("Krivi identifikator\n");
        }
        /*++++++++++++++++++++++++++++++++*/

        for(int i = 0; i < buf[1]; i++){
            int adresa;
            char a[INET_ADDRSTRLEN];
            //bugfix, zaboravio dodati 4 kao parametar
            memcpy(&adresa, buf +4 + i *32, 4);
            inet_ntop(AF_INET, &adresa, a, INET_ADDRSTRLEN);

            printf("%s\n", a);
        }



        printf("Primljeni id: %s\n", idprimljeni);
    }
}