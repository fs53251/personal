#include "includes.h"

void child_job(int child, int pfd[NUM_PIPES][2], int msqid){

	//initialize fds
	int reading_fds[3];
	int writing_fds[3];
	initialize_child_fds(reading_fds, writing_fds, child, 3);
	
	//remove unused pipes based on child number (HARDCODED)
	close_pipes(reading_fds, 3, writing_fds, 3, pfd);
	
	//find child number (CHILD1 - 1,  CHILD2 - 2,  CHILD3 - 3)
	int child_number = find_child_num(child);
	
	//get 2 missing ingredients
	int r1, r2;
	generateMissingProducts(&r1, &r2, child_number);
	
	//map process -> fd (T -> Pread: _, Pwrite: _)
	struct map process_fd[3];
	generateMappingProcess(child_number, process_fd, pfd);
	
	//generate Ci
	int Ci = generateRandomNumber();
	printf("C%d, Ct: %d\n", child_number, Ci);
	
	while(1){
	
		//wait for products, message from T
		struct message recieve;
		while(1){
			recieve = wait_response(reading_fds, 3, pfd);
			if(recieve.c == 'S'){
				if((recieve.t1 == r1 && recieve.t2 == r2) || (recieve.t1 == r2 && recieve.t2 == r1)){
					printf("C%d, recieved S(%d, %d).\n", child_number, r1, r2);
					break;
				} else{
					printf("C%d, recieved S, but dont need this products.\n", child_number);
				}
			}else if(recieve.c == 'Z'){
				Ci = maxAndIncrement(Ci, recieve.t2);
				printf("C%d, recieved Z(%d, %d)\n", child_number, recieve.t1, recieve.t2);
				printf("C%d, send O(%d, %d)\n", child_number, child_number, recieve.t2);
				process_Z_direct(process_fd, 3, recieve, child_number);
			}
		}
		
		//permission to enter K.O
		printf("C%d, send Z(%d, %d) to everyone.\n", child_number, child_number, Ci);
		struct message send = send_Z(writing_fds, 3, pfd, 'Z', child_number, Ci);
		
		//get response: O(j, T(i)) or Z(i, T(i))
		int num_responses = 0;
		int response_check[] = {-1, -1, -1};
		struct process didnt_response[10];
		int num_didnt_reponse = 0;
		
		while(num_responses != 3){
			recieve = wait_response(reading_fds, 3, pfd);
	
			if(recieve.c == 'O'){
				process_O(send, recieve, response_check, &num_responses);
				printf("C%d, recieved O(%d, %d)\n", child_number, recieve.t1, recieve.t2);
			}else if(recieve.c == 'Z'){
				Ci = maxAndIncrement(Ci, recieve.t2);
				process_Z(process_fd, 3, send, recieve, didnt_response, &num_didnt_reponse);
				printf("C%d, recieved Z(%d, %d)\n", child_number, recieve.t1, recieve.t2);
			}
		}
		
		//enter K.O (take products s1, s2 from desk)
		take_products(&r1, &r2, msqid);
		printf("C%d,enter K.O, take s1: %d, s2: %d\n", child_number, r1, r2);
		
		//exit K.O
		exit_KO(didnt_response, num_didnt_reponse, child_number);
		printf("C%d, exit K.O\n", child_number);
		
		//send to T, products (s1, s2) taken
		int Tpipe;
		for(int T = 0; T < 3; T++){
			if(process_fd[T].p == SALESMAN){
				Tpipe = process_fd[T].Pwrite;
			}
			
			
		}
		printf("C%d, send S(%d, %d) to notify T\n", child_number, r1, r2);
		notify_T(r1, r2, Tpipe);
		printf("C%d, I'm smoking.\n", child_number);
	}
}

