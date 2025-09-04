
#include <stdlib.h> 
#include <stdio.h>
#include <string.h>


#define LINES 3
#define ROWS 10

// Proceses states 

#define NEW 0
#define READY 1
#define RUNNING 2
#define BLOCKED 3
#define EXIT 4

int num_processes = 0 ; 
int current_time = 0 ;
char*state;

#define MAX_SIZE 8


typedef struct 
{
    int processPid; // process_num = processPid - 1
    int state ;

}cpuState;

/*          QUEUES FUINCTIONS           */

typedef struct {
    int *buffer;
    int head;
    int tail;
    int size;
} queue;

void init_queue(queue *q) {
    q->buffer = (int *) malloc(MAX_SIZE * sizeof(int));
    q->head = 0;
    q->tail = 0;
    q->size = MAX_SIZE+1;
}

int is_empty(queue *q) {
    return q->head == q->tail;
}

int is_full(queue *q) {
    return (q->tail + 1) % q->size == q->head;
}

void enqueue(queue *q, int value) {
    if (is_full(q)) {
        printf("Queue is full\n");
        return;
    }
    q->buffer[q->tail] = value;
    q->tail = (q->tail + 1) % q->size;
}

void dequeue(queue *q) {
    
    if (is_empty(q)) {
        printf("Queue is empty\n");
        
    }
    int value = q->buffer[q->head];
    q->head = (q->head + 1) % q->size;
    
}

void display_queue(queue *q) {
    if (q->size == 0) {
        printf("Queue is empty!\n");
    } else {
        int i = q->head;
        while (i != q->tail) {
            printf("%d ", q->buffer[i]);
            i = (i + 1) % q->size;
        }
        printf("\n");
    }
}


// DECLARAÇÃO DAS QUEUE
    
      queue readyFifo;
      queue blockedFifo ; 


struct process {
    
    int pid; // process id 
    int state; // estado do processo (blocked, ready , new , exit ) 
    int event ; // variavel de controlo que controla o inicio de um acontecimente , seja um processo ficar blocked , running ou new etc... 
    int programCounter ; 
    
};

// DECLARAÇÃO DE STRUCTS 

 struct process processes[ROWS];
 cpuState cpu ;

// DISPLAY FUNCTIONSMN

void printState (struct process processes[8] )
{
    int aux = 0 ; 

    printf("  %d       \t", current_time );
    while (aux<num_processes) 
    {
        switch (processes[aux].state)
        {
        case 0 :
            printf("  NEW    \t" );
            break;
        
         case 1 :
            printf("  READY    \t" );
            break;
        
         case 2 :
            printf("  RUNNING    \t" );
            break;
         
         case 3 :
            printf("  BLOCKED    \t" );
            break;
        
         case 4 :
            printf("  EXIT    \t" );
            break;
        
        default:
            break;
        }
        
        
        aux++; 
    }

    printf("\n\n");

}

//UTILITY FUNCTIUONS

int isInsideQueue (queue * q, int value)
{
    int i = q->head ; 

    while (i!=q->tail)
    {
        if(q->buffer[i] == value )
        return 1 ;
        
       i = (i + 1) % q->size;
    }
    
    return 0 ; 
}

void init_CPU ()
{
   cpu.processPid = -1 ;
   cpu.state = -1 ;

}

struct process creatProcess()
{

       struct process processes ; 

            processes.pid = num_processes ;
            processes.state = NEW;
            processes.event = current_time ;
            processes.programCounter = 0 ;

           return processes ;
    
}

void readyState(queue * q, int**programs) 
{

    // ready state to running state 

    int i = 0 ;

    if(cpu.state == -1 )
    {
     
            int Processpid = q->buffer[q->head];

            if( ((current_time - processes[Processpid].event)>= 1) && (processes[Processpid].state == READY ) ) 
            {
                processes[Processpid].programCounter++;
                processes[Processpid].event = current_time;
                processes[Processpid].state = RUNNING ;    
                cpu.processPid = Processpid ;
                cpu.state = 1 ;
                dequeue(&readyFifo);   

            }
        
    }

}

void newProcess(int columns,int**programs, int lines)
{   
    int i = 0 ;

    while(i < lines )
    {
         if(programs[i][0] == current_time)
         {
            
            processes[num_processes] = creatProcess(); 
            num_processes++;

         }

         i++;
    }

}

void newTimeout(int columns,int**programs, int lines) // tira o processo do estado new para o estado ready
{

    int i = 0 ;

    while(i < num_processes )
    {
        
        if( (processes[i].event == current_time - 1)  && (processes[i].state == NEW) )
        {
            
            if(cpu.state == -1 && is_empty(&readyFifo) == 1 )
            {
                  processes[i].programCounter++;
                  processes[i].state = RUNNING ; 
                  processes[i].event = current_time ;
                  cpu.processPid = processes[i].pid ; 
                  cpu.state = 1 ; 
                if(programs[i][processes[i].programCounter + 1] == -1) 
                {
                    // PROCESS PID 
                }


            } else 
            {
                  enqueue(&readyFifo,processes[i].pid);
                  processes[i].state = READY ; 
                  processes[i].event = current_time;
            }
           

        }

         i++;
    }
    
}

