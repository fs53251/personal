#! /bin/bash

# Compile shofer module
make

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."

        # Compile user programs
        gcc -g program1.c -o program1
        gcc -g program2.c -o program2

        # Check if user programs were compiled successfully
        if [ $? -eq 0 ]; then
            echo "User programs compiled successfully."

            # Run user programs in separate terminals
            gnome-terminal -- gdb -ex run --args "./program1" &
            gnome-terminal -- gdb -ex run --args "./program2" &
        else
            echo "User program compilation failed. Check errors."
        fi
else
    echo "Module compilation failed. Check errors."
fi

