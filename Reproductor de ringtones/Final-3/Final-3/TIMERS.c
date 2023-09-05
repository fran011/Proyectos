 #include "common.h"
 #include <avr/io.h>
 #include <util/delay.h>
 #include <avr/interrupt.h>

void TIMER0_Init(){
		// Configuro una interrupcion cada 1 mseg
		OCR0A = 248;			//124 para 8MHz y 248 para 16MHz
		TCCR0A = (1<<WGM01);   // Modo CTC, clock interno, prescalador 64
		TCCR0B = (1<<CS01)|(1<<CS00);   // Modo CTC, clock interno, prescalador 64
		TIMSK0 = (1<<OCIE0A);   // Habilito Timer 0 en modo de interrupcion de comparacion
}

void TIMER1_Init(){
		TCCR1A|=(1<<COM1A0);// Configuro Timer1 para clk con prescaler P=1, modo CTC y salida por pin
		TCCR1B|=(1<<WGM12)|(1<<CS10);
		DDRB|=(1<<PINB1); // El PIN1 del PORTB sera el pin de salida
}