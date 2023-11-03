#include "includes.h"

void cleanupAndExit(int signal){
	int msqid = msgget(MQ_TCP_KEY, 0600);
	Msgctl(msqid, IPC_RMID, NULL);
	
	msqid = msgget(MQ_KEY, 0600);
	Msgctl(msqid, IPC_RMID, NULL);
}

//wrappers
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
