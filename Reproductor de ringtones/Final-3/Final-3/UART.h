#ifndef UART
#define UART

 #include "common.h"	
 
void UART_Update(void);void UART_Build_CorrectSyntax_String(char *);
char* UART_Get_String_From_Buffer(void);
void UART_Send_String_To_Transmit(char* );
void UART_Send_Char_To_Transmit(char);
void UART_Send_Digit_To_Transmit(uint8_t);
void UART_Set_flag(uint8_t );
uint8_t UART_Get_Flag(void);

#endif