#include <stdio.h>
#include <avr/pgmspace.h>
#include <string.h>
#include "interface.h"
#include "UART.h"
 #include "common.h"
#include "RingtonePlayer.h"
static char *caracter;
extern char *rtttl_library[];

const char message1[] PROGMEM = "PLAY: reproduce la cancion seleccionada \r\n";
const char message2[] PROGMEM = "STOP: detiene la reproduccion del sonido en curso \r\n";
const char message3[] PROGMEM = "NUM: numero de cancion a seleccionar de la lista [1 a N] \r\n";
const char message4[] PROGMEM = "RESET: reinicia el sistema al estado inicial \r\n";
const char message5[] PROGMEM = "Comando no valido \r\n";
const char message6[] PROGMEM = "* Bienvenido * \r\n";


static char buffer[100];
// Mensaje para cuando se realiza la verificación de los comandos recibidos.
void invalidCommandMessage() {
	strcpy(buffer, message5);
	UART_Send_String_To_Transmit(buffer);
}


static void imprimirCanciones(){
	
	UART_Send_String_To_Transmit("1- The Simpsons");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("2- MissionImp");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("3- Batman");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("4- Pinkpanther");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("5- Adamsfamily");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("6- Argentina");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("7- Indiana");
	UART_Send_String_To_Transmit("\r\n");
	UART_Send_String_To_Transmit("\r\n");
	
}




// Mensaje de bienvenida (por única vez), indicando cuantas canciones tiene almacenado
// y una lista completa enumerada con el título (almacenar en memoria tipo ROM).
void welcomeMessage() {
	strcpy(buffer, "**************\r\n");
	UART_Send_String_To_Transmit(buffer);
	strcpy(buffer,message6);
	UART_Send_String_To_Transmit(buffer);
	strcpy(buffer, "**************\r\n");
	UART_Send_String_To_Transmit(buffer);
	UART_Send_String_To_Transmit("\r\n");
	imprimirCanciones();
}

void optionsMessage() {
	strcpy(buffer, message1);
	UART_Send_String_To_Transmit(buffer);
	strcpy(buffer, message2);
	UART_Send_String_To_Transmit(buffer);
	strcpy(buffer, message3);
	UART_Send_String_To_Transmit(buffer);
	strcpy(buffer, message4);
	UART_Send_String_To_Transmit(buffer);
	
}

/*
// Funcion auxiliar para imprimir en la pantalla UART
void UARTMessage(char* message) {
	char* receivedString = UART_Send_String_To_Transmit();
	
	if (UART_Get_Flag()) {
		UART_Send_String_To_Transmit(message);
	}
}
*/