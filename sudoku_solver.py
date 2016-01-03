import sys;

def solve(grid, cell):
	while(cell < 256 and ((grid[cell] != "."))):
		cell += 1;
	if(cell == 256):
		return True;
	options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	for i in range(0,16):
		grid[cell] = options[i];
		if isColumnValid(grid, cell % 16):
			if isRowValid(grid, cell / 16 ): 
				if isBlockValid(grid, cell%16, cell/16):
					if isvalid(grid):
						if solve(grid, cell+1):
							return True;
	grid[cell] = ".";
	return False;
#End of Solve().
def isColumnValid(grid, col):
#	print(grid[0]," ",col);
	options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	freq_index = -1;
	freqs = [0];
	for i in range(1, 16):
		freqs.append(0);
	for i in range(0,16):
		cell = grid[i * 16 + col];
		if (cell != "."):
			for j in range(0,16):
				if cell == options[j]:
					freq_index = j;
					break;
			freqs[freq_index] += 1;
			if freqs[freq_index] > 1:
				return False;
	return True;
#End of isColumnValid().
def isRowValid(grid, row):
        options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
        freq_index = -1;
        freqs = [0];
        for i in range(1, 16):
		freqs.append(0);
        for i in range(0,16):
		cell = grid[row * 16 + i];
		if (cell != "."):
			for j in range(0,16):
				if cell == options[j]:
					freq_index = j;
					break;
			freqs[freq_index] += 1;
			if freqs[freq_index] > 1:
				return False;
	return True;
#End of isRowValid().
def isBlockValid(grid, col, row):
	options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	freqs = [0];
	
	for i in range(1,16):
		freqs.append(0);
	
	if col >= 0 and col <=3:
		x =0;
	elif col >=4 and col <=7:
		x =4;
	elif col >=8 and col <=11:
		x = 8;
	else:
		x = 12;
	if row >= 0 and row <= 3:
		y=0;
	elif row >= 4 and row <=7:
		y=4;
	elif row >= 8 and row <=11:
		y=8;
	else:
		y=12;
	
	for i in range(0, 16):
		temp = (y +i / 4) * 16 + (x+i % 4);
		cell = grid[temp];
		if (cell != "."):
                        for j in range(0,16):
                                if cell == options[j]:
                                        freq_index = j;
                                        break;
                        freqs[freq_index] += 1;
                        if freqs[freq_index] > 1:
                                return False;
        return True;
#End of isBlockValid().
def isvalid(grid):
	for i in range(0, 16):
		if (not isRowValid(grid, i)):
			return False;
		if (not isColumnValid(grid, i)):
			return False;
	for row in range(0, 16, 4):
		for col in range(0, 16 ,4):
			if (not isBlockValid(grid, col, row)):
				return False;
	return True;

grid = list(sys.argv[1]);#Taking the given input as command line argument and making a list out of it.
bool = solve(grid, 0); #solving the grid.
if bool:
	print "".join(grid);
else:
	print "Not solved";
