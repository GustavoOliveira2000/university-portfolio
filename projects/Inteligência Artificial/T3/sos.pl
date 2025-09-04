  estado_inicial(e([s,s,s,v,v,v,s,v,v], s)).  % ou 'o', tanto faz
  
  inv(s, o).
  inv(o, s).

% Operador: aplica peça P na posição C e muda o jogador

  op1(e(L, J), (C, P), e(L1, J1)) :-
    member(P, [s, o]),          % permite jogar 's' ou 'o' livremente
    subs(v, P, L, L1, 1, C),    % coloca a peça P na posição C (se livre)
    inv(J, J1).                 % troca o jogador ativo

  subs(A,J, [A|R], [J|R],C,C).
  subs(A,J, [B|R], [B|S],N,C):- M is N+1, subs(A,J,R,S,M,C).

  terminal(e([S,O,S,_,_,_,_,_,_],_)):- S==s, O == o.
  terminal(e([_,_,_,S,O,S,_,_,_],_)):- S==s, O == o.
  terminal(e([_,_,_,_,_,_,S,O,S],_)):- S==s, O == o.

  terminal(e([S,_,_,O,_,_,S,_,_],_)):- S==s, O == o.
  terminal(e([_,S,_,_,O,_,_,S,_],_)):- S==s, O == o.
  terminal(e([_,_,S,_,_,O,_,_,S],_)):- S==s, O == o.

  terminal(e([S,_,_,_,O,_,_,_,S],_)):- S==s, O == o.
  terminal(e([_,_,S,_,O,_,S,_,_],_)):- S==s, O == o.

  terminal(e(L,_)):- \+ member(v,L).

  valor(E,V,P):-terminal(E),
  X is P mod 2,
   (X== 1,V=1;X==0,V= -1).


  valor(e(L,_),0,P):-  \+ member(v,L),!.

% Predicado principal: soma os valores heurísticos dos padrões
avalia(e(L, _), V) :-
    findall(V1, aval(L, V1), Vs),
    soma(Vs, V).

% Padrões a favor (potenciais SOS)
aval(L, 1) :- nth1(1,L,s), nth1(2,L,o), nth1(3,L,v).
aval(L, 1) :- nth1(1,L,s), nth1(3,L,o), nth1(2,L,v).
aval(L, 1) :- nth1(2,L,s), nth1(3,L,o), nth1(1,L,v).

aval(L, 1) :- nth1(1,L,s), nth1(5,L,o), nth1(9,L,v).  % diagonal
aval(L, 1) :- nth1(3,L,s), nth1(5,L,o), nth1(7,L,v).  % diagonal

% Padrões contra (ameaças do adversário)
aval(L, -1) :- nth1(1,L,o), nth1(2,L,s), nth1(3,L,v).
aval(L, -1) :- nth1(1,L,o), nth1(3,L,s), nth1(2,L,v).
aval(L, -1) :- nth1(2,L,o), nth1(3,L,s), nth1(1,L,v).

aval(L, -1) :- nth1(1,L,o), nth1(5,L,s), nth1(9,L,v).
aval(L, -1) :- nth1(3,L,o), nth1(5,L,s), nth1(7,L,v).

soma([], 0).
soma([X|Xs], R) :-
    soma(Xs, R1),
    R is X + R1.