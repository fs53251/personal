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

#define MYPORT 1234
#define BACKLOG 10
#define MAXBUFF 1024

int main(int argc, char *argv[]){
    int ch;
    short port = MYPORT;

    while((ch = getopt(argc, argv, "p:")) != -1){
        switch (ch){
            case 'p':
                port = atoi(optarg);
                break;
            default:
                errx(1, "./tcpserver [-p port]");
                break;
        }
    }

    int sockfd, newfd;
    struct sockaddr_in my_addr;
    struct sockaddr_in their_addr;
    socklen_t sin_size;
    int error;

    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		err(1,"socket");
    }

    my_addr.sin_family = AF_INET;
    my_addr.sin_port = htons(port);
    my_addr.sin_addr.s_addr = INADDR_ANY;
    memset(my_addr.sin_zero, '\0', sizeof my_addr.sin_zero);

    if (bind(sockfd, (struct sockaddr *)&my_addr, sizeof my_addr) == -1) {
		err(1,"bind");
    }

    if (listen(sockfd, BACKLOG) == -1) {
		err(1,"listen");
    }

    sin_size = sizeof their_addr;
    while(1){
        if ((newfd=accept(sockfd,(struct sockaddr *)&their_addr,&sin_size)) == -1) {
            err(1,"accept");
        }
        int r = 0;
        uint32_t offset;
        //procitaj 4 byta u strukturu offset, ali kao "network byte order"
        r = recv(newfd, &offset, 4, 0);
        if(r == -1){
            errx(1, "Failure");
        }
        // "network byte order -> host long byte order, 32 bit unsigned"
        offset = ntohl(offset);
        
        // recieve file name
        char buf[MAXBUFF];
        // primaj podatke dok ne dode '\0'
        
        int recievedBytes = 0;
        
        while (1){
            r = recv(newfd, buf + recievedBytes, 1, 0);
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

        bool notSupported = false;
        for (int i = 0; i < recievedBytes; i++)
        {
            if (buf[i] == '/')
            {
                notSupported = true;
                break;
            }
        }

        char *str = malloc(recievedBytes);
        strcpy(str, buf);
        
        unsigned char s = 0x00;
        
        FILE *f = fopen(str, "rb");
        if (f == NULL || notSupported){

            // ne postoji datoteka
            s = 0x01;
            char *poruka = "Ne postoji datoteka ili joj ne smijem pristupiti.";
            char sendBuff[strlen(poruka) + 2];
            sendBuff[0] = s;
            memcpy(sendBuff + 1, poruka, strlen(poruka));
            sendBuff[strlen(poruka) + 1] = '\0';
            r = send(newfd, sendBuff, sizeof(sendBuff), 0);
            if(r == -1){
                errx(1, "Failure");
            }
            close(newfd);
        }
        else if (access(buf, R_OK) != 0)
        {

            // nema prava citanja datoteke
            s = 0x02;
            fclose(f);

            char *poruka = "Nemam prava citati tu datoteku.";
            char sendBuff[strlen(poruka) + 2];
            sendBuff[0] = s;
            memcpy(sendBuff + 1, poruka, strlen(poruka));
            sendBuff[strlen(poruka) + 1] = '\0';
            r = send(newfd, sendBuff, sizeof(sendBuff), 0);
            if(r == -1){
                errx(1, "Failure");
            }
            close(newfd);
        }
        else
        {
            fseek(f, offset, SEEK_SET);
            char recvBuf[MAXBUFF];
            send(newfd, &s, 1, 0);
            while(( r = fread(recvBuf, sizeof(char), sizeof(recvBuf), f)) > 0){
                if(r == -1){
                    errx(1, "Failure");
                }
                for(int i = 0; i < r; i++){
                    send(newfd, &recvBuf[i], sizeof(char), 0);
                }
            }
           
            fclose(f);
        }

        free(str);
        close(newfd);
    }
    close(sockfd);

    return 0;
}
