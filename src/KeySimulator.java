import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
 
public class KeySimulator {
	Robot r;
	Random rd = new Random();
	Scanner scan = new Scanner(System.in);
	FileCreator fileCreator = new FileCreator();
	HumanErrorSimulator typoError = new HumanErrorSimulator();
	HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
	
	int charsPM;//Chars per minute
	
	boolean backspace;
	char actualKey;
	
	char[] spChars = {'"' , ':', '?', '{', '}', '<', '>', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+'};
	char[] toChars = {'\'', ';', '/', '[', ']', ',', '.', '`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '='};
	
	
	public KeySimulator(){
		try {
			r = new Robot();
			fillHashMap();
		}catch (AWTException e) {e.printStackTrace();}
	}
	
	public void start(){
		//fillHashMap();
		//createAndWriteFile();
		fileCreator.createAndWriteFile();
		try {fileInput();} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	private void fillHashMap(){
		for(int i = 0; i<spChars.length; i++) hmap.put(spChars[i], KeyEvent.getExtendedKeyCodeForChar(toChars[i]));
	}

	private boolean doesListContain(char[] array, char c){
		for(char n : array){
			if(c == n) return true;
		}
		return false;
	}
	
	private void shiftChar(Robot robot, char c, boolean inHash){
		r.keyPress(KeyEvent.VK_SHIFT);
		if(inHash) r.keyPress(hmap.get(c));
		else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
		r.keyRelease(KeyEvent.VK_SHIFT);
	}
  	
	private void fileInput() throws InterruptedException{
		setCharsPerMinute(800);//6000
		Thread.sleep(2000);
		
		try (BufferedReader br = new BufferedReader(new FileReader("robotCopy.txt"))){//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Matthew Dennerlein\\Desktop\\Hello.txt"))
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				System.out.println(currentLine);
				char[] sToChars = currentLine.toCharArray();
				
				for(int i = 0; i<currentLine.length(); i++){
					sleep();
					
					if(doesListContain(spChars, sToChars[i])) shiftChar(r, sToChars[i], true);
					else{
						if(Character.isUpperCase(sToChars[i])) shiftChar(r, sToChars[i], false);
						else{
							try{
								char errorKey = typoError.testForError(sToChars[i]);
								if(errorKey != sToChars[i]){
									backspace = true;
									actualKey = sToChars[i];
								}
								r.keyPress(KeyEvent.getExtendedKeyCodeForChar(errorKey));
								//r.keyPress(KeyEvent.getExtendedKeyCodeForChar(sToChars[i]));
							}
							catch(Exception e){e.printStackTrace();}
						}
					}
					
					if(backspace){
						backspace = false;
						sleep();
						r.keyPress(KeyEvent.VK_BACK_SPACE);
						sleep();
						try {r.keyPress(KeyEvent.getExtendedKeyCodeForChar(actualKey));}catch(Exception e){}
					}
				}
				r.keyPress(KeyEvent.VK_ENTER);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
  	
	private void setCharsPerMinute(int cps){ charsPM = cps; }
	
	private void sleep(){
		double milliStop = 1d/(charsPM*1d / 60d / 1000d); //60 for seconds per minute, and 1000 for milliseconds per second
		//long millis = (long) ((.75 + (rd.nextDouble()))*milliStop); //Test to generate human varibles
		long millis = (long) milliStop;
		try{
			Thread.sleep(millis);
		}catch(Exception e){ e.printStackTrace(); }
	}
}
