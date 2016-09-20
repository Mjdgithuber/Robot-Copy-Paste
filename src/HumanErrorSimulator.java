import java.util.Random;

public class HumanErrorSimulator {
	
Random rd = new Random();
	
	int rows = 4, columns = 10;
	int arrayRow, arrayCol;
	char[][] keys = new char[rows][columns];
//	String keyboard = "`1234567890-= qwertyuiop[]\\ asdfghjkl;' zxcvbnm,./";
	String keyboard = "1234567890 qwertyuiop asdfghjkl; zxcvbnm,./";
	
	public HumanErrorSimulator(){
		fillArray();
		while(true){
			char test = testForError('5');
			if(test != '5') System.out.println(test);
		}
	}
	
	public static void main(String[] args){
		new HumanErrorSimulator();
	}
	
	private void fillArray(){
		String[] splitRows = keyboard.split(" ");
		
		for(int r = 0; r<splitRows.length; r++){
			char[] splitStringColumns = splitRows[r].toCharArray();
			
			for(int c = 0; c<splitStringColumns.length; c++) keys[r][c] = splitStringColumns[c];
		}
	}
	
	public char testForError(char c){
		double percent = rd.nextDouble();
		
		if(percent<=.05) return switchChar(c);
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
		
//		System.out.println("");
//		System.out.println("xDisplacement: "+xDisplacement);
//		System.out.println("yDisplacement: "+yDisplacement);
//		System.out.println("");
		
		int newCharRow = arrayRow+yDisplacement;
		
		int newCharCol = arrayCol+xDisplacement;
		if(newCharCol < 0){
			newCharCol = 1;
		}else if(newCharCol > 9){
			newCharCol = 8;
		}
		
		return keys[newCharRow][newCharCol];
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
}