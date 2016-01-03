import java.util.*;
import java.io.*;
import java.nio.file.*;
import javax.script.*;
import java.nio.charset.StandardCharsets;
import org.jpl7.Query;
import org.jpl7.*;

/*
 	SudokuSolver in Java.
 	
 	SudokuSolver is Main Driving Class.
 	
 	Solve_java java class for solving sudoku in java language
 */
public class SudokuSolver{
	
	public native String[] solvec(String str);
	static {
		System.loadLibrary("SudokuSolver");
	}//Loading the library for Native C component
	
	public static void main(String[] args) throws IOException{
		
		String[] lang = args[0].split("=");
		String language_preference = lang[1];
		String output_file = args[1];
		String input_file = args[2];

		File ifile = new File(input_file);
		BufferedReader reader =  null;

		char[] grid = new char[256];
		String ip = "";
		long t1;
		long t2;


		try{
			reader = new BufferedReader(new FileReader(ifile));
			ip  = reader.readLine();
			grid = ip.toCharArray();
		}//End of try Block
		
		catch(IOException | FileNotFoundException e){System.out.println(e);}
		char[] work = grid.clone();
		
		switch(language_preference){
			case "python": System.out.println("You Selected python as Sudoku Solving language.");
					String[] cmd = new String[3];
					cmd[0] = "python";
					cmd[1] = "sudoku_solver.py";
					cmd[2] = ip;
					try{
						t1 = timing();
						Process pr = Runtime.getRuntime().exec(cmd);//Starting a new process with command python sudoku_solver.py <inputstring>
						BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream())); //Gathering the output of the python program into a string
						String python_output = in.readLine();
						t2 = timing();
						fwrite(output_file, python_output, (t2 - t1));
					}
					catch(IOException e){
						System.out.println(e);
					}
					break;
			
			case "c": System.out.println("You Selected C as Sudoku Solving language.");
					t1 = timing();
					SudokuSolver ob = new SudokuSolver();
					String[] ret = ob.solvec(ip);//Calling native component C function for solving.
					t2 = timing();
					fwrite(output_file, ret[0], (t2 -t1));
					break;
			
			case "java": System.out.println("You Selected java as sudoku solving language.");
					t1 = timing();
					if(Solve_java.solve(work,0)){ // Call Java function.
						t2 = timing();
						String owork = String.valueOf(work);
						fwrite(output_file, owork, (t2 - t1));
					}
						 else
							 System.out.println("This cant be solved");
					break;
			case "prolog": System.out.println("You Selected prolog as sudoku solving language.");
                    char[] hex = {'A','B','C','D','E','F'};
                    String[] values = {"10","11","12","13","14","15"}; 
                    String t = "consult('import/linux/home/adesu1/plprj/sudoku_solver.pl')";
                    Query query = new Query(t); //Consulting solve_prolog whether it exists or not.
                    System.out.println(query.hasSolution());
                    String qs = "solve_prolog(Y)";
                    t1 = System.currentTimeMillis();
                    Query q1 = new Query(qs);
                    Term[] ta = q1.oneSolution().get("Y").toTermArray(); //Get the solution from solve_prolog.
                    Term[] tb;
                    t2 = System.currentTimeMillis();
                    try{
                        File ofile = new File("output.txt");
                        ofile.createNewFile();
                        FileWriter writer = new FileWriter(ofile); //Creating a writer to ouput the solution.
                        float tmp;
                        char val;
                        for(int i=0;i<16;i++){
                            tb = ta[i].toTermArray();
								for(int j=0;j<16;j++){
                                    tmp = (float) java.lang.Integer.parseInt(tb[j].toString()) / 9;
                                    if(tmp <= 1)
                                        writer.write(tb[j].toString()+" "); //Writing the output.
                                    else{
                                        for(int k=0;k<5;k++)
											if(values[k].equals(tb[j].toString()))
                                                writer.write(hex[k]+" ");  //Writing the output.
                                        }
                                    }
                                    writer.write("\r\n");            
                        }
                        float x = (float)(t2 - t1) /1000;
                        writer.write("Time take= "+x+"seconds");
                        writer.flush();
                        writer.close();
                    }
                    catch(IOException e){System.out.println(e);}
                    break;
			
			case "js": System.out.println("You selected javaScript as sudoku solving language");
					try{
						t1 = timing();
						ScriptEngineManager factory = new ScriptEngineManager();//Creating an engine to inovke JavaScriptEngine.
						ScriptEngine engine = factory.getEngineByName("JavaScript");
						engine.eval(Files.newBufferedReader(Paths.get("/import/linux/home/adesu1/plprj/sudoku_solver.js"), StandardCharsets.UTF_8));
						Invocable inv = (Invocable)engine;
						String Js_ret = inv.invokeFunction("solve_JS",ip).toString();
						t2 = timing();
						fwrite(output_file, Js_ret, (t2 - t1));
					}
					catch(IOException | NoSuchMethodException | ScriptException e){System.out.println(e);}
					break;
			default:System.out.println("Enter a valid language preference");
						break;
		}
	}
	
