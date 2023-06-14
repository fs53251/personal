CC = gcc
CFLAGS = 
#CFLAGS = -Wall
OBJS = tftpserver.o

all: tftpserver

tftpserver:
	$(CC) $(CFLAGS) -c -o $@.o $@.c
	$(CC) $(CFLAGS) -o $@ $(OBJS)

clean:
	-rm -f tftpserver core *.o *~
