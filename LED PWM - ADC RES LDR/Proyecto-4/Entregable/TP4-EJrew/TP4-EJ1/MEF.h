#ifndef MEF
	#define MEF
	typedef enum {APAGADO,MAX,TRANSICION1,TRANSICION2} states;
	#include <avr/io.h>
	#include <util/delay.h>
	#include <avr/interrupt.h>
void MEF_Init(void);
void MEF_Update(uint16_t);

	
	
#endif