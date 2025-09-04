% ---------------------------
% Estado inicial 
% ---------------------------

% exemplo 1 : 

% estado_inicial(e([

%     v(c(1), [1,2,3,4,5,6,7,8,9], _),
%     v(c(2), [1,2,3,4,5,6,7,8,9], _),
%     v(c(3), [1,2,3,4,5,6,7,8,9], _),
%     v(c(4), [1,2,3,4,5,6,7,8,9], _),
%     v(c(5), [1,2,3,4,5,6,7,8,9], _),
%     v(c(6), [1,2,3,4,5,6,7,8,9], _),
%     v(c(7), [1,2,3,4,5,6,7,8,9], _),
%     v(c(8), [1,2,3,4,5,6,7,8,9], _),
%     v(c(9), [1,2,3,4,5,6,7,8,9], _),
%     v(c(10), [1,2,3,4,5,6,7,8,9], _),
%     v(c(11), [1,2,3,4,5,6,7,8,9], _)
% ], 

% []

% )).


% exemplo 2 : 

estado_inicial(e([

    v(c(1), [1,2,3,4,5,6,7,8,9], _),
    v(c(2), [1,2,3,4,5,6,7,8,9], _),
    v(c(4), [1,2,3,4,5,6,7,8,9], _),
    v(c(5), [1,2,3,4,5,6,7,8,9], _),
    v(c(6), [1,2,3,4,5,6,7,8,9], _),
    v(c(8), [1,2,3,4,5,6,7,8,9], _),
    v(c(9), [1,2,3,4,5,6,7,8,9], _),
    v(c(10), [1,2,3,4,5,6,7,8,9], _),
    v(c(11), [1,2,3,4,5,6,7,8,9], _),
    v(c(12), [1,2,3,4,5,6,7,8,9], _),
    v(c(13), [1,2,3,4,5,6,7,8,9], _),
    v(c(16), [1,2,3,4,5,6,7,8,9], _),
    v(c(17), [1,2,3,4,5,6,7,8,9], _),
    v(c(19), [1,2,3,4,5,6,7,8,9], _)
], 
[
    v(c(3), [1,2,3,4,5,6,7,8,9], 4),
    v(c(7), [1,2,3,4,5,6,7,8,9], 4),
    v(c(14), [1,2,3,4,5,6,7,8,9], 2),
    v(c(15), [1,2,3,4,5,6,7,8,9], 5),
    v(c(18), [1,2,3,4,5,6,7,8,9], 8)
])).

% ---------------------------
% Grupos de soma (exemplo)
% ---------------------------

% exemplo 1 : 

% grupo(h1, [c(3), c(7), c(10)], 23).
% grupo(h2, [c(4), c(8), c(11)], 9).
% grupo(h3, [c(1), c(5), c(9)], 24).
% grupo(h4, [c(2), c(6)], 15).

% grupo(v1, [c(1), c(2)], 13).
% grupo(v2, [c(3), c(4), c(5), c(6)], 24).
% grupo(v3, [c(7), c(8), c(9)], 23).
% grupo(v4, [c(10), c(11)], 11).

% exemplo 2 : 

grupo(h1, [c(1), c(4)], 13).
grupo(h2, [c(13), c(17)], 11).
grupo(h3, [c(2), c(5), c(9), c(14), c(18)], 26).
grupo(h4, [c(3), c(6), c(10), c(15), c(19)], 28).
grupo(h5, [c(7), c(11), c(16)], 18).
grupo(h6, [c(8), c(12)], 3).

grupo(v1, [c(1), c(2), c(3)], 20).
grupo(v2, [c(4), c(5), c(6), c(7), c(8)], 23).
grupo(v3, [c(9), c(10), c(11), c(12)], 14).
grupo(v4, [c(13), c(14), c(15), c(16)], 23).
grupo(v5, [c(17), c(18), c(19)], 19 ).



% ---------------------------
% Restrição geral
% ---------------------------

ve_restricoes(e(_, Afectadas)) :-
    forall(grupo(_, Casas, Soma),
           verifica_grupo_kakuro(Casas, Soma, Afectadas)).

% ---------------------------
% Validação de cada grupo
% ---------------------------

verifica_grupo_kakuro(Casas, Soma, Atribuidas) :-
    % extrai apenas as casas deste grupo já atribuídas
    findall(V, (member(v(c(ID),_,V), Atribuidas), member(c(ID), Casas), nonvar(V)), Valores),
    all_different(Valores),
    sumlist(Valores, Parcial),
    Parcial =< Soma,
    ( length(Valores, L), length(Casas, L) -> Parcial = Soma ; true ).

% ---------------------------
% Auxiliares
% ---------------------------

all_different([]).
all_different([X|Xs]) :-
    \+ member(X, Xs),
    all_different(Xs).

sumlist([], 0).
sumlist([H|T], S) :-
    sumlist(T, ST),
    S is H + ST.

% ---------------------------
% Mostra valores atribuídos (ou não) a cada casa
% ---------------------------

mostra_estado(Atribuidas) :-
   % between(1, 11, ID),      % para casas c(1) até c(11) -> Exemplo 1 
    between(1, 19, ID),     % para casas c(1) até c(19) -> Exemplo 2 
    ( member(v(c(ID),_,V), Atribuidas) ->
        write(c(ID)), write(' = '), write(V), nl
    ; write(c(ID)), write(' = _'), nl
    ),
    fail.
mostra_estado(_).  % termina o backtracking