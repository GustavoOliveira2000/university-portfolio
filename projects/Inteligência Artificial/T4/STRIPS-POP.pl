% Estado Inicial
estado_inicial([
    pos(R,1,1),
    mao_vazia,
    peca(1,1,1), peca(2,2,1), peca(3,3,1),
    peca(4,1,2), vazio(2,2), peca(5,3,2),
    peca(6,1,3), peca(7,2,3), peca(8,3,3)
]).

% Estado Final
estado_final([
    peca(1,1,1), peca(2,2,1), peca(3,3,1),
    peca(4,1,2), peca(5,2,2), peca(8,3,2),
    peca(6,1,3), peca(7,2,3),
    pos(R,3,3), mao_vazia
]).

% Passos do plano
passo(a1, mover(1,1, 2,1)).
passo(a2, mover(2,1, 3,1)).
passo(a3, mover(3,1, 3,2)).
passo(a4, agarra(5, 3,2)).
passo(a5, mover(3,2, 2,2)).
passo(a6, larga(5, 2,2)).
passo(a7, mover(2,2, 3,2)).
passo(a8, mover(3,2, 3,3)).
passo(a9, agarra(8, 3,3)).
passo(a10, mover(3,3, 3,2)).
passo(a11, larga(8, 3,2)).
passo(a12, mover(3,2, 3,3)).

% Links causais
link(s0, pos(R,1,1), a1).
link(a1, pos(R,2,1), a2).
link(a2, pos(R,3,1), a3).
link(a3, pos(R,3,2), a4).
link(s0, peca(5,3,2), a4).
link(s0, mao_vazia, a4).
link(a4, na_mao(5), a5).
link(a5, pos(R,2,2), a6).
link(a6, peca(5,2,2), sg).
link(a6, mao_vazia, a9).
link(a7, pos(R,3,2), a8).
link(a8, pos(R,3,3), a9).
link(s0, peca(8,3,3), a9).
link(a9, na_mao(8), a10).
link(a10, pos(R,3,2), a11).
link(a11, peca(8,3,2), sg).
link(a11, mao_vazia, sg).
link(a12, pos(R,3,3), sg).

% Ordem parcial dos passos
ordem(s0, a1).
ordem(a1, a2).
ordem(a2, a3).
ordem(a3, a4).
ordem(a4, a5).
ordem(a5, a6).
ordem(a6, a7).
ordem(a7, a8).
ordem(a8, a9).
ordem(a9, a10).
ordem(a10, a11).
ordem(a11, a12).
ordem(a12, sg).

% Operador: mover(X1, Y1, X2, Y2)
precondicoes(mover(X1, Y1, X2, Y2), [
    adjacente(X1, Y1, X2, Y2),
    pos(R, X1, Y1)
]).

addlist(mover(X1, Y1, X2, Y2), [
    pos(R, X2, Y2)
]).

dellist(mover(X1, Y1, X2, Y2), [
    pos(R, X1, Y1)
]).

% Operador: agarra(N, X, Y)
precondicoes(agarra(N, X, Y), [
    mao_vazia,
    peca(N, X, Y),
    pos(R, X, Y)
]).

addlist(agarra(N, X, Y), [
    na_mao(N),
    vazio(X, Y)
]).

dellist(agarra(N, X, Y), [
    mao_vazia,
    peca(N, X, Y)
]).

% Operador: larga(N, X, Y)
precondicoes(larga(N, X, Y), [
    na_mao(N),
    vazio(X, Y),
    pos(R, X, Y)
]).

addlist(larga(N, X, Y), [
    mao_vazia,
    peca(N, X, Y)
]).

dellist(larga(N, X, Y), [
    na_mao(N),
    vazio(X, Y)
]).