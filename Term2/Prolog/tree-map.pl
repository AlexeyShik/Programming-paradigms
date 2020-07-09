%Review
split([], _, [], [], []) :- !.
split(node(K, _, _, TL, TR), K, TL, node(K, _, _, [], []), TR) :- !.
split(node(K, V, Y, TL, TR), X, L, M, R) :- (X < K, split(TL, X, L, M, NTR), R = node(K, V, Y, NTR, TR) ; 	X > K, split(TR, X, NTL, M, R), L = node(K, V, Y, TL, NTL)).

merge([], TR, TR) :- !.
merge(TL, [], TL) :- !.
merge(L, R, Tree) :- L = node(KL, VL, YL, LL, RL), R = node(KR, VR, YR, LR, RR), 
		(YL > YR, merge(RL, R, T), Tree = node(KL, VL, YL, LL, T) ; YL =< YR, merge(L, LR, T), Tree = node(KR, VR, YR, T, RR)).

map_remove(Tree, K, NTree) :- split(Tree, K, TL, _, TR), merge(TL, TR, NTree).

map_put(Tree, K, V, NTree) :- rand_int(1000000000, Y), split(Tree, K, TL, _, TR), merge(TL, node(K, V, Y, [], []), T), merge(T, TR, NTree).
		
map_get(node(K, V, _, _, _), K, V) :- !.
map_get(node(K, _, _, L, R), X, Q) :- X < K, map_get(L, X, Q) ; X > K, map_get(R, X, Q).

map_build([], []) :- !.
map_build([(K, V) | T], R) :- map_build(T, Tree), map_put(Tree, K, V, R).

%modif
map_min(node(K, _, _, [], _), K) :- !.
map_min(node(_, _, _, TL, _), K) :- map_min(TL, K), !.
map_ceilingKey(Tree, K, CK) :- split(Tree, K, TL, TM, TR), (map_min(TM, CK), ! ; map_min(TR, CK)).