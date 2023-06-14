#include <stdio.h>
#include<unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <err.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include<stdbool.h>
//prog [-r] [-t|-u] [-x] [-h|-n] [-46] {hostname|IP_address} {service|port}

//default -t
//-4 default
//default -h

//ispis: 161.53.72.120 (www2.fer.hr) 25
// IPv4 - CanonName - port

int main(int argc, char *argv[]){
	
    if(argc < 3){
    	printf("prog [-r] [-t|-u] [-x] [-h|-n] [-46] {hostname|IP_address} {servicename|port}\n");
    	return 1;
    }
    int ch;
    
    bool printHex = false;
    bool networkByteOrder = false;
    bool reverse = false;
    bool udp = false;
    
    char *host;
    char *service;
    struct addrinfo hints, *res, *pamti;
    int error;
    
    char addrstr4[INET_ADDRSTRLEN];
    char addrstr6[INET6_ADDRSTRLEN];
    
    memset(&hints, 0, sizeof(hints));
    hints.ai_flags |= AI_CANONNAME;
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;
    

    while((ch = getopt(argc, argv, "rtuxhn46")) != -1){
        switch (ch){
            case 'r':
            	reverse = true;
                break;
            case 'u':
            	hints.ai_socktype = SOCK_DGRAM;
    		hints.ai_protocol = IPPROTO_UDP;
    		udp = true;
                break;
            case 'x':
            	printHex = true;
                break;
            case 'n':
            	networkByteOrder = true;
                break;
            case '6':
            	hints.ai_family = AF_INET6;
                break;
            default:
                break;
        }
    }
    if((argc - optind) > 2){
    	return 2;
    }
    host = argv[optind];
    service = argv[optind + 1];
    
    if(reverse){
    	if(udp){
    		hints.ai_socktype = SOCK_DGRAM;
    		hints.ai_protocol = IPPROTO_UDP;
    	}
    	struct addrinfo *res;
    	if(getaddrinfo(host, service, &hints, &res) != 0){
    		printf("a.out: %s is not a valid IP address\n", host);
    		return 2;
    	}
    	char hostReturn[NI_MAXHOST];
    	char serviceReturn[NI_MAXSERV];
    	if(getnameinfo(res->ai_addr, res->ai_addrlen, hostReturn, sizeof(hostReturn), serviceReturn, sizeof(serviceReturn), NI_NAMEREQD) != 0){
    		printf("a.out: %s is not a valid IP address\n", host);
    		return 2;
    	}
    	
    	printf("%s (%s) %s\n", host, hostReturn, serviceReturn);
    }else{
    error = getaddrinfo(host, service, &hints, &res);
    
    if(error){
    	printf("prog: nodename nor servname provided, or not known\n");
    	return 2;
    }
    
    pamti = res;
    while(res){
    	if(hints.ai_family == AF_INET){
    		inet_ntop(
    		res->ai_family,
    		&((struct sockaddr_in *) res->ai_addr)->sin_addr,
    		addrstr4,
    		INET_ADDRSTRLEN
    		);
    		int nport  = ((struct sockaddr_in *)res->ai_addr)->sin_port;
    		int port = networkByteOrder ? nport : ntohs(nport);
    		if(printHex){
    			printf("%s (%s) %04x\n", addrstr4, res->ai_canonname, port);
    		}else{
    			printf("%s (%s) %d\n", addrstr4, res->ai_canonname, port);
    		}
    		
    	} else if(hints.ai_family == AF_INET6){
    		inet_ntop(
    		res->ai_family,
    		&((struct sockaddr_in6 *) res->ai_addr)->sin6_addr,
    		addrstr6,
    		INET6_ADDRSTRLEN
    		);
    		int nport = ((struct sockaddr_in6 *)res->ai_addr)->sin6_port;
    		int port = networkByteOrder ? nport : ntohs(nport);
    		if(printHex){
    			printf("%s (%s) %04x\n", addrstr6, res->ai_canonname, port);
    		}else{
    			printf("%s (%s) %d\n", addrstr6, res->ai_canonname, port);
    		}
    		
    	}
    	
    	
    	res = res->ai_next;
    }	
    freeaddrinfo(pamti);
    }
   
    return 0;
}
