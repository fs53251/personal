#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <err.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <getopt.h>
#include <stdbool.h>
#include <arpa/inet.h>
#include <sys/stat.h>

#define PORT "1234"
#define HOST "127.0.0.1"
#define MAX_BUF 1024

int main(int argc, char *argv[]){

    int ch;
    char *host = HOST;
    char *port = PORT;
    bool c = false;
    char *filename;

    while((ch = getopt(argc, argv, "s:p:c")) != -1){
        switch (ch){
            case 's':
                host = optarg;
                break;
            case 'p':
                port = optarg;
                break;
            case 'c':
                c = true;
                break;
            default:
                errx(1, "./tcpklijent [-s server] [-p port] [-c] filename");
                break;
        }
    }

    filename = argv[optind];
    if(filename == NULL){
        errx(1, "./tcpklijent [-s server] [-p port] [-c] filename");
    }

    int sockfd;
    struct sockaddr_in their_addr;
    int error;
    struct addrinfo hints, *res;

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;

    if ((error=getaddrinfo(host, port, &hints, &res)) != 0) {
        errx(1, "%s", gai_strerror(error));
    }

    if ((sockfd = socket(res->ai_family, res-> ai_socktype, res->ai_protocol)) == -1) {
        err(1,"socket");
    }

    if (connect(sockfd, res->ai_addr, res->ai_addrlen) == -1) {
        close(sockfd);
	    err(1,"connect");
    }


    //posalji broj(4 okteta) + filename + (\0)
    uint32_t offset;

    if(!c){
        if(access(filename, F_OK) != -1){
            errx(1, "Program pozvan bez -c, a trazena datoteka postoji u direktoriju.");
        }else{
            offset = 0;
            offset = htonl(offset);
            //4 za pocetni offset, strlen string i 1 za null terminator
            char sendBuff[strlen(filename) + 4 + 1];
            memcpy(sendBuff, &offset, 4);
            memcpy(sendBuff + 4, filename, strlen(filename));
            sendBuff[strlen(filename) + 4] = '\0';
            send(sockfd, sendBuff , sizeof(sendBuff), 0);
        }
    }else{
        if(access(filename, F_OK) != -1){
            if(access(filename, W_OK) == -1){
                errx(1, "Program nema dozvolu za pisanje u datoteku.");
            }else{
                struct stat st;
                stat(filename, &st);
                offset = (uint32_t)st.st_size;
                offset = htonl(offset);
                char sendBuff[strlen(filename) + 4 + 1];
                memcpy(sendBuff, &offset, 4);
                memcpy(sendBuff + 4, filename, strlen(filename) + 1);
                sendBuff[strlen(filename) + 4] = '\0';
                send(sockfd, sendBuff , sizeof(sendBuff), 0);
            }
        }else{
            offset = 0;
            offset = htonl(offset);
            char sendBuff[strlen(filename) + 4 + 1];
            memcpy(sendBuff, &offset, 4);
            memcpy(sendBuff + 4, filename, strlen(filename) + 1);
            sendBuff[strlen(filename) + 4] = '\0';
            send(sockfd, sendBuff , sizeof(sendBuff), 0);
        }
    }

     //primaj podatke dok ne dode '\0'
    int r = 0;
    int recievedBytes = 0;
    char buf[1];

    r = recv(sockfd, buf, 1, 0);
    if(r == -1){
        errx(1, "Failure");
    }
    unsigned char firstByte = (unsigned char)buf[0];
    if(r == 1){
        firstByte = buf[0];
    }
    printf("%d/n", firstByte);
    if(firstByte == 0x00){
         //zapisi buf u datoteku filename
        //koristi a kao append mode, ako ne postoji file stvori ga, 
        // a ako postoji, nadodaj bajtove
        FILE *f = fopen(filename, "a");
        if(f == NULL){
            errx(1, "Neuspješno otvaranje file-a");
        }

        char buf[MAX_BUF];
        while((r = recv(sockfd, buf, sizeof(buf), 0)) > 0){
            if(r == -1){
                errx(1, "Failure");
            }
            for(int i = 0; i < r; i++){
                fwrite(&buf[i], sizeof(char), 1, f);
            }
        }
        errx(firstByte, "Uspjesno primio datoteku.");
        fclose(f);

    }else if(firstByte == 0x01){
        char poljePoruka[1024];
         while(1){
            r = recv(sockfd, poljePoruka + recievedBytes, 1, 0);
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
        strcpy(str, poljePoruka);

        errx(firstByte, "%s", str);
    }else if(firstByte == 0x02){
        char poljePoruka[1024];
         while(1){
            r = recv(sockfd, poljePoruka + recievedBytes, 1, 0);
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
        strcpy(str, poljePoruka);
        errx(firstByte, "%s", str);
    }else if(firstByte == 0x03){
        char poljePoruka[1024];
         while(1){
            r = recv(sockfd, poljePoruka + recievedBytes, 1, 0);
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
        strcpy(str, poljePoruka);
         errx(firstByte, "%s", str);
    }else{
        errx(1, "Wrong data recieved.");
    }


   
    close(sockfd);

    return 0;
}
