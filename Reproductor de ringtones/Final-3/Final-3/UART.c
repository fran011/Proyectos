 #include "serialPort.h"
 #include "common.h"
 #include "UART.h"
 #define BUFFER_LENGTH 50 // 100 en decimal 
 
volatile char RX_String_Buffer[BUFFER_LENGTH];
volatile char TX_String_Buffer[BUFFER_LENGTH];
static uint8_t flagD = 0;
static uint8_t flagTX = 0;
static uint8_t flagMEF=0;
volatile uint8_t writeIndex=0;
volatile uint8_t readIndex=0;
volatile char RX_Buffer=0;
static uint8_t lengthCadena;

 /*
 void UART_Write_Char_To_Buffer (const char character){
	 if(writeIndex < BUFFER_LENGTH){
		 cadena[writeIndex] = character;
		 writeIndex++;
	 } else {
		 //No hay espacio
	 }
	 
 }
 
 void UART_Write_String_To_Buffer(const char* string_pointer){
	 index=0;
	 while(string_pointer[index] != '\0'){
		 UART_Write_Char_To_Buffer(string_pointer[index]);
		 index++;
	 }
 }
 */
 
//Avisa si se recibio un string por comando
uint8_t UART_Get_Flag(){
	return flagMEF;
}

void UART_Set_flag(uint8_t value){
	flagMEF = value;
}

//String recibido de la linea de comando
char* UART_Get_String_From_Buffer(){
	return RX_String_Buffer;	
}

void UART_Send_Char_To_Transmit(char caracter){
	
}

void UART_Send_Digit_To_Transmit(uint8_t number){
	lengthCadena = 1;
	TX_String_Buffer[0] = '0' + number;
	SerialPort_TX_Interrupt_Enable();
}

//Manda mensaje a imprimir a la linea de comando
 
void UART_Send_String_To_Transmit(char* string_pointer){
	while((flagTX));
	flagTX = 1;
	lengthCadena = strlen(string_pointer);
	strcpy(TX_String_Buffer, string_pointer);
	SerialPort_TX_Interrupt_Enable();
}

void UART_Build_CorrectSyntax_String(char* string_pointer){ // Le agrego el '\r' y'\n' para que se vea correctamente en la linea de comando
	lengthCadena = strlen(string_pointer); //Longitud de la cadena
	string_pointer[lengthCadena] = '\r' ;
	string_pointer[++lengthCadena] = '\n';
}

void UART_Update(){
	
	if(flagD){ // recepción NO Bloqueante
		strcpy(TX_String_Buffer, RX_String_Buffer);
		UART_Build_CorrectSyntax_String(TX_String_Buffer);
		flagTX = 1;
		SerialPort_TX_Interrupt_Enable();
		while (flagTX);
		flagMEF = 1 ;
		flagD=0;
	}
}

 //Recepcion
ISR(USART_RX_vect){
	RX_Buffer = UDR0; //la lectura del UDR borra flag RXC
	RX_String_Buffer[writeIndex] = RX_Buffer;
	if(RX_Buffer == '\r' || (writeIndex >= BUFFER_LENGTH - 2)){ //Reservo 2 lugares del buffer para el '\r' y'\n'
		flagD = 1;
		RX_String_Buffer[writeIndex] = '\0';
		writeIndex = 0;
		} else {
		writeIndex++;
	}
}

 //Transmision
ISR(USART_UDRE_vect){
	UDR0 = TX_String_Buffer[readIndex];
	readIndex++;
	if(lengthCadena == 0){
		readIndex = 0;
		lengthCadena = 0;
		flagTX = 0;
		UCSR0B &=~(1<<UDRIE0); //Deshabilito interrupcion
		} else{
		lengthCadena--;
	}
	
	
}
