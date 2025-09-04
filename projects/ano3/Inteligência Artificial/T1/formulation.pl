% Definição do estado inicial
estado_inicial((pos(1,1), objetos([]))).

% Estado final.
estado_final((pos(2,7), objetos([a, b]))).

% Definição das adjacências em um grid 4x4 (exemplo de labirinto)
adjacente((X,Y), (X1,Y)) :- X1 is X + 1, X1 =< 7. % Movimento para a direita (limite X = 4)
adjacente((X,Y), (X1,Y)) :- X1 is X - 1, X1 >= 1. % Movimento para a esquerda (limite X > 1)
adjacente((X,Y), (X,Y1)) :- Y1 is Y + 1, Y1 =< 7. % Movimento para cima (limite Y = 4)
adjacente((X,Y), (X,Y1)) :- Y1 is Y - 1, Y1 >= 1. % Movimento para baixo (limite Y > 1)

% Obstáculos (casas bloqueadas)
bloqueado((2,2)).
bloqueado((2,6)).
bloqueado((4,1)).
bloqueado((4,2)).
bloqueado((4,3)).
bloqueado((4,4)).
bloqueado((4,7)).
bloqueado((3,5)).
bloqueado((3,6)).
bloqueado((7,6)).


% Objetos disponíveis em posições específicas
objeto((2,4), a).
objeto((2,6), b).

% Regras para movimentação (evita obstáculos)
mover((pos(X,Y), Objetos), (pos(X1,Y)), Objetos) :- 
    adjacente((X,Y), (X1,Y)), 
    \+ bloqueado((X1,Y)).

mover((pos(X,Y), Objetos), (pos(X,Y1)), Objetos) :- 
    adjacente((X,Y), (X,Y1)), 
    \+ bloqueado((X,Y1)).

% Regras para pegar objetos (somente na ordem correta)
pegar((pos(X,Y), objetos([])), pegar_a, (pos(X,Y), objetos([a]))) :- objeto((X,Y), a).
pegar((pos(X,Y), objetos([a])), pegar_b, (pos(X,Y), objetos([a, b]))) :- objeto((X,Y), b).

% Expansão de estados possíveis
expande(Estado, NovosEstados) :-
    findall(NovoEstado, (mover(Estado, _, NovoEstado); pegar(Estado, _, NovoEstado)), NovosEstados).

% Busca em Largura (BFS) para encontrar uma solução
resolver_bfs(Solucao) :-
    estado_inicial(Inicio),
    bfs([[Inicio]], Solucao).

bfs([[Estado|Caminho]|_], [Estado|Caminho]) :- estado_final(Estado).
bfs([Caminho|Outros], Solucao) :-
    expande(Caminho, Expandidos),
    append(Outros, Expandidos, NovaLista),
    bfs(NovaLista, Solucao).
    

% Busca A* com heurística da distância Manhattan
resolver_aestrela(Solucao) :-
    estado_inicial(Inicio),
    aestrela([[(Inicio, 0, H)]], Solucao).

aestrela([[Estado|Caminho]|_], [Estado|Caminho]) :- estado_final(Estado).
aestrela([Caminho|Outros], Solucao) :-
    expande(Caminho, Expandidos),
    calcular_f(Expandidos, Caminho, ExpandidosF),
    append(Outros, ExpandidosF, NovaLista),
    sort(2, @=<, NovaLista, Ordenado),
    aestrela(Ordenado, Solucao).

% Cálculo da heurística Manhattan
heuristica((pos(X,Y)), H) :-
    estado_final((pos(Xf,Yf), _)),
    H is abs(X - Xf) + abs(Y - Yf).

calcular_f([], _, []).
calcular_f([(Estado, G, _)|Outros], Caminho, [(Estado, G, F)|ExpandidosF]) :-
    heuristica(Estado, H),
    F is G + H,
    calcular_f(Outros, Caminho, ExpandidosF).


%pesquisa_a([],_):- !,fail.
pesquisa_a([no(E,Pai,Op,C,HC,P)|_],no(E,Pai,Op,C,HC,P)):- estado_final(E),inc.

pesquisa_a([E|R],Sol):- inc, asserta(fechado(E)), expande(E,Lseg), esc(E),
  insere_ord(Lseg,R,Resto),
  length(Resto,N), actmax(N),
                              pesquisa_a(Resto,Sol).

insere_ord([],L,L).
insere_ord([A|L],L1,L2):- insereE_ord(A,L1,L3), insere_ord(L,L3,L2).

expande(no(E,Pai,Op,C,HC,P),L):- findall(no(En,no(E,Pai,Op,C,HC,P),Opn,Cnn,HCnn,P1),
					 (op(E,Opn,En,Cn), \+ fechado(no(En,_,_,_,_,_)),
					  P1 is P+1, Cnn is Cn+C, h(En,H), HCnn is Cnn+H), L).

  actmax(N):- maxNL(N1), N1 >= N,!.
  actmax(N):- retract(maxNL(_N1)), asserta(maxNL(N)).
  
 esc(A):- write(A), nl.