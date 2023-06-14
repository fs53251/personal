#include<sys/types.h>
#include<sys/socket.h>
#include<err.h>
#include<errno.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<netdb.h>

//kreiranje socketa
int Socket(int family, int type, int protocol){
    int n;
    if((n = socket(family, type, protocol)) == -1){
        err(1, "Fatal error while creating socket.");
    }

    return(n);
}

//bind - pridruzi socketu adresu
int Bind(int sockfd, const struct sockaddr *myaddr, int addrlen){
    int n;
    if((n = bind(sockfd, myaddr, addrlen)) == -1){
        err(1, "Fatal error while binding.");
    }

    return (n);
}

//na koju adresu i port je vezan socket
int Getsockname(int socket, struct sockaddr *address, socklen_t *address_len){
    int n;
    if((n = getsockname(socket, address, address_len)) == -1){
        err(1, "Fatal error while getting info about address and port from socket");
    }

    return(n);
}

//zatvaranje socketa
int Shutdown(int sockfd, int how){
    int n;
    if((n = shutdown(sockfd, how)) == -1){
        err(1, "Fatal erro while doing shutdown.");
    }

    return(n);
}

//adresa iz in_addr -> string
const char * Inet_ntop(int af, const void *src, char *dst, socklen_t size){
    return(inet_ntop(af, src, dst, size));
}

char *Inet_ntop(const struct sockaddr *sa, socklen_t salen)
{
    static char str[INET_ADDRSTRLEN];	/* Unix domain is largest */

    struct sockaddr_in *sin = (struct sockaddr_in *)sa;

    if (inet_ntop(AF_INET, &sin->sin_addr, str, sizeof(str)) == NULL) {
	warn("inet_ntop error");	/* inet_ntop postavlja errno */
	return (NULL);
    }
    return (str);
}

//adresa "161.53.19.186" string -> in_addr
int Inet_pron(int af, const char *src, void *dst){
    return(inet_pton(af, src, dst));
}

//getaddrinfo(naziv ili IP dotted adresa, naziv ili broj servisa(string), ...)
//freeaddrinfo(result)
int Getaddrinfo(const char *hostname, const char *service, const struct addrinfo *hints, struct addrinfo **result){
    int n;
    n = getaddrinfo(hostname, service, hints, result);
    if(n) errx(1, "%s", gai_strerror(n));

    return(n);
}

//getnameinfo, sockaddr -> *host, *serv
int Getnameinfo(const struct sockaddr *sockaddr, socklen_t addrlen, char *host, size_t hostlen, char *serv, size_t servlen, int flags){
    int n;
    n = getnameinfo(sockaddr, addrlen, host, hostlen, serv, servlen, flags);
    if(n){
        errx(1, "getnameinfo: %s", gai_strerror(n));
    }

    return(n);
}

//UDP 

//send UDP
ssize_t Sendto(int sockfd, void *buff, size_t nbytes, int flags, const struct sockaddr* to, socklen_t addrlen){
    int n;
    n = sendto(sockfd, buff, nbytes, flags, to, addrlen);

    if(n == -1){
        errx(1, "Fatal error while sending data");
    }

    return(n);
}

//recieve UDP
ssize_t Recvfrom(int sockfd, void *buff, size_t nbytes, int flags, struct sockaddr *from, socklen_t *fromaddrlen){
    int n;
    n = recvfrom(sockfd, buff, nbytes, flags, from, fromaddrlen);

    if(n == -1){
        errx(1, "Fatal error while recieving data");
    }

    return(n);
}

//TCP listen, 
int Listen(int sockfd, int backlog){
    int n;

    n = listen(sockfd, backlog);

    if(n == -1){
        errx(1, "Fatal error while listening");
    }

    return(n);
}

//TCP accept, vraca sockfd
int Accept(int sockfd, struct sockaddr *cliaddr, socklen_t *addrlen){
    int n;

    n = accept(sockfd, cliaddr, addrlen);

    if(n == -1){
        errx(1, "Fatal errror while accepting");
    }

    return(n);
}

//TCP connect
int Connect(int sockfd, const struct sockaddr *server, socklen_t addrlen){
    int n;

    n = connect(sockfd, server, addrlen);

    if(n == -1){
        errx(1, "Fatal error while connecting");
    }
}

//dohvaćanje adrese spojenog klijenta, iz sockfd dobivenog od accept
int Getpeername(int socket, struct sockaddr *restrict address, socklen_t *restrict address_len){
    int n;

    n = getpeername(socket, address, address_len);

    if(n == -1){
        errx(1, "Fatal error while getting peer name");
    }

    return(n);
}

