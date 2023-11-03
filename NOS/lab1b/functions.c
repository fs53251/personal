#include "includes.h"

int maxAndIncrement(int a, int b){
	return a > b ? (a + 1) : (b + 1);
}

int generateTwoRandomNumbers(int *num1, int *num2){
	srand(time(NULL));
	*num1 = (rand() % 3) + 1;
	
	do{
		*num2 = (rand() % 3) + 1;
	} while(*num2 == *num1);
}

void initializePipes(int pfd[NUM_PIPES][2]) {
    for (int i = 0; i < NUM_PIPES; i++) {
    	Pipe(pfd[i]);
    }
}

void closeAllPipes(int pfd[NUM_PIPES][2]){
	for (int i = 0; i < NUM_PIPES; i++) {
	    	close(pfd[i][READ]);
	    	close(pfd[i][WRITE]);
	}
}

void close_pipes(int close_write[], int len1, int close_read[], int len2, int (*pfd)[2]){
	for(int pipes = 0; pipes < NUM_PIPES; pipes++){
		int changed = -1;
		for(int i = 0; i < len1; i++){
			if(pipes == close_write[i]){
				close(pfd[pipes][WRITE]);
				changed = 1;
			}
		}
		
		for(int i = 0; i < len2; i++){
			if(pipes == close_read[i]){
				close(pfd[pipes][READ]);
				changed = 1;
			}
		}
		
		if(changed == -1){
			close(pfd[pipes][READ]);
			close(pfd[pipes][WRITE]);
		}
	}
}

void cleanupAndExit(int signal){
	int msqid = Msgget(getuid(), 0600);
	Msgctl(msqid, IPC_RMID, NULL);
}

int generateRandomNumber(){
	srand(getpid());
	return (rand() % 500) + 1;
}

struct message send_Z(int writing_fds[], int len, int pfd[NUM_PIPES][2], char c, int t1, int t2){
	
	struct message m;
	m.c = c;
	m.t1 = t1;
	m.t2 = t2;
	
	for(int i = 0; i < len; i++){
		Write(pfd[writing_fds[i]][WRITE], &m, sizeof(struct message));
	}
	
	return m; 
}

struct message send_message(int pfd, char c, int t1, int t2){
	struct message m;
	m.c = c;
	m.t1 = t1;
	m.t2 = t2;

	Write(pfd, &m, sizeof(struct message));
	
	return m; 
}



struct message wait_response(int reading_fds[], int len, int pfd[NUM_PIPES][2]){
	fd_set readfds;
	int p[len];
	struct message m;
	
	for(int i = 0; i < len; i++){
		p[i] = pfd[reading_fds[i]][READ];
	}
	
	FD_ZERO(&readfds);
	int maxfd = -1;
	
	for(int i = 0; i < len; i++){
		FD_SET(p[i], &readfds);
		maxfd = (p[i] > maxfd) ? p[i] : maxfd;
	}
	
	Select(maxfd + 1, &readfds, NULL, NULL, NULL);
	
	for(int i = 0; i < len; i++){
		if(FD_ISSET(p[i], &readfds)){
			Read(p[i], &m, sizeof(struct message));
			return m;
		}
	}
}

bool isNumberInArray(int arr[], int size, int number){
	for(int i = 0; i < size; i++){
		if(arr[i] == number){
			return true;
		}
	}
	
	return false;
}

void process_O(struct message send, struct message recieve, int response_check[], int *num_responses){
	if(send.t2 == recieve.t2){
		if(!isNumberInArray(response_check, 3, recieve.t1)){
			response_check[*num_responses] = recieve.t1;
			(*num_responses)++;
		}else {
			perror("Recieved double O.\n");
		}
	} else{
		perror("Recieved O that has T(i) different than Z(i, T(i)).\n");
	}
}

void process_Z(struct map process_fd[] , int len, struct message send, struct message recieve, struct process didnt_response[], int *num_didnt_response){
	int sender = recieve.t1;
	if(send.t2 > recieve.t2){
		for(int i = 0; i < len; i++){
			if(process_fd[i].p == sender){
				send_message(process_fd[i].Pwrite, 'O', send.t1, recieve.t2);
			}
		}
	}else{
		for(int i = 0; i < len; i++){
			if(process_fd[i].p == sender){
				struct process proc;
				proc.Pj = process_fd[i].Pwrite;
				proc.Tj = recieve.t2;
				
				didnt_response[*num_didnt_response] = proc;
				(*num_didnt_response)++;
			}
		}
	}
}

void process_Z_direct(struct map process_fd[], int len, struct message recieve, int p_name){
	int sender = recieve.t1;
	for(int i = 0; i < len; i++){
		if(process_fd[i].p == sender){
			send_message(process_fd[i].Pwrite, 'O', p_name, recieve.t2);
		}
	}
}

void put_products(int s1, int s2, int msqid){
	struct my_msgbuf buf;
	buf.mtype = 1;
	buf.s1 = s1;
	buf.s2 = s2;
	
	Msgsnd(msqid, &buf, sizeof(struct my_msgbuf) - sizeof(long), 0);
}

void take_products(int *r1, int *r2, int msqid){
	struct my_msgbuf buf;
	
	Msgrcv(msqid, &buf, sizeof(struct my_msgbuf) - sizeof(long), 1, 0);
	(*r1) = buf.s1;
	(*r2) = buf.s2;
}

