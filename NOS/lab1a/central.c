#include "includes.h"

void addToRow(int i, int red_z[NUM_ROW_Z], int *broj_elemenata){
	if(*broj_elemenata < NUM_ROW_Z){
		red_z[*broj_elemenata] = i;
		(*broj_elemenata)++;
		printf("Element dodan u red zahtjeva: Z(%d)\n", i);
	}else{
		printf("Polje zahtijeva je punno, ne možete dodati više elemenata.\n");
	}
}

int removeFromRow(int target, int red_z[NUM_ROW_Z], int *broj_elemenata){
	int index_removing = -1;
	for(int i = 0; i < *broj_elemenata; i++){
		if(red_z[i] == target){
			index_removing = i;
			break;
		}
	}
	
	if(index_removing >= 0){
		printf("Uklonjen element iz reda zahtjeva: Z(%d)\n", red_z[index_removing]);
		
		for(int i = index_removing; i < (*broj_elemenata - 1); i++){
			red_z[i] = red_z[i + 1];
		}
		
		(*broj_elemenata)--;
		return 1;
	} else{
		printf("Nema elemenata u polju s odgovarajućim zahtjevom\n");
		return -1;
	}
}

int main(void){
	//sincronization structs
	struct command ccomand;
	struct my_msgbuf buf;
	
	//K.O struct
	struct ko_buf char_message;
	struct msqid_ds msq_ds;
	
	char buffer[10];
	int r;
	
	signal(SIGINT, cleanupAndExit);
	
	//sincronization MQ
	int msqid = Msgget(MQ_TCP_KEY, 0600 | IPC_CREAT);
	
	//K.O MQ, set to max 1 byte data
	int msqid_ko = Msgget(MQ_KEY, 0600 | IPC_CREAT);
	Msgctl(msqid_ko, IPC_STAT, &msq_ds);
	msq_ds.msg_qbytes = 1;
	Msgctl(msqid_ko, IPC_SET, &msq_ds);
	
	
	int red_z_proiz[NUM_ROW_Z];
	int broj_zahtjeva_proiz = 0;	
	
	int red_z_potros[NUM_ROW_Z];
	int broj_zahtjeva_potros = 0;
	struct msqid_ds msq_info;
	
	for(;;){
		Msgrcv(msqid, (struct msgbuf *)&buf, sizeof(buf) - sizeof(long), CENTRAL, 0);
		printf("Recieve message %c(%d)\n", buf.cmd.com, buf.cmd.i);
		
		Msgctl(msqid_ko, IPC_STAT, &msq_info);
		int broj_poruka = msq_info.msg_qnum;
		

		if(buf.cmd.com == 'Z'){
			if(buf.cmd.i <= 2){ //request producer
				addToRow(buf.cmd.i, red_z_proiz, &broj_zahtjeva_proiz);
				if(broj_poruka == 0){
					ccomand.com = 'O';
					ccomand.i = buf.cmd.i;
					
					buf.mtype = buf.cmd.i;
					buf.cmd = ccomand;
					Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
					
					printf("Sent Response(%ld)\n", buf.mtype);
				}
			} else { //request consumer
				addToRow(buf.cmd.i, red_z_potros, &broj_zahtjeva_potros);
				if(broj_poruka == 1){
					ccomand.com = 'O';
					ccomand.i = buf.cmd.i;
					
					buf.mtype = buf.cmd.i;
					buf.cmd = ccomand;
					Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
					
					printf("Sent Response(%ld)\n", buf.mtype);
				}
			}
		}else if(buf.cmd.com == 'I'){
			if(buf.cmd.i <= 2){ //request producer
				removeFromRow(buf.cmd.i, red_z_proiz, &broj_zahtjeva_proiz);
				if(broj_zahtjeva_potros > 0){
					ccomand.com = 'O';
					ccomand.i = red_z_potros[0];
					
					buf.mtype = red_z_potros[0];
					buf.cmd = ccomand;
					Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
					
					printf("Sent Response(%ld)\n", buf.mtype);
				}else{
					continue;
				}
			} else { //request consumer
				removeFromRow(buf.cmd.i, red_z_potros, &broj_zahtjeva_potros);
				if(broj_poruka == 0){
					if(broj_zahtjeva_proiz > 0){
						ccomand.com = 'O';
						ccomand.i = red_z_proiz[0];
						
						buf.mtype = red_z_proiz[0];
						buf.cmd = ccomand;
						Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
						
						printf("Sent Response(%ld)\n", buf.mtype);
					} else{
						continue;
					}
				}else{
					if(broj_zahtjeva_potros > 0){
						ccomand.com = 'O';
						ccomand.i = red_z_potros[0];
						
						buf.mtype = red_z_potros[0];
						buf.cmd = ccomand;
						Msgsnd(msqid, (struct msgbuf *)&buf, sizeof(buf.cmd), 0);
						
						printf("Sent Response(%ld)\n", buf.mtype);
					} else{
						continue;
					}
				}
			}
		}
	}
	
	Msgctl(msqid, IPC_RMID, NULL);
	Msgctl(msqid_ko, IPC_RMID, NULL);
	return 0;
}

