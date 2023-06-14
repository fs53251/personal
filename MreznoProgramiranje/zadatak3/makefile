CC = gcc

SERVER_SRC = tcpserver.c
CLIENT_SRC = tcpklijent.c

SERVER_EXE = tcpserver
CLIENT_EXE = tcpklijent

SRCS := tcpserver.c tcpklijent.c makefile

.PHONY: clean

all: $(SERVER_EXE) $(CLIENT_EXE)

$(SERVER_EXE): $(SERVER_SRC)
	$(CC) -o $(SERVER_EXE) $(SERVER_SRC)

$(CLIENT_EXE): $(CLIENT_SRC)
	$(CC) -o $(CLIENT_EXE) $(CLIENT_SRC)
clean:
	rm -f $(filter-out $(SRCS), $(wildcard *))