void exit_KO(struct process didnt_response[], int num_didnt_response, int p_name){
	for(int i = 0; i < num_didnt_response; i++){
		send_message(didnt_response[i].Pj, 'O', p_name, didnt_response[i].Tj);
	}
}

void notify_consumers(int s1, int s2, struct map process_fd[], int len){
	for(int i = 0; i < len; i++){
		send_message(process_fd[i].Pwrite, 'S', s1, s2);
	}
}

void notify_T(int r1, int r2, int Tpipe){
	send_message(Tpipe, 'S', r1, r2);
}

int find_child_num(int child){
	switch(child){
		case 0:
			return CHILD1;
		case 1:
			return CHILD2;
		case 2:
			return CHILD3;
	}
}

void generateMissingProducts(int *s1, int *s2, int child_number){
	bool first = false;
	for(int i = 1; i <=3; i++){
		if(child_number != i){
			if(first == true){
				(*s2) = i;
			} else{
				(*s1) = i;
				first = true;
			}
		}
	}
}

//hardcoded
void initialize_child_fds(int reading_fds[], int writing_fds[], int child, int size) {

    int *close_write = (int *)malloc(size * sizeof(int));
    int *close_read = (int *)malloc(size * sizeof(int));

    if (close_write == NULL || close_read == NULL) {
        fprintf(stderr, "Memory allocation failed.\n");
        exit(1);
    }
    
    if (child == 0) { // First child
        close_write[0] = 0;
        close_write[1] = 6;
        close_write[2] = 8;

        close_read[0] = 1;
        close_read[1] = 7;
        close_read[2] = 9;
    } else if (child == 1) { // Second child
        close_write[0] = 2;
        close_write[1] = 7;
        close_write[2] = 10;

        close_read[0] = 3;
        close_read[1] = 6;
        close_read[2] = 11;
    } else if (child == 2) { // Third child
        close_write[0] = 4;
        close_write[1] = 9;
        close_write[2] = 11;

        close_read[0] = 5;
        close_read[1] = 8;
        close_read[2] = 10;
    }
    for (int i = 0; i < size; i++) {
        reading_fds[i] = close_write[i];
        writing_fds[i] = close_read[i];
    }

    free(close_write);
    free(close_read);
}

//hardcoded
void generateMappingProcess(int child_number, struct map process_fd[], int pfd[NUM_PIPES][2]) {

    switch (child_number) {
        case 1:
            process_fd[0] = (struct map){SALESMAN, pfd[0][READ], pfd[1][WRITE]};
            process_fd[1] = (struct map){CHILD2, pfd[6][READ], pfd[7][WRITE]};
            process_fd[2] = (struct map){CHILD3, pfd[8][READ], pfd[9][WRITE]};
            break;
        case 2:
            process_fd[0] = (struct map){SALESMAN, pfd[2][READ], pfd[3][WRITE]};
            process_fd[1] = (struct map){CHILD1, pfd[7][READ], pfd[6][WRITE]};
            process_fd[2] = (struct map){CHILD3, pfd[10][READ], pfd[11][WRITE]};
            break;
        case 3:
            process_fd[0] = (struct map){SALESMAN, pfd[4][READ], pfd[5][WRITE]};
            process_fd[1] = (struct map){CHILD1, pfd[9][READ], pfd[8][WRITE]};
            process_fd[2] = (struct map){CHILD2, pfd[11][READ], pfd[10][WRITE]};
            break;
    }
}

//wrappers
int Pipe(int fds[2]){
	int r;
	
	if((r = pipe(fds)) == -1){
		perror("pipe");
		exit(1);
	}
	
	return r;
}

int Msgget(key_t key, int msgflg){
	int r;
	
	if((r = msgget(key, msgflg)) == -1){
		perror("msgget");
		exit(1);
	}
	
	return r;
}

int Msgsnd(int msqid, const void *msgp, size_t msgsz, int msgflg){
	int r;
	
	if((r = msgsnd(msqid, msgp, msgsz, msgflg)) == -1){
		perror("msgsnd");
		exit(1);
	}
	
	return r;
}

ssize_t Msgrcv(int msqid, void *msgp, size_t msgsz, long msgtyp, int msgflg){
	ssize_t r;
	
	if((r = msgrcv(msqid, msgp, msgsz, msgtyp, msgflg)) == -1){
		perror("msgrcv");
		exit(1);
	}
}

int Msgctl(int msqid, int cmd, struct msqid_ds *buf){
	int r;
	
	if((r = msgctl(msqid, cmd, buf)) == -1){
		perror("msgctl");
		exit(1);
	}
	
	return r;
}

ssize_t Write(int fd, const void *buf, size_t nbyte){
	int r;
	
	if((r = write(fd, buf, nbyte)) == -1){
		perror("write");
		exit(1);
	}
	
	return r;
}

ssize_t Read(int fd, void *buf, size_t nbyte){
	int r;
	
	if((r = read(fd, buf, nbyte)) == -1){
		perror("read");
		exit(1);
	}
}

int Select(int nfds, fd_set *readfds, fd_set *writefds, fd_set *exceptfds, struct timeval *timeout){
	int r;
	
	if((r = select(nfds, readfds, writefds, exceptfds, timeout)) == -1){
		perror("select");
		exit(1);
	}
	
	return r;
}











