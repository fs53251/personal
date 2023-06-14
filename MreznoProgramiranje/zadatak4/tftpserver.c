#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <unistd.h>
#include <fcntl.h>
#include <signal.h>
#include <syslog.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <netdb.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include <err.h>

#define TFTP_BUFFER 516

#define DIR "/dev/null"
#define CH_DIR "/"
#define DEFAULT_DIR "/tftpboot/"

bool d = false;
int output = 0;

struct tftp_rrq{
	short code;
	char filename[512];
	char mode[15];
};

struct tftp_data{
	short code;
	short count;
};


void print_error_and_exit(int where, const char *errmsg){
	if(where == 0){
		syslog(LOG_ERR, "%s", errmsg);
	}else{
		fprintf(stderr, "%s", errmsg);
	}
	
	exit(2);
}

int Socket(int family, int type, int protocol){
    int n;
    if((n = socket(family, type, protocol)) == -1){
        errx(1, "Fatal error while creating socket.");
    }

    return(n);
}

int Getaddrinfo(const char *hostname, const char *service, const struct addrinfo *hints, struct addrinfo **result){
    int n;
    n = getaddrinfo(hostname, service, hints, result);
    if(n) errx(1, "%s", gai_strerror(n));

    return(n);
}

int Bind(int sockfd, const struct sockaddr *myaddr, int addrlen){
    int n;
    if((n = bind(sockfd, myaddr, addrlen)) == -1){
        errx(1, "Fatal error while binding.");
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



char *rm(const char *str){
	int length = strlen(str);
	
	if(length >= 2 && str[length - 2] == '\r' && str[length - 1] == '\n'){
		length = length - 2;
	}else{
		while(length >= 1 && (str[length - 1] == '\r' || str[length - 1] == '\n')){
			length--;
		}
	}
	
	char *new = malloc((length + 1) * sizeof(char));
	strncpy(new, str, length);
	new[length] = '\0';
	
	return new;
}



void execute_child_proccess (char buf[], struct sockaddr_in *cliaddr, int clilen){
	int clientSocketfd;
	struct addrinfo hints, *res;
	struct tftp_rrq *rrq_msg, rrq_struct;

	int o = 1;
	if(d){
		o = 0;
	}
	
	
	memset(&hints, 0, sizeof hints);

	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	Getaddrinfo(NULL, "0", &hints, &res);

	clientSocketfd = Socket(AF_INET, SOCK_DGRAM, 0);

	Bind(clientSocketfd, res->ai_addr, res->ai_addrlen);

	rrq_msg = &rrq_struct;
	memcpy (&(rrq_msg->code), buf, 2);
	rrq_msg->code = ntohs (rrq_msg->code);
	
	int ccode = (int) rrq_msg->code;
	

	exit (0);
}

int main (int argc, char *argv[]){
	int ch;
	char *portUDP;
	
	int socketUDP;
	struct addrinfo hintsUDP, *resUDP;

	int print_to = 1;
	char buf[1500];


	if (argc > 3) {
		
		
	}
	
	while ((ch = getopt (argc, argv, "d")) != -1) {
		switch (ch) {
		case 'd':
			d = true;
			output = 0;
			break;
		default:
			if(output == 0){
				syslog(LOG_ERR, "Usage: %s [-d] port_name_or_number\n", "tftpserver");
			}else{
				fprintf(stderr, "Usage: %s [-d] port_name_or_number\n", "tftpserver");
			}
			break;
		}
	}

	portUDP = argv[optind];

	
	memset(&hintsUDP, 0, sizeof hintsUDP);

	hintsUDP.ai_family = AF_INET;
	hintsUDP.ai_socktype = SOCK_DGRAM;
	hintsUDP.ai_flags = AI_PASSIVE;

	Getaddrinfo(NULL, portUDP, &hintsUDP, &resUDP);
	

	if (d) {
		pid_t forkValue = fork();
		if(forkValue < 0){
			errx(1, "Fatal error while forking.");
		}
		if(forkValue > 0){
	    	    errx(0, "Turn off parent");
		}
	    
		if(setsid() < 0){
			errx(1, "Error");
		}
	    
	    	open(DIR, O_RDONLY);
		open(DIR, O_RDWR);
		open(DIR, O_RDWR);

		
		int read = 4;
		int write = 2;
		int execute = 1;
		
		int owner = 0;
		int group = write;
		int others = read + write + execute;
		
		char str[4];
		sprintf(str, "%d%d%d", owner, group, others);
		int mask = strtol(str, NULL, 8);
		umask(mask);
		
		
		chdir(CH_DIR);
		
		for(int i = 0; i < 64; i++){
			close(i);
		}
		
		signal(SIGHUP, SIG_IGN);
		signal(SIGCHLD, SIG_IGN);
		signal(SIGTSTP, SIG_IGN);
		
		openlog("tftpserver", LOG_PID, LOG_LOCAL0);
	}

	socketUDP = Socket(resUDP->ai_family, resUDP->ai_socktype, resUDP->ai_protocol);

	Bind(socketUDP, resUDP->ai_addr, resUDP->ai_addrlen);

	struct sockaddr_in clientSockaddr;
	socklen_t clientLen = sizeof clientSockaddr;
	
	while (true) {
		Recvfrom (socketUDP, buf, sizeof buf, 0, (struct sockaddr *) &clientSockaddr, &clientLen);

		pid_t forkValue;
		forkValue = fork();
		if(forkValue == 0){
			execute_child_proccess(buf, &clientSockaddr, clientLen);
		}
	}

	return 0;
}



























