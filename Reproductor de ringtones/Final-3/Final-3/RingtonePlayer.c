
 #include <avr/io.h>
 #include <util/delay.h>
 #include <avr/interrupt.h>
 #include <string.h>
 #include "TIMERS.h"
 #include "common.h"
 #include "RingtonePlayer.h"
    
 
  // Coleccion de musica RTTL
  char *rtttl_library[]=
  {
	"TheSimpsons:d=4,o=5,b=160:c.6,e6,f#6,8a6,g.6,e6,c6,8a,8f#,8f#,8f#,2g,8p,8p,8f#,8f#,8f#,8g,a#.,8c6,8c6,8c6,c6"

	/*"MissionImp:d=4,o=6,b=150:16d5,16d#5,16d5,16d#5,16d5,16d#5,16d5,16d5,16d#5,16e5,16f5,16f#5,16g5,8g5,4p,8g5,4p,8a#5,8p,8c6,8p,8g5,4p,8g5,4p,8f5,8p,8p,8g5,4p,4p,8a#5,8p,8c6,8p,8g5,4p,4p,8f5,8p,8f#5,8p,8a#5,8g5,1d5",

	"Batman:d=8,o=5,b=160:16a,16g#,16g,16f#,16f,16f#,16g,16g#,4a.,p,d,d,c#,c#,c,c,c#,c#,d,d,c#,c#,c,c,c#,c#,d,d,c#,c#,c,c,c#,c#,g6,p,4g6",

	"Pinkpanther:d=16,o=5,b=160:8d#,8e,2p,8f#,8g,2p,8d#,8e,p,8f#,8g,p,8c6,8b,p,8d#,8e,p,8b,2a#,2p,a,g,e,d,2e",

	"Adamsfamily:d=4,o=5,b=160:8c,f,8a,f,8c,b4,2g,8f,e,8g,e,8e4,a4,2f,8c,f,8a,f,8c,b4,2g,8f,e,8c,d,8e,1f,8c,8d,8e,8f,1p,8d,8e,8f#,8g,1p,8d,8e,8f#,8g,p,8d,8e,8f#,8g,p,8c,8d,8e,8f",

	"Argentina:d=4,o=5,b=70:8e.4,8e4,8e4,8e.4,8f4,8g4,8a4,g4,8p,8g4,8a4,8a4,8g4,c,g4,8f4,e.4,8p,8e4,8f4,8g4,8d4,d4,8d4,8e4,8f4,c4,16p,8c4,8d4,8c4,8e4,g4,16p,8g4,8g4,8a4,c,16p",

	"Indiana:d=4,o=5,b=250:e,8p,8f,8g,8p,1c6,8p.,d,8p,8e,1f,p.,g,8p,8a,8b,8p,1f6,p,a,8p,8b,2c6,2d6,2e6,e,8p,8f,8g,8p,1c6,p,d6,8p,8e6,1f.6,g,8p,8g,e.6,8p,d6,8p,8g,e.6,8p,d6,8p,8g,f.6,8p,e6,8p,8d6,2c6"  */
  };

  // La siguiente matriz almacena las frecuencias de las notas musicales
  const unsigned int note[4][12] =
  {   // C    C#    D     D#    E     F     F#    G     G#    A     A#    B
	  {262,  277,  294,  311,  330,  349,  370,  392,  415,  440,  466,  494}, // 4ta octava
	  {523,  554,  587,  622,  659,  698,  740,  784,  830,  880,  932,  988}, // 5ta octava
	  {1047, 1109, 1175, 1244, 1319, 1397, 1480, 1568, 1660, 1760, 1865, 1976}, // 6ta octava
	  {2093, 2218, 2349, 2489, 2637, 2794, 2960, 3136, 3320, 3520, 3728, 3951}  // 7ma octava
  };

 
unsigned int duration_timer;
volatile unsigned int sound_playing=0;
unsigned char duration, octave;
unsigned int tempo;

