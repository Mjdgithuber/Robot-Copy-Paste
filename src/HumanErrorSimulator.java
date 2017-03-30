import java.util.Random;

public class HumanErrorSimulator {
	private Random rd = new Random();
	
	private int rows = 4, columns = 10;
	private int arrayRow, arrayCol;
	private char[][] keys = new char[rows][columns];
	private final String keyboard = "1234567890 qwertyuiop asdfghjkl; zxcvbnm,./";
	
	private double chanceForError = .05;
	private double chanceForStickyShift = .02;
	
	public HumanErrorSimulator(){
		fillArray();
	}
	
	private void fillArray(){
		String[] splitRows = keyboard.split(" ");
		
		for(int r = 0; r<splitRows.length; r++){
			char[] splitStringColumns = splitRows[r].toCharArray();
			
			for(int c = 0; c<splitStringColumns.length; c++) keys[r][c] = splitStringColumns[c];
		}
	}
	
	public char testForError(char c, boolean b){
		double percent = rd.nextDouble();
		
		if(percent<=chanceForError && b) return switchChar(c);//.05
		return c;
	}
	
	private char switchChar(char c){
		if(locateChar(c)){
			return setNewChar(arrayRow);
		}
		return c;
	}
	
	private char setNewChar(int row){//Location
		int xDisplacement, yDisplacement;
		if(row == 0){//1
			xDisplacement = rd.nextInt(3)-1;
			if(xDisplacement == 0){
				if(rd.nextDouble()<=.5) xDisplacement = -1;
				else xDisplacement = 1;
			}
			yDisplacement = 0;
		}else if(row == 1){//2
			xDisplacement = rd.nextInt(3)-1;
			yDisplacement = rd.nextInt(2);
			if(xDisplacement == 0 && yDisplacement == 0){
				if(rd.nextDouble()<=.5) xDisplacement = -1;
				else xDisplacement = 1;
			}
		}else if(row == 2){//3
			xDisplacement = rd.nextInt(3)-1;
			yDisplacement = rd.nextInt(3)-1;
			if(xDisplacement == 0 && yDisplacement == 0){
				if(rd.nextDouble()<=.5) xDisplacement = -1;
				else xDisplacement = 1;
			}
		}else{//If it equals 4
			xDisplacement = rd.nextInt(3)-1;
			yDisplacement = rd.nextInt(2)-1;
			if(xDisplacement == 0 && yDisplacement == 0){
				if(rd.nextDouble()<=.5) xDisplacement = -1;
				else xDisplacement = 1;
			}
		}
		
		int newCharRow = arrayRow+yDisplacement;
		
		int newCharCol = arrayCol+xDisplacement;
		if(newCharCol < 0){
			newCharCol = 1;
		}else if(newCharCol > 9){
			newCharCol = 8;
		}
		
		return keys[newCharRow][newCharCol];
	}
	
	private void checkForStickyShift(char c){
		//Check to see if the shift key should shift more than one letter
	}
	
	private boolean locateChar(char findChar){
		for(int r = 0; r<rows; r++){
			for(int c = 0; c<columns; c++){
				if(keys[r][c] == findChar){
					setArrayLocation(r, c);
					return true;
				}
			}
		}
		return false;
	}
	
	private void setArrayLocation(int r, int c){
		arrayRow = r;
		arrayCol = c;
	}
	
	public void setChanceForError(double errorChance){ chanceForError = errorChance; }
}