#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <err.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>
#include <string.h>
#include <getopt.h>
#include <stdbool.h>
#include <sys/select.h>

#define PORT "1234"
#define POPIS ""
#define RECV_MESSAGE_LEN 6
#define BACKLOG 10
#define FD_SET_LENGTH 3
#define POPIS_MAX_LEN 1024


/*+++++++++++++++++++++ Osnovne funkcije ++++++++++++++++++++++*/

int Accept(int sockfd, struct sockaddr *cliaddr, socklen_t *addrlen){
    int n;

    n = accept(sockfd, cliaddr, addrlen);

    if(n == -1){
        close(sockfd);
        errx(1, "Fatal errror while accepting");
    }

    return(n);
}

void PrintPayloads(char *popis){
    char *token = strtok(popis, ":");
    while(token != NULL){
        printf("%s\n", token);
        token = strtok(NULL, ":");
    }
}

int Select(int maxfd, fd_set *readset, fd_set *writeset, fd_set *excepset, struct timeval *timeout){
    int n;
    n = select(maxfd, readset, writeset, excepset, timeout);
    if(n < 0){
        errx(1, "Fatal error while blocked on select.");
    }

    return (n);
}

int Listen(int sockfd, int backlog){
    int n;

    n = listen(sockfd, backlog);

    if(n == -1){
        close(sockfd);
        errx(1, "Fatal error while listening");
    }

    return(n);
}

int Bind(int sockfd, const struct sockaddr *myaddr, int addrlen){
    int n;
    if((n = bind(sockfd, myaddr, addrlen)) == -1){
        close(sockfd);
        err(1, "Fatal error while binding.");
    }

    return (n);
}

int Socket(int family, int type, int protocol){
    int n;
    if((n = socket(family, type, protocol)) == -1){
        err(1, "Fatal error while creating socket.");
    }

    return(n);
}

int Getaddrinfo(const char *hostname, const char *service, const struct addrinfo *hints, struct addrinfo **result){
    int n;
    n = getaddrinfo(hostname, service, hints, result);
    if(n) errx(1, "%s", gai_strerror(n));

    return(n);
}

int Setsockopt(int sockfd, int level, int optname, void *opval, socklen_t optlen){
    int n = setsockopt(sockfd, level, optname, opval, optlen);
    if(n){
        close(sockfd);
        errx(1, "Fatal error while seting socket options.");
    }

    return (n);
}
 /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


 /*++++++++++++++++++++++++++ MAIN ++++++++++++++++++++++++++++++*/