static unsigned char temp_duration, temp_octave, current_note, dot_flag; // <------------------------------
static unsigned int calc_duration;  // <------------------------------
static char *song; // <------------------------------

// Control de la duracion del sonido
ISR (TIMER0_COMPA_vect) // ISR para la interrupcion de comparacion del Timer 0
{
	if (duration_timer) duration_timer--; // Decremento el timer si > 0
	else                                  // si timer es = 0
	{
		TCCR1A=0;	// Desactivo el timer 1
		sound_playing = 0;                  // Borro el flag para avisar que no hay una nota sonando
	}
}


// Saco el sonido por el PIN5 del PORTD: freq en Hz, dur en ms
void sound(unsigned int freq, unsigned int dur)
{
	while (sound_playing);      // Si hay algun sonido presente, espero a que termine
	
	duration_timer = dur;       // Seteo el tiempo de duracion
	
	// Activo la salida y configuro el timer para que genere la señal de la frecuencia apropiada
	TCCR1A|=(1<<COM1A0);
	
	// Actualizo el valor de OCR1A para que produzca la nota adecuada
	OCR1A=(8000000/(freq))-1;
	
	sound_playing = 1;          // Activo el flag para avisar que hay una nota sonando
}

//Cargo la cancion seleccionada
void play_LoadSong(uint8_t selected_song){ // <------------------------------
	song = rtttl_library[selected_song]; // <------------------------------
	duration = 4;                 // Duracion estandar = 4/4 = 1 beat
	tempo = 63;                   // Tempo estandar = 63 bpm
	octave = 6;                   // Octava estandar = 6th
	while (*song != ':') song++;  // Busca el primer ':'
	song++;                       // Saltea el primer ':'
	while (*song!=':')            // Repite hasta encontrar ':'
	{
		if (*song == 'd')           // Entra si es el seteo de la duracion
		{
			duration = 0;             // Seteo la duracion en cero (temporalmente)
			song++;                   // Avanzo al proximo caracter
			while (*song == '=') song++;  // Salteo '='
			while (*song == ' ') song++;  // Salteo los espacios
			// Si el caracter es un numero, seteo la duracion
			if (*song>='0' && *song<='9') duration = *song - '0';
			song++;                   // Avanzo al proximo caracter
			// Me fijo si el caracter es un numero, ya que la duracion puede ser de dos digitos de largo
			if (*song>='0' && *song<='9')
			{ // Multiplico duracion por 10 y le agrego el valor del caracter
				duration = duration*10 + (*song - '0');
				song++;                 // Avanzo al proximo caracter
			}
			while (*song == ',') song++;  // Salteo ','
		}
		
		if (*song == 'o')           // Entra si es el seteo de la octava
		{
			octave = 0;               // Seteo la octava en cero (temporalmente)
			song++;                   // Avanzo al proximo caracter
			while (*song == '=') song++;  // Salteo '='
			while (*song == ' ') song++;  // Salteo los espacios
			// Si el caracter es un numero, seteo la octava
			if (*song>='0' && *song<='9') octave = *song - '0';
			song++;                   // Avanzo al proximo caracter
			while (*song == ',') song++;  // Salteo ','
		}
		if (*song == 'b')           // Entra si es el seteo del tempo (beats por minuto)
		{
			tempo = 0;                // Seteo el tempo en cero (temporalmente)
			song++;                   // Avanzo al proximo caracter
			while (*song == '=') song++;  // Salteo '='
			while (*song == ' ') song++;  // Salteo los espacios
			// Ahora leo el seteo del tempo (puede tener 3 digitos de largo)
			if (*song>='0' && *song<='9') tempo = *song - '0';
			song++;                   // Avanzo al proximo caracter
			if (*song>='0' && *song<='9')
			{
				tempo = tempo*10 + (*song - '0'); // El tempo tiene dos digitos
				song++;                 // Avanzo al proximo caracter
				if (*song>='0' && *song<='9')
				{
					tempo = tempo*10 + (*song - '0'); // El tempo tiene tres digitos
					song++;               // Avanzo al proximo caracter
				}
			}
			while (*song == ',') song++;  // Salteo ','
		}
		while (*song == ',') song++;    // Salteo ','
	}
	song++;                       // Avanzo al proximo caracter
}

