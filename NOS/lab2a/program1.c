#include<fcntl.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/poll.h>
#include<unistd.h>

#define DEVICE_PREFIX "/dev/shofer"

int main() {
	int num_devices = 6;
	struct pollfd *fds = (struct pollfd *)malloc(num_devices * sizeof(struct pollfd));
	if(!fds) {
		perror("Malloc failed\n");
		return 1;
	}
	
	//open devices for reading
	for(int i = 0; i < num_devices; ++i) {
		char device_path[20];
		snprintf(device_path, sizeof(device_path), "%s%d", DEVICE_PREFIX, i);
		fds[i].fd = open(device_path, O_RDONLY | O_NONBLOCK);
		fds[i].events = POLLIN;
	}
	int lastReadDevice = -1;
	while(1) {
		//whait for character on device
		int ret = poll(fds, num_devices, -1);
		if(ret < 0) {
			perror("Poll failed\n");
			break;
		}
		
		//read and print character from device
		for (int i = 0; i < num_devices; ++i) {
		    if (fds[i].revents & POLLIN) {
		        char buffer[1];
		        ssize_t bytesRead = read(fds[i].fd, buffer, sizeof(buffer));
		        if (bytesRead > 0) {
		            lastReadDevice = i;
		            printf("Read from %s%d: %c\n", DEVICE_PREFIX, lastReadDevice, buffer[0]);
		        }
		    }
		}
	}
	
	//close all devices
	for(int i = 0; i < num_devices; ++i) {
		close(fds[i].fd);
	}
	
	free(fds);
	
	return 0;
}




























