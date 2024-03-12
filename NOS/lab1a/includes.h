#ifndef INCLUDES_H
#define INCLUDES_H

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/ipc.h>
#include<sys/msg.h>
#include<sys/wait.h>
#include<time.h>
#include<signal.h>

#define MQ_TCP_KEY 10
#define MQ_KEY 11
#define NUM_CONSUMERS 3
#define CENTRAL 1
#define PRODUCER 2
#define NUM_ROW_Z 20

struct ko_buf{
	long mtype;
	char c;
};

struct command{
	char com;
	int i;
};

struct my_msgbuf{
	long mtype;
	struct command cmd;
};

void cleanupAndExit(int signal);

//wrappers
int Msgget(key_t key, int msgflg);
int Msgctl(int msqid, int cmd, struct msqid_ds *buf);
int Msgsnd(int msqid, const void *msgp, size_t msgsz, int msgflg);
ssize_t Msgrcv(int msqid, void *msgp, size_t msgsz, long msgtyp, int msgflg);

#endif