//Toco la nota que corresponde (No bloqueante)
uint8_t play_note(void) // <------------------------------
{
	// read the musical notes
	//while (*song)                 // Repito hasta que el caracter sea null (No repito ya que de esta manera es bloqueante)
	if((*song !=  '\0')){  // <------------------------------
		current_note = 255;         // Nota por defecto = pausa
		temp_octave = octave;       // Seteo la octava a la por defecto de la cancion
		temp_duration = duration;   // Seteo la duracion a la por defecto de la cancion
		dot_flag = 0;               // Borro el flag de deteccion de punto
		// Busco un prefijo de duracion
		if (*song>='0' && *song<='9')
		{
			temp_duration = *song - '0';
			song++;
			if (*song>='0' && *song<='9')
			{
				temp_duration = temp_duration*10 + (*song - '0');
				song++;
			}
		}
		// Busco una nota
		switch (*song)
		{
			case 'c': current_note = 0; break;    // C (do)
			case 'd': current_note = 2; break;    // D (re)
			case 'e': current_note = 4; break;    // E (mi)
			case 'f': current_note = 5; break;    // F (fa)
			case 'g': current_note = 7; break;    // G (sol)
			case 'a': current_note = 9; break;    // A (la)
			case 'b': current_note = 11; break;   // B (si)
			case 'p': current_note = 255; break;  // pausa
		}
		song++;                     // Avanzo al pr�ximo caracter
		// Busco un '#' siguiendo la nota
		if (*song=='#')
		{
			current_note++;   // Incremento la nota (A->A#, C->C#, D->D#, F->F#, G->G#)
			song++;                   // Avanzo al pr�ximo caracter
		}

		// Busco '.' (extiende la duracion de la nota un 50%)
		if (*song=='.')
		{
			dot_flag = 1;             // Si se encuentra '.', seteo el flag
			song++;                   // Avanzo al proximo caracter
		}
		// Busco un sufijo de una octava
		if (*song>='0' && *song<='9')
		{
			temp_octave = *song - '0';// Seteo la octava en consecuencia
			song++;                   // Avanzo al proximo caracter
		}
		if (*song=='.') // Un punto puede ser encontrado incluso despues de una octava
		{
			dot_flag = 1;             // Si se encuentra '.', seteo el flag
			song++;                   // Avanzo al proximo caracter
		}
		while (*song == ',') song++;    // Salteo ','
		// Calculo la duracion de la nota
		calc_duration = (60000/tempo)/(temp_duration);
		calc_duration *= 4;         // La nota completa tiene cuatro beats
		// Chequeo si el flag de punto esta activado, de ser asi, extiendo la duracion en un 50%
		if (dot_flag) calc_duration = (calc_duration*3)/2;
		// Si la nota actual NO es una pausa, reproduzco la nota usando la funcion sound
		if (current_note<255) sound(note[temp_octave-4][current_note],calc_duration);
		else
		{ // Si la nota actual es una pausa (255), espero dicha cantidad de tiempo
			duration_timer = calc_duration;
			sound_playing = 1;
		}
		return 1;     // <------------------------------
	} else {
		return 0; // <------------------------------
	}
}




/*
int main(void)
{
	// Declaraciones de variables
	
	unsigned char song_sel;
	
	//Habilito la mascara de interrupciones
	
	sei();
	
	while(1)
	{
		//Main
		for (song_sel=0;song_sel<11;song_sel++)
		{
			play_song(rtttl_library[song_sel]); // Reproduzco la canci�n
			// Espero dos segundos antes de comenzar la siguiente canci�n
			duration_timer = 2000;
			sound_playing = 1;
			while (sound_playing);
		}
	}

	return 0; // Nunca va a alcanzar esta parte
}
*/