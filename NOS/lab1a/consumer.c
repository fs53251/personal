#include "includes.h"

void child_job(int child){
	//sincronization structs
	struct command ccomand;
	struct my_msgbuf buf;
	
	//K.O struct
	struct ko_buf char_message;
	struct msqid_ds msq_ds;
	
	char buffer[100];
	int buf_pos = 0;
	int r;
	
	signal(SIGINT, cleanupAndExit);
	
	//sincronization MQ
	int msqid = Msgget(MQ_TCP_KEY, 0600 | IPC_CREAT);
	
	//K.O MQ, set to max 1 byte data
	int msqid_ko = Msgget(MQ_KEY, 0600 | IPC_CREAT);
	Msgctl(msqid_ko, IPC_STAT, &msq_ds);
	msq_ds.msg_qbytes = 1;
	Msgctl(msqid_ko, IPC_SET, &msq_ds);
	
	while(1){
		//ask CENTRAL to enter K.O
		ccomand.com = 'Z';
		ccomand.i = child;
		buf.mtype = CENTRAL;
		buf.cmd = ccomand;
		printf("Child %d, send Z(%d) to CENTRAL.\n", child, child);
		Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);

		//wait for CENTRAL response
		Msgrcv(msqid, (struct msgbuf *)&buf, sizeof(struct my_msgbuf) - sizeof(long), child, 0);
		printf("Child %d, recieved response from CENTRAL.\n", child);
		
		//enter K.O, check if there is message for me
		if(buf.cmd.com = 'O' && buf.cmd.i == child){
			if(msgrcv(msqid_ko, (struct msgbuf *)&char_message, sizeof(char_message) -sizeof(long), child, IPC_NOWAIT) != -1){
				if(char_message.c == '\0'){
					buffer[buf_pos] = '\0';
					printf("Recieved message: %s\n\n\n", buffer);
					buf_pos = 0;
				} else{
					printf("C%d: %c\n", child, char_message.c);
					buffer[buf_pos++] = char_message.c;
				}
			}
		}
		
		//exit K.O
		ccomand.com = 'I';
		ccomand.i = child;
		buf.mtype = CENTRAL;
		buf.cmd = ccomand;
		Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
		printf("Send I(%d) to CENTRAL.\n", child);
	}
	
	
	Msgctl(msqid, IPC_RMID, NULL);
	Msgctl(msqid_ko, IPC_RMID, NULL);
}

int main() {
	pid_t child_pid;
	int status;

	//create consumers
	for (int i = 3; i <= 5; i++) {
		child_pid = fork();

		if (child_pid == -1) {
		    perror("Fork failed");
		    exit(1);
		} else if (child_pid == 0) {
		    child_job(i);
		}
	}

	for (int i = 0; i < 3; i++) {
		wait(&status);
	}
	return 0;
}
