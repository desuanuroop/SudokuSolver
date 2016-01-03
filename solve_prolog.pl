:- use_rendering(table).
:- use_module(library(clpfd)).

solve_prolog(Y):-
    	X = [[_, 7, 2, _, _, 8, 10, _, _, 1, 3, _, 12, _, 9, 6],
            [_, 0, _, 3, _, 5, _, _, 9, _, 6, _, 1, _, 11, 15],
[10, _, 4, _, 13, _, 9, _, _, 8, _, 11, 0, _, 2, 7],
[_, 9, 6, _, 12, 1, _, 0, 7, 2, 10, _, 13, 3, _, 8],
[0, 11, _, 13, _, _, 1, 15, _, 3, _, 9, 6, _, _, 4],
[1, 14, _, 10, _, 11, _, 4, _, 15, _, 6, 9, 2, _, 0],
[2, 15, 9, _, 0, _, 6, 8, _, 7, _, 4, _, 1, 3, 5],
[_, _, 8, 4, _, _, _, 9, 1, _, 0, 13, 7, _, 15, _],
[4, 13, _, 7, 6, 10, _, _, 11, _, 9, 8, _, 15, _, 2],
[15, 1, _, _, _, 7, 13, _, _, _, 4, 10, _, 11, _, 9],
[3, 10, 11, _, 2, _, 0, _, 6, _, _, 7, _, 5, 4, _],
[6, 8, 14, _, _, _, 11, 5, 3, _, 2, _, 10, _, 7, 1],
[_, _, _, 8, 1, _, 7, _, _, _, _, 3, 15, _, _, _],
[7, 2, _, _, 5, _, 8, _, _, 0, _, 1, _, _, _, 3],
[_, 4, _, 6, _, 14, _, _, _, _, 13, 5, _, 7, 1, _],
[9, 3, _, _, 11, 4, _, 10, _, 6, 7, _, 5, _, _, 13]],

        length(X, 16), maplist(same_length(X), X),
        append(X, Vs), Vs ins 0x0..0xf, 
    	maplist(all_distinct, X),
        transpose(X, Columns),
        maplist(all_distinct, Columns),
        X = [A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P],
        blocks(A, B, C, D), blocks( E, F, G, H), blocks( I, J, K, L), blocks( M, N, O, P),
    	Y = X.

blocks([], [], [], []).
blocks([A,B,C,D|Bs1], [E,F,G,H|Bs2], [I,J,K,L|Bs3], [M,N,O,P|Bs4]) :-
        all_distinct([A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P]),
        blocks(Bs1, Bs2, Bs3, Bs4).