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
	
	boolean wordNeedsBackspacing;
	char actualKey;
	
	int spaceAt; //Used to determine were a word begins and ends
	int currentCharNo;
	char[] charsInLine;
	
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
	
	private void shiftChar(char c, boolean inHash){
		r.keyPress(KeyEvent.VK_SHIFT);
		if(inHash) r.keyPress(hmap.get(c));
		else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
		r.keyRelease(KeyEvent.VK_SHIFT);
	}
  	
	private void fileInput() throws InterruptedException{
		setCharsPerMinute(400);//This line of code sets the chars per minute (in the future this will be dictated by the user)
		Thread.sleep(2000);//This will be removed later on (hard coded sleep)

		try (BufferedReader br = new BufferedReader(new FileReader("robotCopy.txt"))){
			String currentLine;//Makes a local string to store the chars
			while ((currentLine = br.readLine()) != null) {//Will only go into loop if there is something on the line of the file
				//System.out.println(currentLine);
				char[] stringToChars = currentLine.toCharArray(); //Converts the current line into an array of chars
				
				charsInLine = stringToChars;//Records stringToChars in a global array so it can be used in other methods
				
				for(int i = 0; i<currentLine.length(); i++){
					sleep();//Sleeps for a certain amount of time dictated by the setCharsPerMinute method

					if(stringToChars[i] == ' ') spaceAt = i;//If there is a space than it records it (this is used to determine where the word begins)
					currentCharNo = i;//This save the location in the array of the char we are currently dealing with

					//If the char is one of the special chars (spChars at top of class) it will 'convert' it into it 'lowercase' and then shift it back
					if(doesListContain(spChars, stringToChars[i])) shiftChar(stringToChars[i], true);
					else{
						//If the char is uppercase then we need to send it to be uppercased
						if(Character.isUpperCase(stringToChars[i])) shiftChar(stringToChars[i], false);
						else{
							try{
								char possibleErrorKey = typoError.testForError(stringToChars[i]);//This generates a char that might be wrong (Human Error)
								if(possibleErrorKey != stringToChars[i])//Checks to see if there was an error (this error is fake and on purpose!)
									wordNeedsBackspacing = true;//Tells the program to backspace when it is time
								r.keyPress(KeyEvent.getExtendedKeyCodeForChar(possibleErrorKey));//Types the key (error or not)
							}
							catch(Exception e){e.printStackTrace();}
						}
					}
					
					if(wordNeedsBackspacing){
						wordNeedsBackspacing = false;
						sleep();
						sleep();
						
						int goBack = 0;
						
						for(int loops = 0; currentCharNo-loops > -1 && charsInLine[currentCharNo-loops] != ' '; loops++){
							goBack++;
							r.keyPress(KeyEvent.VK_BACK_SPACE);
							sleep();
						}
						
						for(int reWrite=0; reWrite<goBack; reWrite++){
							if(doesListContain(spChars, charsInLine[currentCharNo-goBack+reWrite+1])) shiftChar(charsInLine[currentCharNo-goBack+reWrite+1], true);
							else{
								if(Character.isUpperCase(charsInLine[currentCharNo-goBack+reWrite+1])) shiftChar(charsInLine[currentCharNo-goBack+reWrite+1], false);
								else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(charsInLine[currentCharNo-goBack+reWrite+1]));
							}
							sleep();
						}
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
	
	private void sleep(int millis){
		try{
			Thread.sleep(millis);
		}catch(Exception e){ e.printStackTrace(); }
	}
}


