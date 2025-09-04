// QUANDO É QUE SAI DO ESTADO READY ?? 


//readyToRunning(lines,programs); -> ESSA FUNÇÃO SÓ É ATIVADA QUANDO UM PROCESSO PASSA DE RUNNING PARA BLOCKED !!



/*                     POSSIVEL SITUAÇÃO, TODOS OS PROCESSOS BLOCKED E A SURGE UM PROCESSO NEW QUE DEVERIA IR DIRETO PARA RUNNING 
                       PENSAR EM ALTERAR O NOME DA VARIAVEL PROGRAMCOUNTER , NÃO FAZ SENTIDO ....                                  
                                                        
                                                         */



// pid = num_processo + 1 


#include <stdio.h>  
#include <stdlib.h> 


#define MAX_INSTRUCTIONS 8 
#define MAX_PROGRAMS 8

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

// QUEUE FUNCTIONS
typedef struct 
{
    int processPid; // process_num = processPid - 1
    int processArrivalTime; 

}cpuState;

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


// QUEUE GLOBAL VARIABLES 

      queue newFifo;
      queue readyFifo;
      queue blockedFifo ; 

// CPU SATE GLOBAL VARIBLE
     
       cpuState cpu ; 

// PROCESS STRUCT 

struct process {
    
    int pid;
    int state;
    int arrival_time;
    int Nextcpu_time;
    int io_time;
    int remaining_cpu_time;
    int remaining_io_time;
    int Nextblocked_time;
    int programCounter ; 
    
};

struct process processes[MAX_PROGRAMS];

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
            printf("  NEW       \t" );
            break;
        
         case 1 :
            printf("  READY       \t" );
            break;
        
         case 2 :
            printf("  RUNNING       \t" );
            break;
         
         case 3 :
            printf("  BLOCKED       \t" );
            break;
        
         case 4 :
            printf("  EXIT       \t" );
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
    int i = q->head;
        while (i != q->tail) 
        {
            if(value == q->buffer[i])
            {
                return 1 ;
            }


            i = (i + 1) % q->size;
        }

                return 0 ; 

}

void init_runningProcess (cpuState *q)
{
    q->processArrivalTime = -1 ; 
    q->processPid = -1 ;
}
// STATE HANDLER FUNCTIONS


struct process createProcess(int line, int**programs) {

            struct process processes ; 

            processes.pid = num_processes + 1;
            processes.state = NEW;
            processes.arrival_time = current_time ;
            processes.remaining_cpu_time = processes.Nextcpu_time = programs[line][1];
            processes.remaining_io_time = processes.io_time = programs[line][2];
            processes.Nextblocked_time = programs[line][2];
            processes.programCounter = 0 ;
            

           return processes ; 
        
};

void checkIfNew(int lines, int**programs)
{
     for(int i = 0; i<lines; i++)  
        {
            if(current_time == programs[i][0]){
                
                processes[num_processes] = createProcess(i,programs);
                enqueue(&newFifo,processes[num_processes].pid);                                                               
                num_processes++;
            }
        }
}

void newToRunning (int lines, int**programs)
{
                if(( ( current_time - ( processes[(newFifo.buffer[newFifo.head] - 1 )].arrival_time ) ) == 1 )) 
                {
                    
                
                cpu.processPid =  processes[(newFifo.buffer[newFifo.head] - 1 )].pid ;
                cpu.processArrivalTime = current_time ; 
                processes[(newFifo.buffer[newFifo.head] - 1 )].state = RUNNING ; 
                processes[(newFifo.buffer[newFifo.head] - 1 )].programCounter ++ ;
                processes[(newFifo.buffer[newFifo.head] - 1 )].Nextcpu_time = programs[(newFifo.buffer[newFifo.head] - 1 )][processes[(newFifo.buffer[newFifo.head] - 1 )].programCounter + 2 ] ; 
                dequeue(&newFifo);

                }
            
}

void NewtoReady(int lines, int**programs)
{
        int aux = 0 ;
        
        
          if(is_empty(&readyFifo)) 
          {
                if(cpu.processPid == -1  ) // RUNNING PID = -1 
                {  
                    newToRunning(lines, programs);

                }  
          } 

           
        for(int i = 0; i<num_processes; i++)  
        {
            
            if( (( current_time - processes[i].arrival_time ) == 1  ) && (isInsideQueue(&newFifo,processes[i].pid) )  ) // se o processo esta no array .
            {   
                processes[i].state = READY ; 
                enqueue(&readyFifo,processes[i].pid);
                dequeue(&newFifo);
 
            }
        }


}

