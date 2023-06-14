#include <netdb.h>
#include <sys/socket.h>
#include <netdb.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <err.h>

#define PORT "1234"
#define PAYLOAD ""
#define RECV_MSG 7
#define MAX_SEND_MSG 512

int main(int argc, char *argv[]){
    int ch;

     if(argc > 5){
        printf("Usage: ./UDP_server [-l port] [-p payload]");
        return 1;
    }

    //argumenti programa
    char *payload = PAYLOAD;
    char *port = PORT;

    while((ch = getopt(argc, argv, "l:p:")) != -1){
        switch (ch){
            case 'l':
                port = optarg;
                break;
            case 'p':
                payload = optarg;
                break;
            default:
                break;
        }
    }
    
    int sfd;
    struct sockaddr claddr;
    struct addrinfo hints, *res;

    char buf[RECV_MSG];
    socklen_t cllen;
    int msglen;
    int error;

    memset(&hints, 0, sizeof(hints));

    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM;
    hints.ai_flags = AI_PASSIVE;
    
    error = getaddrinfo(NULL, port, &hints, &res);
    if(error){
        errx(1, "%s", gai_strerror(error));
    }

    sfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    if(sfd == -1){
        errx(1, "%s", "Socket is not created.");
    }
    
    error = bind(sfd, res->ai_addr, res->ai_addrlen);
    if(error == -1){
        errx(1, "%s", "Bind was not successfull.");
    }
 
    cllen = sizeof(claddr);

    while(1){
        msglen = recvfrom(sfd, buf, RECV_MSG, 0, &claddr, &cllen);

        if(msglen == -1){
            errx(1, "%s", "Error while recieving data.");
        }
        char *hello = malloc(msglen + 1);
        strncpy(hello, buf, msglen);
        hello[msglen] = '\0';
        char p[MAX_SEND_MSG] = "PAYLOAD:";

        if(strcmp(hello, "HELLO") == 0){
            strcat(p, payload);
            strcat(p, "\n");

            error = sendto(sfd, (const void *)p, 9 + strlen(payload), 0, (const struct sockaddr *)&claddr, sizeof(claddr));    
            if(error == -1){
                errx(1, "%s", "Error while sending data.");
            }
        }
        free(hello);
        memset(p, 0, sizeof(p));
    }

    

    memset(buf, 0, RECV_MSG);
    memset(&hints, 0, sizeof(hints));
    close(sfd);
    
    return 0;
}