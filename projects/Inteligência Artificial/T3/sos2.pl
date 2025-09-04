 estado_inicial(e([v,v,v,v,v,v,v,v,v],s,p1,p2)).

terminal(e(Tabuleiro, _, _, _)) :-
    \+ member(v, Tabuleiro).

valor(e(_, _, P1, P2), 1)  :- P1 > P2.  % Vitória de j1 (MAX)
valor(e(_, _, P1, P2), -1) :- P2 > P1.  % Vitória de j2 (MIN)
valor(e(_, _, P1, P2), 0)  :- P1 =:= P2. % Empate