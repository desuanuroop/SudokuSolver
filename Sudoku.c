#include <stdio.h>
#include "SudokuSolver.h"
#include <jni.h>

char options[16] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

int solve(char *grid, int);
int isColumnValid(char *grid, int);
int isRowValid(char *grid, int);
int isBlockValid(char *grid, int,int);
int isValid(char *grid);
int solve_c(char *grid);

JNIEXPORT jobjectArray JNICALL Java_SudokuSolver_solvec(JNIEnv *env, jobject o,jstring javaip){ //JNI Declaration of native c function.
	jobjectArray ret;
	char *ip = (*env)->GetStringUTFChars(env, javaip, 0);
	char grid[256];
	for(int i=0;i<256;i++)
		grid[i] = *(ip++);

	if(solve_c(grid)){
		ret = (jobjectArray)(*env)->NewObjectArray(env, 1, (*env)->FindClass(env, "java/lang/String"), (*env)->NewStringUTF(env, ""));
		(*env)->SetObjectArrayElement(env, ret, 0, (*env)->NewStringUTF(env, grid));
	}
	return ret;

}
int solve_c(char *grid){
	return solve(grid, 0);
}

int solve(char *grid, int cell){
	while (cell < 256 && grid[cell] != '.')
		cell++;
	if(cell == 256)
		return 1;

	for(int i = 0; i < 16; i++)
	{
		grid[cell] = options[i];//Take an option from options array and place it in the cell.
		if(isColumnValid(grid, cell % 16)) //Now check the column of the cell and if true move forward.
			if(isRowValid(grid, cell / 16)) //Now check the row of the cell and if true goto BlockValid()
				if(isBlockValid(grid, cell % 16, cell / 16))// Check BlockValidity
					if(isValid(grid) && solve(grid, cell +1)) // if Entire grid at present stage is valid then call solve with next cell.
						return 1;//return 1
	}
	grid[cell] = '.';
	return 0;//return 0
}

int isColumnValid(char *grid, int col){
	int freqs[16] = {0};
	int freq_index=-1;

	for(int i = 0; i < 16; i++)	{    	 
	 char cell = grid[i*16 + col];
	 if(cell != '.'){
		 for(int j=0;j<16;j++){
			 if(cell == options[j]){//Count the number of times a particular value has occurred in the given col. If number times > 1 then col is false.
				 freq_index = j;break;}
		 }
		 if(++freqs[freq_index] > 1)
			 return 0;
		}
	}
	return 1;
}

int isRowValid(char *grid, int row){
	int freqs[16] = {0};
	int freq_index=-1;

	for(int i = 0; i < 16; i++){    	  
		char cell = grid[row * 16 + i];
			if(cell !='.'){
				for(int j=0;j<16;j++){
					if(cell == options[j]){//Count the number of times a particular value has occurred in the given row. If number times > 1 then row is false.
						freq_index = j;break;}
				}
		if(++freqs[freq_index] > 1)
			return 0;
		}
	}
	return 1;
}

int isBlockValid(char *grid, int col, int row){
	int freqs[16] = {0};
	int freq_index = -1;
	int x,y;

	if(col >= 0 && col<=3)
		x = 0;
	else if(col >=4 && col <=7)
		x = 4;
	else if(col >= 8 && col <=11)
		x = 8;
	else
		x=12;
		
	if(row >= 0 && row<=3)
		y = 0;
	else if(row >=4 && row <=7)
		y = 4;
	else if(row >= 8 && row <=11)
		y = 8;
	else
		y=12;

	for(int i = 0; i < 16; i++){
		int temp = (y + i / 4) * 16 + (x + i % 4);
		char cell = grid[temp];
		if(cell != '.'){
			for(int j=0;j<16;j++){
				if(cell == options[j]){//Count the number of times a particular value has occurred in the given block. If number times > 1 then block is false.
					freq_index = j;
					break;}
				}
			if(++freqs[freq_index] > 1)
				return 0;
		}
	}
	return 1;
}

int isValid(char *grid){
	for(int i = 0; i < 16; i++){
         if(!isRowValid(grid, i))//Check each and other row.
            return 0;
         if(!isColumnValid(grid, i))//check every col.
            return 0;
	}
	for(int row=0;row<16;row +=4)
		for(int col =0;col<16;col+= 4)
			if(!isBlockValid(grid, col, row))//Check the Blocks for validity.
				return 0;
    return 1;
}
