#include "LED.h"
#include "TIMERS.h"

const uint8_t RED_BRIGHT = 250;		//Multiplos de 10
const uint8_t BLUE_BRIGHT = 0;	//Multiplos de 5
const uint8_t GREEN_BRIGHT = 0;	//Multiplos de 5


static uint16_t call_count=0;
static uint8_t auxA=255;
static uint8_t auxB=255;

void LED_Update(uint8_t direction){
	static call_count_led=0;
	static uint8_t aux;
	static uint8_t pwm_value=0;
	if (direction == 1){
		if(OCR1A > BLUE_BRIGHT){
			OCR1A-=5;
		}
		if(OCR1B > GREEN_BRIGHT){
			OCR1B-=5;
		}
		if (get_pwm_soft_value() < RED_BRIGHT)
		{
			pwm_value = get_pwm_soft_value();
			set_pwm_soft_value(pwm_value + 5);
		}

	} else {
			if(OCR1A < 255){
				OCR1A+=5;
			}
			if(OCR1B < 255){
				OCR1B+=5;
			}
			if (get_pwm_soft_value() > 1)
			{
				pwm_value = get_pwm_soft_value();
				set_pwm_soft_value(pwm_value - 5);
			}
				

	}
}
