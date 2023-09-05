

#define F_CPU 16000000UL
#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include "timers.H"
#include "MEF.h"
#include "ADC.h"

#define PWM_START DDRB |= (1<<PORTB5)

#define PIN_RED PORTB5
#define PIN_GREEN PORTB2
#define PIN_BLUE PORTB1



int main(void)
{
	
	MEF_Init();
	ADC_Init();
	TIMER0_INIT();
	TIMER1_INIT();
	PWM_START;
	ADC_Enable();
	static uint16_t result;
	ADC_Start_Conversion(); //ADCSRA |= (1<<ADSC);
	
    while (1) 
    { 
		
		while((ADCSRA&(1<<ADIF))==0); //wait for end of conversion
		ADCSRA |= (1<<ADIF);
		result = ADC_GetResult(); 
		Dispatch_Tasks(result);
		
		ADC_Start_Conversion(); //ADCSRA |= (1<<ADSC);
		
		

		
    }
	
	

}


