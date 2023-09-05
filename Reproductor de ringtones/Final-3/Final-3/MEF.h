#ifndef MEF
#define MEF

 #include "common.h"	

typedef enum {INICIO, STOP, PLAYING, NO_VALID, LOAD } states;

void MEF_init();
void MEF_update();

#endif