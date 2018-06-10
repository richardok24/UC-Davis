/*******************************************/
/**    Your solution goes in this file    **/ 
/*******************************************/

/* Part 1 */

fc_course(C) :- course(C, _ , U), (U is 3; U is 4).

prereq_110(C) :- course(C, P, _ ), member(ecs110, P).

ecs140a_students(S) :- student(S, C), member(ecs140a, C).

/* list first, second, until the last instructor name in fact, test each name */
instructor_names(I) :- instructor(I, _ ), findI(I).
findI(I) :- instructor(I, ITC), student(john, STLC), member(STL, STLC), member(STL, ITC), !.

/* list first, second, until the last student name in fact, test each name */
students(S) :- student(S, _ ), findS(S).
findS(S) :- student(S, STLC), instructor(jim, ITCS), member(ITC, ITCS), member(ITC, STLC), !.

/*
need to treat the course as a list, because prereq of the course will be a list, then we need to find  
prereq course of course in that list. Then write the base case, which just cut, and back to pre-state.
Then in that find prereq in H and T, then combine them as a list. back to the pre-fact. 
*/
allprereq([], []) :- !.
allprereq([H|T], AP) :- course(H, P1, _ ), allprereq(P1, AP1), append(AP1, P1, AP2), allprereq(T, AP3), append(AP2, AP3, AP), !.
allprereq(C, AP) :- allprereq([C|[]], AP).

/* Ex
    P1=[A,B] assume all prereq of B only is C, C dont have prereq
    [A |[B]], find all prereq AP of A, P1 = [], AP1 = [], AP2 = []
     find all prereq AP of B,
     T = [B|[]]
     find prereq of head, B
     -> P1 = [C|[]] 
     -> find all prereq AP of C, repeat same thing as A, so AP2 = []
        find prereq of tail, tail = [], return, AP3 = [] , AP = [], return
        AP1 = [C]
        ... same thing 
*/

/* Part 2 */

/* as the code professor give about the length and structure similar as before hw. */
all_length([], 0).
all_length([H|T], L) :- atom(H), all_length(T, L1), L is L1 + 1, !.
all_length([H|T], L) :- all_length(H, L1), all_length(T, L2), L is L1 + L2, !.

/* help function to find how many a in list and how many b in list and compare them return yes or no. */
equal_a_b(L) :- help_equal_a_b(L, a, NoA), help_equal_a_b(L, b, NoB), NoA = NoB.
help_equal_a_b([], _, 0) :- !.
help_equal_a_b([H|T], X, N) :- help_equal_a_b(H, X, NH), help_equal_a_b(T, X, NT), N is NH + NT, !.
help_equal_a_b(X, X, 1) :- !.
help_equal_a_b(_, _, 0).

/* using the append utility to find preffix and suffix, then use them to build swap */
swap_prefix_suffix(Hit, L, S) :- help_swap_prefix_suffix(Pre, Hit, Suf, L), help_swap_prefix_suffix(Suf, Hit, Pre, S).
help_swap_prefix_suffix(A, B, C, D) :- append(A, E, D), append(B, C, E).

/* using the reverse utility shown in the dicussion slides, and append utility to return yes or no */
palin(A) :- help_palin(A, A).
help_palin([], []).
help_palin([H|T], X) :- help_palin(T, Y), append(Y, [H], X).
/*check after 1, if there is 2 good sequence*/
good([0]).
good([1|T]) :- append(A, B, T), good(A), good(B).

/* Part 3 */

/* state of the farmer, wolf, goat, and cabbage on the left or right bank */
state( _ , _ , _ , _ ).

/* indicate two oppisite banks of the river */
opposite(left, right).
opposite(right, left).

/* unsafe conditions: first is wolf with goat,2ed goat with cabbage */
unsafe(state(A, B, B, _ )) :- opposite(A, B).
unsafe(state(A, _ , B, B)) :- opposite(A, B).

/* safe: opposite of unsafe. Using not(negation) operator discussed in class */
safe(A) :- \+ unsafe(A).

/* take term for moving object X from bank A to bank B */
take(X, A, B) :- opposite(A, B).

/* arc(N, X, Y) is true if move N takes state X to state Y */
/* take wolf from bank A to bank B, check if it's safe */
arc(take(wolf, A, B), state(A, A, Goat, Cabbage), state(B, B, Goat, Cabbage)) :- opposite(A, B), safe(state(B, B, Goat, Cabbage)).
/* take goat from bank A to bank B, check if it's safe */
arc(take(goat, A, B), state(A, Wolf, A, Cabbage), state(B, Wolf, B, Cabbage)) :- opposite(A, B), safe(state(B, Wolf, B, Cabbage)).
/* take cabbage from bank A to bank B, check if it's safe */
arc(take(cabbage, A, B), state(A, Wolf, Goat, A), state(B, Wolf, Goat, B)) :- opposite(A, B), safe(state(B, Wolf, Goat, B)).
/* take nothing from bank A to bank B, check if it's safe */
arc(take(none, A, B), state(A, Wolf, Goat, Cabbage), state(B, Wolf, Goat, Cabbage)) :- opposite(A, B), safe(state(B, Wolf, Goat, Cabbage)).

/* Find path and then output */
go(A, B) :- help_go(A, B, []), !.
help_go(X, Z, P) :- E = arc(take( _ , _ , _ ), X, Y), \+ member(E, P), E, help_go(Y, Z, [E|P]). 
help_go(X, X, P) :- output(P), !.

output([]).
output([arc(take(X, A, B), _ , _ )|T]) :- output(T), print('take'), print('('), print(X), print(', '), print(A), print(', '), print(B), print(')'), nl.	

solve :- go(state(left, left, left, left), state(right, right, right, right)).