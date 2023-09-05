#include "timers.h"
#include <avr/interrupt.h>
#include "MEF.h"

#define PIN_RED PORTB5
#define PIN_GREEN PORTB2
#define PIN_BLUE PORTB1


#define PWM_PERIOD 255
static uint8_t PWM_DELTA = 0; // es 210
#define PWM_OFF PORTB |= (1<<PORTB5)
#define PWM_ON PORTB &=~ (1<<PORTB5)


uint8_t brightness = 0;
static uint8_t flag_mef=0;
static uint8_t flag_pwm=0;


void TIMER0_INIT(){
	
	DDRD |= (1<<PORTD6);					
	TCCR0A |= (1<<COM0A0) | (1<<WGM01);		// CTC Toggle on compare Match
	TCCR0B |= (1<<CS00); //preescaler div 1
	OCR0A = 63;							//Con un preescalador de 1 y oscilacion de 16Mhz esta realizara una interrupcion cada 8 us
	TIMSK0 |= (1<<OCIE0A); 
	// Habilitar las interrupciones globales
	sei();
}

void TIMER1_INIT(){
	TCCR1A |= (1<<WGM10);	
	TCCR1B |= (1<<WGM12);	//Fast PWM 8-bit
	TCCR1A |= (1<<COM1A1); //| (1<<COM1A0);				//Set OC1A/OC1B on compare match, clear OC1A/OC1B at BOTTOM (inverting mode)
	TCCR1A |= (1<<COM1B1); //| (1<<COM1B0);				//Set OC1A/OC1B on compare match, clear OC1A/OC1B at BOTTOM (inverting mode)
	TCCR1B |= (1<<CS01) | (1<<CS00);	//Preescaler div 1024, 61 hz de Frecuencia
		
	//Pin 0C1B como salida (PB2) verde
	//Si o si tengo que usar el registro B ya que la salida de este es por el PINB2 y es como requiere el enunciado
	DDRB |= (1<<PIN_GREEN);
		
	//Pin 0C1A como salida (PB1) Azul
	DDRB |= (1<<PIN_BLUE);
		
	//Ciclo de trabajo
		OCR1A=255;
		OCR1B=255;
}


//PWM por software que controla el color rojo (salida por PB5)
void PWM_soft_update(){
	static uint16_t PWM_position=0;
	if(++PWM_position >= PWM_PERIOD){
		PWM_position = 0;
		PWM_OFF;
		} else {
		if (PWM_position < PWM_DELTA){
			PWM_ON;
			} else {
			PWM_OFF;
		}
	}
}


void set_ocr1a(uint8_t value){
	OCR1A=value;
}

void set_ocr1b(uint8_t value){
	OCR1B=value;
}

void set_pwm_soft_value(uint8_t value){
	PWM_DELTA = value;
}

uint8_t get_pwm_soft_value(){
	return PWM_DELTA;
}

void Dispatch_Tasks(uint16_t adc1){
	if (flag_mef){
		MEF_Update(adc1);
		flag_mef=0;
	}
	if(flag_pwm){
		PWM_soft_update();
		flag_pwm=0;
	}
}


ISR (TIMER0_COMPA_vect){ //8us
	static uint16_t call_count = 0;
	if(++call_count == 1000){		//8 ms
		flag_mef=1;
		call_count=0;
	}
	flag_pwm=1;

}