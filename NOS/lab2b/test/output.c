#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

#define DEVICE_FILE "/dev/shofer_out"
#define BUFFER_SIZE 256

int main() {
    int fd;
    char buffer[BUFFER_SIZE];

    while (1) {
        fd = open(DEVICE_FILE, O_RDONLY);
        if (fd == -1) {
            perror("Error opening device file");
            return 1;
        }
        ssize_t bytesRead = read(fd, buffer, sizeof(buffer));
        if (bytesRead == -1) {
            perror("Error reading from device");
            close(fd);
            return 1;
        }

        printf("Read from device: %.*s\n", (int)bytesRead, buffer);
        close(fd);
        sleep(3);
    }

    return 0;
}

