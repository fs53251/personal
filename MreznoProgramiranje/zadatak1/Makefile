PROJECT = proba
SOURCE = proba.c
HEADERS = proba.h

CFLAGS = -Wall -g
LDFLAGS =
OBJECTS = ${SOURCE:.c=.o}

$(PROJECT): $(OBJECTS)
	$(CC) $(OBJECTS) -o $(PROJECT)
	
$(OBJECTS): $(HEADERS)

clean:
	-rm -f proba *.o 
