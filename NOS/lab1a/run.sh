#!/bin/bash

# Compile the C files
gcc -g -o central central.c functions.c
gcc -g -o consumer consumer.c functions.c
gcc -g -o producer producer.c functions.c

# Open different terminals with GDB
gnome-terminal -- bash -c "gdb ./central"
gnome-terminal -- bash -c "gdb ./consumer"
gnome-terminal -- bash -c "gdb ./producer"

