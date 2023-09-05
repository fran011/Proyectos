#include "MEF.h"
#include "timers.h"
#include "LED.h"

states estado_act;
static uint16_t call_count_mef=0;
static uint16_t call_count_pwm=0;
static uint8_t pwm_value=0;
uint8_t flicker_period=0;
static uint8_t luz;

void MEF_Init(){
	OCR1A=0;
	OCR1B=0;
	PORTB |= (1<<PORTB1);
	PORTB |= (1<<PORTB2);
	
	estado_act=APAGADO;	
}

void MEF_Update(uint16_t adc1){		//Cada 8ms
	if(adc1 > 980){
		luz=1;
	} else {
		luz=0;
	}
	switch (estado_act){
		case APAGADO:
		OCR1A=255;
		OCR1B=255;
		if (luz == 1){
			if(++call_count_mef == 1){			//0.5 seg apagado
				call_count_mef=0;
				estado_act=TRANSICION1;
			}
		} else {
			if(++call_count_mef == 375){			//1 seg apagado
				call_count_mef=0;
				estado_act=TRANSICION1;
			}
			
		break;
		case TRANSICION1:
				
				LED_Update(1);
			
				if(++call_count_mef == 63 ){  //500 ms
					call_count_mef = 0;
					estado_act = MAX;
				}
				
		break;
		
		case TRANSICION2:
			
				LED_Update(0);
				if(++call_count_mef == 63 ){ //500 ms
					call_count_mef = 0;
					estado_act = APAGADO;
				}
		break;
		
		case MAX:
		if (luz == 1){
			if(++call_count_mef == 125){
				call_count_mef = 0;
				estado_act = TRANSICION2;
			}
		} else {
			if(++call_count_mef == 125){
				call_count_mef = 0;
				estado_act = TRANSICION2;
			}
		}
			
	
		break;
	}

}
		}