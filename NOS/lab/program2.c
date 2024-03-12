#include<fcntl.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/poll.h>
#include<unistd.h>
#include<time.h>

#define DEVICE_PREFIX "/dev/shofer"

int main() {
	int num_devices = 6;
	srand(time(NULL));
	
	struct pollfd *fds = (struct pollfd *)malloc(num_devices * sizeof(struct pollfd));
	if(!fds) {
		perror("Malloc failed\n");
		return 1;
	}
	
	//open devices for writing
	for(int i = 0; i < num_devices; ++i) {
		char device_path[20];
		snprintf(device_path, sizeof(device_path), "%s%d", DEVICE_PREFIX, i);
		fds[i].fd = open(device_path, O_WRONLY | O_NONBLOCK);
		fds[i].events = POLLOUT;
	}
	
	while(1) {
		int ret = poll(fds, num_devices, -1);
		if(ret < 0) {
			perror("Poll failed\n");
			break;
		}
		
		//random choose device that is ready to write
		int randomIndex = rand() % num_devices;
		if(fds[randomIndex].revents && POLLOUT) {
			char buffer[1] = {'A' + (rand() % 26)};
			ssize_t bytesWritten = write(fds[randomIndex].fd, buffer, sizeof(buffer));
			if(bytesWritten > 0) {
				printf("Write to %s%d: %c\n", DEVICE_PREFIX, randomIndex, buffer[0]);
			}
		}
		
		sleep(5);
	}
	
	//close all devices
	for(int i = 0; i < num_devices; ++i) {
		close(fds[i].fd);
	}
	
	free(fds);
	
	return 0;
}


























