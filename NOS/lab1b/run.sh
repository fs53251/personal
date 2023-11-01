#!/bin/bash

# Compile the C program with debugging information
gcc -g -o my_program main.c functions.c

# Check if compilation was successful
if [ $? -eq 0 ]; then
    # Run the program within GDB
    gdb -ex "file ./my_program"
else
    echo "Compilation failed."
fi