void parent_job(int pfd[NUM_PIPES][2], int msqid){

	//close unused file descriptors
	int reading_fds[] = {1, 3, 5};
	int writing_fds[] = {0, 2, 4};
	close_pipes(reading_fds, 3, writing_fds, 3, pfd);
	
	//map process -> fd  (CHILD1 -> Pread: 1, Pwrite: 0)...
	struct map process_fd[] = {
		{CHILD1, pfd[1][READ], pfd[0][WRITE]},
		{CHILD2, pfd[3][READ], pfd[2][WRITE]},
		{CHILD3, pfd[5][READ], pfd[4][WRITE]}
	};
	
	//generate Ct
	int Ct = generateRandomNumber();
	printf("T, Ct: %d\n", Ct);
	
	while(1){
	
		//get 2 radnom ingredients(1, 2, 3)
		int s1, s2;
		generateTwoRandomNumbers(&s1, &s2);
		printf("T, s1: %d, s2: %d\n", s1, s2);
		
		//permission to enter K.O
		struct message send = send_Z(writing_fds, 3, pfd, 'Z', SALESMAN, Ct);
		printf("T, send Z(%d, %d) to everyone.\n", send.t1, send.t2);
		
		//get response: O(j, T(i)) or Z(i, T(i))
		int num_responses = 0;
		struct message recieve;
		int response_check[] = {-1, -1, -1};
		struct process didnt_response[10];
		int num_didnt_reponse = 0;
		
		while(num_responses != 3){
			recieve = wait_response(reading_fds, 3, pfd);
			
			if(recieve.c == 'O'){
				process_O(send, recieve, response_check, &num_responses);
				printf("T, recieved O(%d, %d)\n", recieve.t1, recieve.t2);
			}else if(recieve.c == 'Z'){
				Ct = maxAndIncrement(Ct, recieve.t2);
				process_Z(process_fd, 3, send, recieve, didnt_response, &num_didnt_reponse);
				printf("T, recieved Z(%d, %d)\n", recieve.t1, recieve.t2);
			}
		}
		
		//enter K.O (add products s1, s2 to desk)
		put_products(s1, s2, msqid);
		printf("T,enter K.O, put s1: %d, s2: %d\n", s1, s2);
		
		//exit K.O
		exit_KO(didnt_response, num_didnt_reponse, SALESMAN);
		printf("T, exit K.O\n");
		
		//send to consumers, products (s1, s2) available
		notify_consumers(s1, s2, process_fd, 3);
		printf("T, send S(%d, %d) to notify consumers\n", s1, s2);
		
		//wait for consumers to pick up products
		while(1){
			recieve = wait_response(reading_fds, 3, pfd);
			if(recieve.c == 'S'){
				if((recieve.t1 == s1 && recieve.t2 == s2) || (recieve.t1 == s2 && recieve.t2 == s1)){
					printf("T, consumer picked up product.\n");
					break;
				} else{
					perror("Someone picked up wrong products!\n");
				}
			}else if(recieve.c == 'Z'){
				Ct = maxAndIncrement(Ct, recieve.t2);
				process_Z_direct(process_fd, 3, recieve, SALESMAN);
				printf("T, recieved Z(%d, %d)\n", recieve.t1, recieve.t2);
				printf("T, send O(%d, %d)\n", SALESMAN, recieve.t2);
			}
		}
		sleep(1);
		printf("\n\n");
	}
}

int main(){
	
	//generate pipes
	int pfd[NUM_PIPES][2];
	initializePipes(pfd);
	
	//create K.O. (message queue)
	int msqid = Msgget(getuid(), 0600 | IPC_CREAT);
	
	signal(SIGINT, cleanupAndExit);
	
	//create children
	for(int child = 0; child < NUM_CHILDREN; child++){
		switch(fork()){
			case -1:
				exit(1);
				
			case 0: //child
				sleep(1);
				child_job(child, pfd, msqid);
				closeAllPipes(pfd);
				Msgctl(msqid, IPC_RMID, NULL);
				exit(1);
				
			default:
		}
	}
	
	parent_job(pfd, msqid);
	
	//close pipes and message queue
	closeAllPipes(pfd);
	Msgctl(msqid, IPC_RMID, NULL);
	
	return 0;
}
