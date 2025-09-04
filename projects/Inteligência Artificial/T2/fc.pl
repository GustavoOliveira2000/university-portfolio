% ---------------------------
% Predicado principal com forward checking
% ---------------------------

resolve_forward_checking :-
    estado_inicial(E0),
    fc(E0, Sol, 0, NFinal),
    mostra_estado(Sol),
    write('Nós visitados: '), write(NFinal), nl.

% ---------------------------
% Algoritmo FC com contador
% ---------------------------

fc(e([], Afectadas), Afectadas, N, N).
fc(e([v(ID,D,_)|Resto], Afectadas), Sol, NIn, NOut) :-
    member(V, D),
    N1 is NIn + 1,
    verifica_grupo_kakuro_parcial([v(ID,_,V)|Afectadas]),  % restrições locais
    atualiza_dominios(Resto, [v(ID,D,V)|Afectadas], RAtualizado),
    RAtualizado \= fail,
    fc(e(RAtualizado, [v(ID,D,V)|Afectadas]), Sol, N1, NOut).

% ---------------------------
% Atualiza domínios com FC (manual, compatível com GNU Prolog)
% ---------------------------

atualiza_dominios([], _, []).
atualiza_dominios([v(ID,Dom,_)|R], Afectadas, [v(ID,NovoDom,_)|R2]) :-
    filtra_dominios(Dom, ID, Afectadas, NovoDom),
    NovoDom \= [],
    atualiza_dominios(R, Afectadas, R2).
atualiza_dominios(_, _, fail).

% ---------------------------
% Filtra valores consistentes (versão manual de include/3)
% ---------------------------

filtra_dominios([], _, _, []).
filtra_dominios([V|R], ID, Afectadas, [V|R2]) :-
    valor_consistente(ID, Afectadas, V),
    filtra_dominios(R, ID, Afectadas, R2).
filtra_dominios([V|R], ID, Afectadas, R2) :-
    \+ valor_consistente(ID, Afectadas, V),
    filtra_dominios(R, ID, Afectadas, R2).

% ---------------------------
% Um valor é consistente com restrições parciais
% ---------------------------

valor_consistente(ID, Afectadas, V) :-
    verifica_grupo_kakuro_parcial([v(ID,_,V)|Afectadas]).

% ---------------------------
% Restrição parcial (grupo válido mesmo que incompleto)
% ---------------------------

verifica_grupo_kakuro_parcial(Atribuidas) :-
    forall(grupo(_, Casas, Soma),
        verifica_grupo_kakuro(Casas, Soma, Atribuidas)
    ).