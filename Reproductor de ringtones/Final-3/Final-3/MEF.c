#include "MEF.h"
#include "UART.h"
#include "interface.h"
#include "common.h"
  #define F_CPU 16000000UL
#include "RingtonePlayer.h"

states estado_act;
uint8_t flagInicio = 1;
char comando[50];
uint8_t song_select;

void MEF_init() {
	estado_act = INICIO;
}

static void handleInicio() {
	if (!(strcmp(comando, "1") && strcmp(comando, "2") && strcmp(comando, "3") && strcmp(comando, "4") &&
	strcmp(comando, "5") && strcmp(comando, "6"))) {
		if (estado_act == INICIO) {
			estado_act = LOAD;
			song_select = atoi(comando); // <------------ Convierte de string a entero
		}
		} else if (!(strcmp(comando, "PLAY"))) {
		if (estado_act == STOP || estado_act == LOAD) {
			estado_act = PLAYING;
		}
		} else if ((!( strcmp(comando, "STOP"))) && estado_act == PLAYING) {
		estado_act = STOP;
		} else if (!(strcmp(comando, "RESET"))) {
			flagInicio = 1;
			estado_act = INICIO;
		
		} else if ((!(strcmp(comando, "STOP"))) && estado_act == STOP) {
		// Nada
		} else if ((!(strcmp(comando, "STOP"))) && estado_act == INICIO) {
		// Nada
		} else if ((!(strcmp(comando, "STOP"))) && estado_act == LOAD) {
		// Nada
		} else {
		invalidCommandMessage();
	}
}

void MEF_update() {
	switch (estado_act) {
		case INICIO:
		if (flagInicio) {
			welcomeMessage();
			optionsMessage();
			flagInicio = 0;
		}
		if (UART_Get_Flag() == 1) { // Si se recibio algo (comando) flag es 1
			UART_Set_flag(0);
			strcpy(comando, UART_Get_String_From_Buffer());
			handleInicio();
		}
		break;
		case LOAD:
		if (UART_Get_Flag() == 1) {
			UART_Set_flag(0);
			strcpy(comando, UART_Get_String_From_Buffer());
			handleInicio();
		}
		play_LoadSong(song_select - 1);
		break;
		case NO_VALID:
		// TO-DO
		break;
		case PLAYING:
		if (UART_Get_Flag() == 1) {
			UART_Set_flag(0);
			strcpy(comando, UART_Get_String_From_Buffer());
			handleInicio();
		}
		if ( !(play_note()) )
		{
			estado_act = INICIO;
		}
		break;
		case STOP:
		if (UART_Get_Flag() == 1) {
			UART_Set_flag(0);
			strcpy(comando, UART_Get_String_From_Buffer());
			handleInicio();
		}
		break;
		default:
		// TO-DO
		break;
	}
}