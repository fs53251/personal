#ifndef INCLUDES_H
#define INCLUDES_H

#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<time.h>
#include<stdbool.h>
#include<errno.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/ipc.h>
#include<sys/msg.h>
#include<signal.h>
#include<fcntl.h>
#include<sys/time.h>
#include<sys/select.h>

#define WRITE 1
#define READ 0
#define NUM_PIPES 12
#define NUM_CHILDREN 3
#define SALESMAN 0
#define CHILD1 1
#define CHILD2 2
#define CHILD3 3

struct message {
	char c;
	int t1;
	int t2;
};

struct process {
	int Pj;
	int Tj;
};

struct map {
	int p;
	int Pread;
	int Pwrite;
};

struct my_msgbuf{
	long mtype;
	int s1;
	int s2;
};

int maxAndIncrement(int a, int b);
int generateTwoRandomNumbers(int *num1, int *num2);
int generateRandomNumber();
void initializePipes(int pfd[NUM_PIPES][2]);
void closeAllPipes(int pfd[NUM_PIPES][2]);
void close_pipes(int close_write[], int len1, int close_read[], int len2, int (*pfd)[2]);
void cleanupAndExit(int signal);
struct message wait_response(int reading_fds[], int len, int pfd[NUM_PIPES][2]);
struct message send_Z(int writing_fds[], int len, int pfd[NUM_PIPES][2], char c, int t1, int t2);
struct message send_message(int pfd, char c, int t1, int t2);
struct message wait_response(int reading_fds[], int len, int pfd[NUM_PIPES][2]);
bool isNumberInArray(int arr[], int size, int number);
void process_O(struct message send, struct message recieve, int response_check[], int *num_responses);
void process_Z(struct map process_fd[] , int len, struct message send, struct message recieve, struct process didnt_response[], int *num_didnt_response);
void process_Z_direct(struct map process_fd[], int len, struct message recieve, int p_name);
void put_products(int s1, int s2, int msqid);
void take_products(int *r1, int *r2, int msqid);
void exit_KO(struct process didnt_response[], int num_didnt_response, int p_name);
void notify_consumers(int s1, int s2, struct map process_fd[], int len);
void notify_T(int r1, int r2, int Tpipe);
int find_child_num(int child);
void generateMissingProducts(int *s1, int *s2, int child_number);
void initialize_child_fds(int reading_fds[], int writing_fds[], int child, int size);
void generateMappingProcess(int child_number, struct map process_fd[], int pfd[NUM_PIPES][2]);

//wrappers
int Pipe(int fds[2]);
int Msgget(key_t key, int msgflg);
int Msgctl(int msqid, int cmd, struct msqid_ds *buf);
int Msgsnd(int msqid, const void *msgp, size_t msgsz, int msgflg);
ssize_t Msgrcv(int msqid, void *msgp, size_t msgsz, long msgtyp, int msgflg);
ssize_t Write(int fd, const void *buf, size_t nbyte);
ssize_t Read(int fd, void *buf, size_t nbyte);
int Select(int nfds, fd_set *readfds, fd_set *writefds, fd_set *exceptfds, struct timeval *timeout);

#endif
