%Review, [4..6] times more, than benchmark
forj(J, I, N) :- J > N, !.
forj(J, I, N) :- assert(composite(J)), J1 is J + I, forj(J1, I, N).
fori(I, II, N) :- II > N, !.
fori(I, II, N) :- (composite(I); forj(II, I, N)), !, I1 is I + 2, II1 is I1 * I1, fori(I1, II1, N).
init(N) :- fori(3, 9, N).

composite(X) :- 0 is mod(X, 2), X > 2.
prime(X) :- not(composite(X)), X > 1.

find_prime(X, D, [X]) :- DD is D * D, X < DD, !.
find_prime(X, D, [D | T]) :- 0 is mod(X, D), !, X1 is div(X, D), find_prime(X1, D, T).
find_prime(X, D, V) :- not(0 is mod(X, D)), !, D1 is D + 1, find_prime(X, D1, V).

check(1, []), !.
check(X, [X]) :- prime(X), !.
check(X, [H | [T1 | T2]]) :- prime(H), H =< T1, !, check(X1, [T1 | T2]), X is X1 * H.

prime_divisors(1, []) :- !.
prime_divisors(X, [X]) :- prime(X), !.
prime_divisors(X, V) :- integer(X), X > 1, !, find_prime(X, 2, V).
prime_divisors(X, V) :- check(X, V), !.

%modif
to_kth(0, _, []) :- !.
to_kth(N, K, R) :- REM is mod(N, K), N1 is div(N, K), to_kth(N1, K, R1), append([REM], R1, R).

equals([], []) :- !.
equals([H1 | T1], [H2 | T2]) :- H1 is H2, equals(T1, T2).

prime_palindrome(N, K) :- prime(N), to_kth(N, K, N1), reverse(N1, RN1), equals(N1, RN1).
