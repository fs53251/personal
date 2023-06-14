CC = gcc

# Compiler flags
CFLAGS = -Wall -g

# Source files
SERVER_SRC = server.c
BOT_SRC = bot.c

# Output files
SERVER_OUT = server
BOT_OUT = bot

# Default target
all: $(SERVER_OUT) $(BOT_OUT)

# Compile server
$(SERVER_OUT): $(SERVER_SRC)
	$(CC) $(CFLAGS) -o $(SERVER_OUT) $(SERVER_SRC)

# Compile bot
$(BOT_OUT): $(BOT_SRC)
	$(CC) $(CFLAGS) -o $(BOT_OUT) $(BOT_SRC)

# Clean target
clean:
	rm -f $(SERVER_OUT) $(BOT_OUT) *.o
