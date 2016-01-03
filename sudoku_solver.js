function solve_JS(ip){ //Solve_JS this called from java with input as string.
	grid = ip.split(''); //split the given ip into char array.
	if (solve(grid, 0))
		return grid.join("");
	else 
		print("not solved");

	return grid.join("");
		
}

function solve(grid, cells){//Solve each cell by cell recursively calling solve.
	var options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	while(cells < 256 && grid[cells] != '.')
		cells++;
	if(cells == 256)
		return true;
	for (var i=0;i<16;i++){
		grid[cells] = options[i];
		if (isColumnValid(grid, cells % 16))
			if(isRowValid(grid, Math.floor(cells / 16)))
				if(isBlockValid(grid, cells % 16, Math.floor(cells / 16)))
					if(isValid(grid) && solve(grid, cells+1))
					return true;
	}
	grid[cells] = '.';
	return false;
	
}

function isColumnValid(grid, colC){
	var options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	var freqs = [0];
	for(var i=1;i<16;i++)
		freqs.push(0);
	var freq_index = -1;
	for(var i=0;i<16;i++){
		var cell = grid[i * 16 + colC];
		if(cell != '.'){
				for(var j=0;j<16;j++){
	            	 if(cell == options[j]){
	            		 freq_index = j;break;}
	             }
	        	 if(++freqs[freq_index] > 1)
	        		 return false;
		}
	}
	return true;
}

function isRowValid(grid, rowR){
	var options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	var freqs = [0];
	for(var i=1;i<16;i++)
		freqs.push(0);
	var freq_index = -1;
	for(var i=0;i<16;i++){
		var cell = grid[rowR * 16 + i];
		if(cell != '.'){
				for(var j=0;j<16;j++){
	            	 if(cell == options[j]){
	            		 freq_index = j;break;}
	             }
	        	 if(++freqs[freq_index] > 1)
	        		 return false;
		}
	}
	return true;
}

function isBlockValid(grid, colB, rowB){
	var options = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
	var freq = [0];
	for(var i=1;i<16;i++)
		freq.push(0);
	var freq_index = -1;
	var x,y;      
	if(colB >= 0 && colB<=3)
		x = 0;
	else if(colB >=4 && colB <=7)
		x = 4;
	else if(colB >= 8 && colB <=11)
		x = 8;
	else
		x=12;
	if(rowB >= 0 && rowB<=3)
		y = 0;
	else if(rowB >=4 && rowB <=7)
		y = 4;
	else if(rowB >= 8 && rowB <=11)
		y = 8;
	else
		y=12;
	      
	for(var i = 0; i < 16; i++){
		var temp = (y + Math.floor(i / 4)) * 16 + (x + i % 4);
		var cell = grid[temp];
		if(cell != '.'){
			for(var j=0;j<16;j++){
				if(cell == options[j]){
					freq_index = j;
					break;}
			}
		if(++freq[freq_index] > 1)
		return false;
		}
	}
	return true;
}

function isValid(grid){
	for(var i = 0; i < 16; i++)
	{          
		if(!isRowValid(grid, i))
			return false;
		if(!isColumnValid(grid, i))
			return false;
	}
	for(var row=0;row<16;row+=4)
		for(var col=0;col<16;col+=4)
			if(!isBlockValid(grid, col, row))
				return false;
	return true;

}
function cheese(grid){
	print (grid[0]);
	grid[0] = '1';
	print (grid[0]);
}
	