void readyToRunning(int lines, int**programs) 
{   
     
     // ATUALIZAMOS O PC DO PROGRAMA EM EXECUÇÃO 
     
    processes[(cpu.processPid - 1)].Nextcpu_time = programs[(cpu.processPid - 1)][processes[(cpu.processPid - 1)].programCounter + 2] ; 
    processes[(cpu.processPid - 1)].programCounter ++ ; 
    processes[(cpu.processPid - 1 )].remaining_cpu_time = processes[(cpu.processPid - 1 )].Nextcpu_time ;
   
     

    // SETAR O CPU PARA EXECUTAR PRIMEIRO PROCESSO DA FILA RUNNING ...  

    cpu.processPid =  processes[(readyFifo.buffer[readyFifo.head] - 1 )].pid ;
    cpu.processArrivalTime = current_time ; 
    processes[(readyFifo.buffer[readyFifo.head] - 1 )].state = RUNNING ; 
    dequeue(&readyFifo);
   
}

void runningToBlocked(int lines , int**programs)
{
        if(processes[( cpu.processPid-1 )].remaining_cpu_time == 0 )
        {
            processes[(cpu.processPid - 1 )].state = BLOCKED ; 
            enqueue(&blockedFifo,processes[cpu.processPid - 1].pid);

            readyToRunning(lines,programs); // ESSA FUNÇÃO SÓ É ATIVADA QUANDO UM PROCESSO PASSA DE RUNNING PARA BLOCKED !!

        }


}

void blockedToReady(int lines, int**programas, queue * q )
{
    if(!is_empty(&blockedFifo))
    {
        int i = blockedFifo.head;
       
        while (i != q->tail) {

            if(processes[(blockedFifo.buffer[i] - 1 )].remaining_io_time == 0 )
            {
                enqueue(&readyFifo,processes[(blockedFifo.buffer[i] - 1 )].pid); // ADICIONAR PROCESSO NA FILA BLOCKED
                
                
                printf("pc : %d \n\n",processes[blockedFifo.buffer[i] - 1 ].programCounter);
                processes[blockedFifo.buffer[i] - 1 ].Nextblocked_time = programas[processes[blockedFifo.buffer[i] - 1 ].pid - 1][processes[blockedFifo.buffer[i] - 1 ].programCounter + 2 ];
                printf(" Nextblockedtime : %d \n\n", processes[blockedFifo.buffer[i] - 1 ].Nextblocked_time);
                processes[ blockedFifo.buffer[i] - 1 ].state = READY ;
                processes[ blockedFifo.buffer[i] - 1 ].programCounter ++ ;

                
                // mudar de estado...
                
                // set the next remaning io time ....

                dequeue(&blockedFifo);

            }   

            i = (i + 1) % q->size ;
        }
    }
}

void stateHandler(int lines, int**programs)
{
     
    checkIfNew(lines,programs);
    NewtoReady(lines,programs);
    runningToBlocked(lines,programs);
    blockedToReady(lines,programs, &blockedFifo);

     // TIRAR UM PROCESSO DE BLOCKED PARA FILA READY .... 
     // VARIOS PROCESSOS PODEM IR DE READY PARA RUNNING 
    


}

void cpuTimeHandler ()
{   

    if( (cpu.processPid != -1) && ( cpu.processArrivalTime != -1) )
    {
        processes[( cpu.processPid - 1 )].remaining_cpu_time-- ; 
    }

}

void blockedTimeHandler (queue * q)
{
    if(!is_empty(&blockedFifo)) 
    {
        int i = blockedFifo.head;
       
        while (i != q->tail) {

            processes[(blockedFifo.buffer[i] - 1 )].remaining_io_time -- ;           
            i = (i + 1) % q->size ;
        }//percorrer a blocked fifo a decrementar o timer 
    }
}

int main () 
{

    int lines = 0 ;
    int columns = 0 ;
    int **program;
    int aux = 0; 

    // QUEUES INICIALIZATION

     
    init_queue(&newFifo);
    init_runningProcess(&cpu);
    init_queue(&readyFifo);
    init_queue(&blockedFifo);
  

    
    // array com pseudo-instruções 

    int programs[1][8] = {
        {0, 3, 3, 5, 6, 4, 0, 0 },
       
    };

    
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


    printf("\n");
    printf("CURRENT TIME \t | PROC1 \t | PROC2 \t | PROC3 \t | PROC4 \t | PROC5 \t | PROC6 \t | PROC7 \t | PROC8 \t | \n");
    printf("\n");


    //Run OS
    while(aux<10) // lebrar de alterar o valor -> aux<3 para 1 .... 
    {

        stateHandler(lines,program);
        cpuTimeHandler();
        blockedTimeHandler(&blockedFifo); // DEPOIS DE COLOCARMOS O PROCESSO NA FILA BLOCKED VAMOS DECREMENTAR A VARIAVEL IO_TIME_REMAINING... 
        printState(processes); 
      
        aux++;
        current_time++;
        
    }


    printf("\n\n  process next cpu time p0 : %d  next cpu time p1  : %d  " , processes[0].Nextcpu_time, processes[1].Nextcpu_time);
    printf("\n\n");
    printf("\n\n");

  return 0 ; 
    
};

