
#include "common.h"		
#include "serialPort.h"
#include <string.h>
#include "UART.h"
#include "MEF.h"
#include "TIMERS.h"  
#include "interface.h"
  #define F_CPU 16000000UL
 #define BR9600 (0x67)	// 0x67=103 configura BAUDRATE=9600@16MHz


 int main(void)
 {
	 
	SerialPort_Init(BR9600); 		// Inicializo formato 8N1 y BAUDRATE = 9600bps
	SerialPort_TX_Enable();			// Activo el Transmisor del Puerto Serie
	SerialPort_RX_Enable();			// Activo el Receptor del Puerto Serie
	SerialPort_RX_Interrupt_Enable();	// Activo Interrupción de recepcion.
	 
	sei();								// Activo la mascara global de interrupciones (Bit I del SREG en 1)
	
	TIMER1_Init();
	TIMER0_Init();
	MEF_init();

	while(1)
	{
		UART_Update(); 
		MEF_update();
		
	}
	 return 0;
 }

