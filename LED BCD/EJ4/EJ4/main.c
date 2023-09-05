/*
 * Entregable1-EJ4.c
 *
 * Created: 28/03/2023 19:37:07
 * Author : User
 */ 

#include <avr/io.h>
#define F_CPU 16000000UL // Defino la frecuencia de oscilador en 16MHz
#include <util/delay.h>

const float DELAY = 2.5;
const uint8_t BCDTABLE[10] = {0b00111111, 0b00000110, 0b01011011, 0b01001111, 0b01100110, 0b01101101, 0b01111101, 0b00000111, 0b01111111, 0b01101111};



void Timer_Init(void){
	
	TCCR0A &= ~(1<<WGM00);
	TCCR0A &= ~(1<<WGM01);
	TCCR0B &= ~(1<<WGM02);
	TCNT0 = 0;
	
	TCCR0B = (1<<CS01); // 2 MHz
	TIMSK0 = (1<<TOIE0) //Interrupcion OVF
}


ISR(TIMER0_OVF_vect){ // Periodo de interrupcion -> Tisr = 256/2Mhz = 128us
	SEOS_Dispatch_Tasks();
}


void SEOS_Dispatch_Sch(){
	
}

void SEOS_Dispatch_Tasks(void){
	volatile unsigned char FLAG_X = 0;
	
	static unsigned char contX=19;
	
	if (++contX==20) //2,5ms
	{
		FLAG_X = 1;
		contX=0;
	}
	 
}



//Anti rebote
uint8_t debounce(uint8_t pin){
	if(!(PIND & (1<<pin))){
		_delay_ms(20);
		while(!(PIND & (1<<pin)));
		_delay_ms(20);
		return 1;
	}
	return 0;
}


// Obtener centesima de segundo, segundo, decima y centesima del contador;
void separarDigitos(uint16_t* contador, uint8_t* digito1, uint8_t* digito2, uint8_t* digito3, uint8_t* digito4) {
	uint16_t aux = 0;
	aux = *contador;
	if(aux != 0){
		*digito1 = aux % 10;
		aux = aux / 10;
	}
	if(aux !=0){
		*digito2 = aux % 10;
		aux = aux / 10;
	}
	if(aux != 0){
		*digito3 = aux % 10;
		aux = aux / 10;
	}
	if(aux != 0){
		*digito4 = aux % 10;
	}
}


// Por cada DELAY ms, muestro el valor que le corresponde a cada digito en el dispositivo de 7 segmentos.
void showDisplays(int digito1, int digito2, int digito3, int digito4){
	PORTB = BCDTABLE[digito1];
	PORTC = 0b00000001;
	_delay_ms(DELAY);
	PORTB = BCDTABLE[digito2];
	PORTC = 0b00000010;
	_delay_ms(DELAY);
	PORTB = BCDTABLE[digito3];
	PORTC = 0b00000100;
	_delay_ms(DELAY);
	PORTB = BCDTABLE[digito4];
	PORTC = 0b00001000;
	_delay_ms(DELAY);
	PORTB = BCDTABLE[digito1];
	PORTC = 0b00000001;
}


int main(void)
{
	uint16_t contador = 0;
	uint8_t digito1 = 0;
	uint8_t digito2 = 0;
	uint8_t digito3 = 0;
	uint8_t digito4 = 0;
	uint8_t start = 0;

	
	DDRB = 0xFF; // Seteamos todos los puertos de B como Output
	DDRC = 0x0F; // Seteamos los primero 4 puertos de C como Output
	// Los primeros 4 puertos los utilizamos para definir que segmento mostramos en ese instante de tiempo
	
	// Para los pulsadores de STOP, START y RESET.
	DDRD = 0x00;
	
    while (1) 
    {
		// Chequear el RESET.
		// Si el PIND0 esta en 1 (y todos los demas en 0)
		if (debounce(PIND0)) {
			contador = 0;
			digito1=0;
			digito2=0;
			digito3=0;
			digito4=0;
			
		}
		// START
		if (debounce(PIND1)) {
			start = 1;
		}
		// STOP
		if (debounce(PIND2)) {
			start = 0;	
		}
		if (start) {
			separarDigitos(&contador, &digito1, &digito2, &digito3, &digito4);
			contador++;
		}
		showDisplays(digito1, digito2, digito3, digito4);
	}
		
}

