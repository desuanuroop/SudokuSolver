all: SudokuSolver.java
	javac SudokuSolver.java
	javah -jni SudokuSolver
	gcc Sudoku.c -I/usr/lib/jvm/default-java/include -o libSudokuSolver.so -shared -rdynamic -fPIC -lc -std=c99
	java -Djava.library.path=. SudokuSolver -lang=java output.txt inputs.txt