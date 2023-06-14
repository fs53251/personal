CC = gcc
CFLAGS = -Wall -g

default: bot UDP_server

bot: bot.c
	$(CC) $(CFLAGS) bot.c -o bot

UDP_server: UDP_server.c
	$(CC) $(CFLAGS) UDP_server.c -o UDP_server

clean:
	rm -f bot UDP_server *.o