int main(int argc, char *argv[]){
    int ch;
    char *portTCP = PORT;
    char *portUDP = PORT;
    char *popis;
    char *noviPopis = (char *)malloc(POPIS_MAX_LEN * sizeof(char));
    bool imamoNoviPopis = false;

    while((ch = getopt(argc, argv, "t:u:p:")) != -1){
        switch (ch){
        case 't':
            portTCP = optarg;
            break;
        case 'u':
            portUDP = optarg;
            break;
        case 'p':
            popis = optarg;
            break;
        default:
            errx(1, "./server [-t tcp_port] [-u udp_port] [-p popis]");
            break;
        }
    }

    char buf[RECV_MESSAGE_LEN];

    /*+++++++++++++++++++++ UDP service ++++++++++++++++++++++++++*/
    int socketUDP;
    struct addrinfo hintsUDP, *resUDP;

    memset(&hintsUDP, 0, sizeof hintsUDP);

    hintsUDP.ai_family = AF_INET;
    hintsUDP.ai_socktype = SOCK_DGRAM;
    hintsUDP.ai_flags = AI_PASSIVE;

    Getaddrinfo(NULL, portUDP, &hintsUDP, &resUDP);

    socketUDP = Socket(resUDP->ai_family, resUDP->ai_socktype, resUDP->ai_protocol);

    Bind(socketUDP, resUDP->ai_addr, resUDP->ai_addrlen);

    printf("UDP server listening on port: %d\n", ntohs(((struct sockaddr_in *)(resUDP->ai_addr))->sin_port));

    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


    /*+++++++++++++++++++++ TCP service +++++++++++++++++++++++++*/
    int socketTCP;
    struct addrinfo hintsTCP, *resTCP;

    memset(&hintsTCP, 0, sizeof hintsTCP);

    hintsTCP.ai_family = AF_INET;
    hintsTCP.ai_socktype = SOCK_STREAM;
    hintsTCP.ai_flags = AI_PASSIVE;

    Getaddrinfo(NULL, portTCP, &hintsTCP, &resTCP);

    socketTCP = Socket(resTCP->ai_family, resTCP->ai_socktype, resTCP->ai_protocol);

    Setsockopt(socketTCP, SOL_SOCKET, SO_REUSEADDR, &(int){1}, sizeof(int));

    Bind(socketTCP, resTCP->ai_addr, resTCP->ai_addrlen);
    Listen(socketTCP, 100);

    printf("TCP server listening on port: %d\n", ntohs(((struct sockaddr_in *)(resTCP->ai_addr))->sin_port));

    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


    /*+++++++++++++++++++++ SELECT ++++++++++++++++++++++++++++++*/
    fd_set readfds;
    int max_socketfd;


    //isprazni skup
    FD_ZERO(&readfds);

    //dodaj Standardni ulaz (STDIN=0)
    FD_SET(STDIN_FILENO, &readfds);

    //dodaj TCP socket
    FD_SET(socketTCP, &readfds);
    max_socketfd = socketTCP;

    //dodaj UDP socket
    FD_SET(socketUDP, &readfds);
    if(socketUDP > max_socketfd){
        max_socketfd = socketUDP;
    }

    while(1){
        fd_set tmp = readfds;
        Select(max_socketfd + 1, &tmp, NULL, NULL, NULL);

        /*+++++++++++++++++++++ Obradi select() +++++++++++++++++++++++++++*/
        if(FD_ISSET(STDIN_FILENO, &tmp)){ //standardni ulaz: PRINT, SET, QUIT
            char komanda[10];

            //dodaje '\n' u polje komanda na kraj
            scanf("%s", komanda);

            if(strcmp(komanda, "PRINT") == 0){ //popis na stdout
                if(imamoNoviPopis){
                    char tmp_popis[strlen(noviPopis)];
                    memcpy(tmp_popis, noviPopis, strlen(noviPopis));
                    PrintPayloads(tmp_popis);
                }else{
                    char tmp_popis[strlen(popis)];
                    memcpy(tmp_popis, popis, strlen(popis));
                    PrintPayloads(tmp_popis);
                }


            }else if(strcmp(komanda, "SET") == 0){ //popis = novi_popis sa stdin
                scanf(" %s", noviPopis);
                imamoNoviPopis = true;
            }else if(strcmp(komanda, "QUIT") == 0){
                return 0;
            }

        }else if(FD_ISSET(socketTCP, &tmp)){ //TCP servis

            //klijent
            struct sockaddr clientSockaddr;
            socklen_t clientSocksize = sizeof clientSockaddr;
            int botSocket;

            botSocket = accept(socketTCP, &clientSockaddr, &clientSocksize);


            //primi poruku 'HELLO\n'
            // primaj podatke dok ne dode '\0'
            int recievedBytes = 0;
            int r;

            while (1){
                r = recv(botSocket, buf + recievedBytes, 1, 0);
                if(r == -1){
                    errx(1, "Failure");
                }
                if (r > 0){
                    recievedBytes = recievedBytes + r;
                }

                if(buf[recievedBytes - 1]== '\0'){
                    break;
                }
            }

            char *str = malloc(recievedBytes);
            strcpy(str, buf);
            printf("Recieved message: %s\n", str);

            if(strcmp(str, "HELLO") == 0){
                if(imamoNoviPopis){
                    char *modifiedPopis = malloc(strlen(noviPopis) + 2);
                    strcpy(modifiedPopis, popis);
                    strcat(modifiedPopis, "\n");
                    printf("%s\n", modifiedPopis);
                    r = send(botSocket, modifiedPopis, strlen(modifiedPopis), 0);
                    printf("Poslao poruku: %s\n", noviPopis);
                    if(r == -1){
                        errx(1, "Failure while sending TCP message");
                    }
                    free(modifiedPopis);
                }else{
                    //add \n and for \0 space
                    char *modifiedPopis = malloc(strlen(popis) + 2);
                    strcpy(modifiedPopis, popis);
                    strcat(modifiedPopis, "\n");
                    printf("%s\n", modifiedPopis);
                    int r = send(botSocket, modifiedPopis, strlen(modifiedPopis), 0);
                    printf("Poslao payload: %s, poslano %d bajtova\n", popis, r);
                    if(r == -1){
                        errx(1, "Failure while sending TCP message");
                    }
                    free(modifiedPopis);
                }
            
            }else{
                printf("Unsupported message recieved.");
            }

        }else if(FD_ISSET(socketUDP, &tmp)){
            //klijent
            struct sockaddr botAddr;
            socklen_t botLen;
            botLen = sizeof(botAddr);
            char recvBuf[6];

            int msglen = recvfrom(socketUDP, recvBuf, RECV_MESSAGE_LEN, 0, &botAddr, &botLen);

            if(msglen == -1){
                errx(1, "%s", "Error while recieving data.");
            }

            if(strncmp(recvBuf, "HELLO", 5) == 0){
                int r;
                if(imamoNoviPopis){
                    char *modifiedPopis = malloc(strlen(noviPopis) + 2);
                    strcpy(modifiedPopis, popis);
                    strcat(modifiedPopis, "\n");
                    printf("%s\n", modifiedPopis);
                    r = sendto(socketUDP, modifiedPopis, strlen(modifiedPopis), 0, &botAddr, botLen);
                    printf("Poslao poruku: %s\n", noviPopis);
                    if(r == -1){
                        errx(1, "Failure while sending TCP message");
                    }
                    free(modifiedPopis);
                }else{
                    //add \n and for \0 space
                    char *modifiedPopis = malloc(strlen(popis) + 2);
                    strcpy(modifiedPopis, popis);
                    strcat(modifiedPopis, "\n");
                    printf("%s\n", modifiedPopis);
                    r = sendto(socketUDP, modifiedPopis, strlen(modifiedPopis), 0, &botAddr, botLen);
                    printf("Poslao payload: %s, poslano %d bajtova\n", popis, r);
                    if(r == -1){
                        errx(1, "Failure while sending TCP message");
                    }
                    free(modifiedPopis);
                }
            }
        }
        /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    }

    free(noviPopis);
    close(socketTCP);
    close(socketUDP);

    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
}
/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
