CC = gcc

SERVER_SRC = server.c
CLIENT_SRC = klijent.c

SERVER_EXE = server
CLIENT_EXE = klijent

SRCS := server.c klijent.c makefile

.PHONY: clean

all: $(SERVER_EXE) $(CLIENT_EXE)

$(SERVER_EXE): $(SERVER_SRC)
	$(CC) -o $(SERVER_EXE) $(SERVER_SRC)

$(CLIENT_EXE): $(CLIENT_SRC)
	$(CC) -o $(CLIENT_EXE) $(CLIENT_SRC)
clean:
	rm -f $(filter-out $(SRCS), $(wildcard *))
