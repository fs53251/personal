#include "includes.h"

int main(){
	//sincronization structs
	struct command ccomand;
	struct my_msgbuf buf;
	
	//K.O struct
	struct ko_buf char_message;
	struct msqid_ds msq_ds;
	
	char buffer[10];
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
		//user input (max 10 chars)
		printf("Enter text that will be sent to consumer:\n");
		fgets(buffer, sizeof(buffer), stdin);
		size_t len = strlen(buffer);
		if(len > 0 && buffer[len - 1] == '\n'){
			buffer[len - 1] = '\0';
		}
		
		//random chose consumer
		srand(time(NULL));
		r = (rand() % NUM_CONSUMERS) + 3;

		//send message
		for(int i = 0; i < len; i++){
			//create Z(PRODUCER)
			ccomand.com = 'Z';
			ccomand.i = PRODUCER;
			
			//send to CENTRAL
			buf.mtype = CENTRAL;
			buf.cmd = ccomand;
			printf("Sending Z(%d) to CENTRAL\n", buf.cmd.i);
			
			//ask CENTRAl for permission to enter in K.O
			Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
			
			//wait response from CENTRAL
			Msgrcv(msqid, (struct msgbuf *)&buf, sizeof(struct my_msgbuf) - sizeof(long), PRODUCER, 0);
			printf("Response from CENTRAL\n");
			
			if(buf.cmd.com == 'O' && buf.cmd.i == PRODUCER){

				//enter K.O send letter to r producer
				char_message.mtype = r;
				char_message.c = buffer[i];
				printf("Sending char: %c\n", buffer[i]);
				Msgsnd(msqid_ko, (struct msgbuf *)&char_message, sizeof(char), 0);
				
				//exit K.O
				ccomand.com = 'I';
				ccomand.i = PRODUCER;
				buf.mtype = CENTRAL;
				buf.cmd = ccomand;
				printf("Sending I(%d) to CENTRAL\n", buf.cmd.i);
				Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(struct command), 0);
				
			} else{
				printf("Recieved wrong message.\n");
				exit(1);
			}
			
		}
	}
	
	Msgctl(msqid, IPC_RMID, NULL);
	Msgctl(msqid_ko, IPC_RMID, NULL);
	
	return 0;
}