public static void print(char[] grid){
      for(int i = 0; i < 256; i+= 16)
    	  System.out.println(Arrays.toString(Arrays.copyOfRange(grid, i, i + 16)));
	System.out.println();
	}
public static long timing(){
	return System.currentTimeMillis();
	}
public static void fwrite(String file, String sol, long t){ //Function to write the data into the file
	try{
		File ofile = new File(file);
		ofile.createNewFile();
		FileWriter writer = new FileWriter(ofile);

		float tf =(float) t / 1000;
		char[] cowork = sol.toCharArray();
		for(int i =0;i<256;i+=16)
			writer.write(Arrays.toString(Arrays.copyOfRange(cowork, i, i+16))+"\n");
		writer.write("\n");
		writer.write("Time taken = "+tf+" seconds");
		writer.close();
	}//End of try
	catch(IOException e){System.out.println(e);}//END of catch
	}//END of method
}

class Solve_java{
	public static boolean solve(char[] grid, int cell) //This is a recursive function which checks cell by cell for solution.
	   {
		  char[] options = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	      while (cell < 256 && grid[cell] != '.') //Checks if cell is empty or not.
	         cell++;
	      if(cell == 256)
	         return true;

	      for(int i = 0; i < 16; i++)
	      {
	    	  grid[cell] = options[i];	//Take an option from options array and place it in the cell.
	          if(isColumnValid(grid, cell % 16)) //Now check the column of the cell and if true move forward.
	             if(isRowValid(grid, cell / 16)) //Check the row of the cell and if true check Block
	                if(isBlockValid(grid, cell % 16, cell / 16)) //The corresponding block of the cell.
	                	if(isValid(grid) && solve(grid, cell +1)) //Check if entire grid is valid or not.
	                		return true;
	       }
	      grid[cell] = '.';
	      return false;
	   }

	   private static final int[] freqs = new int[16];

	   public static boolean isValid(char[] grid)
	   {
	      for(int i = 0; i < 16; i++)
	      {          
	         if(!isRowValid(grid, i)) //Check each row 
	            return false;
	         if(!isColumnValid(grid, i)) //Check each Column
	            return false;
	      }
	      for(int row=0;row<16;row+=4)
	    	  for(int col=0;col<16;col+=4)
	    		  if(!isBlockValid(grid, col, row)) //Check Block Wise.
	    			  return false;
	      
	      return true;
	   }

	   public static boolean isRowValid(char[] grid, int row)
	   {
	      Arrays.fill(freqs, 0);
	      char[] options = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	      int freq_index=-1;
	      
	      for(int i = 0; i < 16; i++)
	      {    	  
	    	 char cell = grid[row * 16 + i];
	         if(cell !='.'){
	        	 for(int j=0;j<16;j++){
	            	 if(cell == options[j]){ //Count the number of times a particular value has occurred in the given row. If number times > 1 then row is false.
	            		 freq_index = j;break;}
	             }
	        	 if(++freqs[freq_index] > 1)
	        		 return false;
	         }
	      }
	      return true;
	   }

	   public static boolean isColumnValid(char[] grid, int col)
	   {
	      Arrays.fill(freqs, 0);
	      char[] options = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	      int freq_index=-1;
	      
	      for(int i = 0; i < 16; i++)
	      {    	 
	         char cell = grid[i*16 + col];
	         if(cell != '.'){
	             for(int j=0;j<16;j++){
	            	 if(cell == options[j]){ //Count the number of times a particular value has occurred in the given col. If number times > 1 then col is false.
	            		 freq_index = j;break;}
	             }
	        	 if(++freqs[freq_index] > 1)
	        		 return false;
	         }
	      }
	      return true;
	   }

	   public static boolean isBlockValid(char[] grid, int col, int row)
	   {
	      Arrays.fill(freqs, 0);
	      char[] options = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
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
	      
	      for(int i = 0; i < 16; i++)
	      {
	    	 int temp = (y + i / 4) * 16 + (x + i % 4);
	         char cell = grid[temp];
	         if(cell != '.'){
	             for(int j=0;j<16;j++){
	            	 if(cell == options[j]){//Count the number of times a particular value has occurred in the given Block. If number times > 1 then Block is false.
	            		 freq_index = j;
	            		 break;}
	             }
	        	 if(++freqs[freq_index] > 1)
	        		 return false;
	         }
	      }
	      return true;
	   }
}
