CC = mpicc
CFLAGS = -Wall -Wextra -std=c99
LDFLAGS = -lm

SRCDIR = src
INCDIR = include
BUILDDIR = build
TARGET = main

SRCS = $(wildcard $(SRCDIR)/*.c)
OBJS = $(SRCS:$(SRCDIR)/%.c=$(BUILDDIR)/%.o)
DEPS = $(wildcard $(INCDIR)/*.h)

.PHONY: all clean run

all: $(BUILDDIR)/$(TARGET)

$(BUILDDIR)/$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $^ -o $@ $(LDFLAGS)

$(BUILDDIR)/%.o: $(SRCDIR)/%.c $(DEPS)
	@mkdir -p $(BUILDDIR)
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $@

clean:
	rm -rf $(BUILDDIR)

run: $(BUILDDIR)/$(TARGET)
	mpirun -np $(ARGS) $(BUILDDIR)/$(TARGET)


