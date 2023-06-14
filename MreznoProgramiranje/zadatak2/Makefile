PROJECT = prog
SOURCE = prog.c

CFLAGS = -Wall -g
LDFLAGS =
OBJECTS = ${SOURCE:.c=.o}

$(PROJECT): $(OBJECTS)
	$(CC) $(OBJECTS) -o $(PROJECT)
	
$(OBJECTS): $(HEADERS)

clean:
	-rm -f prog *.o 
