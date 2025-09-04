back(e([], A), A, N, N).
back(E, Sol, NIn, NOut) :-
    sucessor(E, E1),
    ve_restricoes(E1),
    N1 is NIn + 1,
    back(E1, Sol, N1, NOut).

sucessor(e([v(N,D,V)|R],E),e(R,[v(N,D,V)|E])):- member(V,D).

p_count :-
    estado_inicial(E0),
    back(E0, Sol, 0, Total),
    mostra_estado(Sol),
    write('Nós visitados: '), write(Total), nl.