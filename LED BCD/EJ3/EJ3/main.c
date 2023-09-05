/*
 * Entregable1-EJ3.c
 *
 * Created: 28/03/2023 19:37:07
 * Author : User
 */ 

#include <avr/io.h>
#define F_CPU 16000000UL // Defino la frecuencia de oscilador en 16MHz
#include <util/delay.h>

const float DELAY = 2.5;
const uint8_t BCDTABLE[10] = {0b00111111, 0b00000110, 0b01011011, 0b01001111, 0b01100110, 0b01101101, 0b01111101, 0b00000111, 0b01111111, 0b01101111};
	
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
	
    while (1) 
    {		
		separarDigitos(&contador, &digito1, &digito2, &digito3, &digito4);
		contador++; //Como tenemos 4 delay de 2,5 ms. La suma nos da 10ms. Como es la unica seccion en donde se utiliza el delay() esta variable contador se incrementara cada 10 ms como pide el enunciado.
		showDisplays(digito1, digito2, digito3, digito4);
	} 
		
}

