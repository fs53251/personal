#!/bin/bash

# Compile ioctl.c with debugging information
gcc -o ioctl_program -g ioctl.c

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation of ioctl_program successful. Running program with gdb..."

    # Run the ioctl program with gdb and pass command-line arguments
    gnome-terminal --tab --title="ioctl_program" -- bash -c "
        gdb -ex 'run /dev/shofer_control 5' ./ioctl_program;
        read -p 'Press Enter to close this terminal.'";

else
    echo "Compilation of ioctl_program failed. Please fix the errors before running the program."
fi

# Compile output.c with debugging information
gcc -o output_program -g output.c

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation of output_program successful. Running program..."

    # Run the output program in a different terminal
    gnome-terminal --tab --title="output_program" -- bash -c "
        ./output_program;
        read -p 'Press Enter to close this terminal.'";

else
    echo "Compilation of output_program failed. Please fix the errors before running the program."
fi