void newState(int columns,int**programs, int lines)
{
    newProcess(columns,programs,lines);
    newTimeout(columns,programs,lines);
}

void unBlock (int**programs)
{

   if( (cpu.state == 1) && processes[cpu.processPid].event == current_time - 1 )
   {

    int processPid = programs[cpu.processPid][ processes[cpu.processPid].programCounter + 1 ] ; 
    
    if( ( processPid != -1 ) && (processPid<=num_processes) && processes[processPid].state == BLOCKED )
    {

        if( isInsideQueue(&readyFifo,processPid) == 0  ) 
        {
               processes[processPid].event = current_time ;
               processes[processPid].state = READY ;
               //printf("\nUNBLOCKED\n");
               enqueue(&readyFifo,processPid); 
        }
     
    }

   }
   
   
   

}

void runningTimeout(int**programs)
{
      if(cpu.state != -1 )
    {
            if(  (( programs[cpu.processPid] [ processes[cpu.processPid].programCounter ] ) == ( current_time - processes[cpu.processPid].event )) && ( processes[cpu.processPid].state == RUNNING ) )
            {
                if( processes[cpu.processPid].programCounter == 7 )
                {
                   
                   // pode ser daqui o problema 
                    
                    processes[cpu.processPid].event = -1 ;
                    processes[cpu.processPid].state = EXIT ;
                    cpu.processPid = -1 ;
                    cpu.state = -1 ;
          
                } else 
                {
                
                    
                    processes[cpu.processPid].programCounter++;
                    processes[cpu.processPid].event = current_time ;
                    processes[cpu.processPid].state = BLOCKED ; 
                    processes[cpu.processPid].programCounter++ ;
                    cpu.processPid = -1 ;
                    cpu.state = -1 ;

                }
            }
    }
}

void runningState(int**programs)
{
  runningTimeout(programs);
}

void blockedTimeout(int**programs)
{
    int i = 0 ;

    while(i<num_processes)
    {
            if( (processes[i].state == BLOCKED) && ( programs[processes[i].pid][processes[i].programCounter] == ( current_time - processes[i].event ) ))
            {
                processes[i].state = READY ; 
                processes[i].event = current_time ;
                enqueue(&readyFifo,processes[i].pid);

            }
    
            i++;
    }


}

void blockedState(int**programs) 
{
    blockedTimeout(programs);
}

void stateHandler(int columns,int**programs, int lines)
{
    newState(columns,programs,lines);
    runningState(programs);
    unBlock(programs); // vai ser chamada no instante event + 1 , se o programa estiver running 
    readyState(&readyFifo,programs);
    blockedState(programs);
    
}

int main () 
{

    int lines = 0 ;
    int columns = 0 ;
    int **program;
    int aux = 0; 

    // QUEUES INICIALIZATION

     
    init_queue(&readyFifo);
    init_queue(&blockedFifo);

    // inicializar o processador 


    init_CPU();
  

    
    // array com pseudo-instruções 

    int programs[LINES][ROWS] = {0};  // Initialize the array with zeros
    FILE *file = fopen("input.txt", "r");
    if (file == NULL) {
        printf("Failed to open the file.\n");
        return 1;
    }

    char line[100];  // Assuming a maximum line length of 100 characters

    // Skip the first line that contains "int programas[8][10] = {"
    fgets(line, sizeof(line), file);

    for (int i = 0; i < LINES; i++) {
        if (fgets(line, sizeof(line), file) == NULL) {
            printf("Invalid format in the file.\n");
            fclose(file);
            return 1;
        }

        char *token = strtok(line, "{,}\t\n ");  // Tokenize the line using delimiters {,} and whitespace
        for (int j = 0; j < ROWS; j++) {
            if (token == NULL) {
                printf("Invalid format in the file.\n");
                fclose(file);
                return 1;
            }
            programs[i][j] = atoi(token);  // Convert token to integer and store in the array
            token = strtok(NULL, "{,}\t\n ");
        }
    }
    fclose(file);



    
    // determinar o numero de linhas e coluna da matrix de pseudo-instruções do programa 

    lines = sizeof(programs) / sizeof(programs[0]);
    columns = sizeof(programs[0])/sizeof(programs[0][0]);
    

    // copiar array programa para um novo array

    program = malloc(lines*sizeof *program);

    for(int i = 0; i<lines; i++){
        program[i] = malloc(columns*sizeof*program[i]);
    }   

    for (int i=0; i<lines; i++){
        for (int j=0; j<columns; j++){
        program[i][j] = programs[i][j];
        }
    }


    // essas linhas acimas servem apenas para podermos passar um array para um função qualquer 


    printf("\n");
    printf("CURRENT TIME \t | PROC1 \t | PROC2 \t | PROC3 \t | PROC4 \t | PROC5 \t | PROC6 \t | PROC7 \t | PROC8 \t | \n");
    printf("\n");



    //Run OS

    while(aux<27) // lebrar de alterar o valor -> aux<3 para 1 .... 
    {

        stateHandler(columns,program,lines);
        printState(processes); 
      
        aux++;
        current_time++;
        
    }


   


    // printf(" cpu state = %d and cpu pid = %d \n\n " , cpu.state , cpu.processPid) ; 

  return 0 ; 
    
};

