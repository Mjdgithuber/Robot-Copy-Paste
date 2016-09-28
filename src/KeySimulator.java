import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
 
public class KeySimulator {
	private Robot r;
	private Random rd = new Random();
	private Scanner scan = new Scanner(System.in);
	public FileCreator fileCreator = new FileCreator();
	private Translator translator = new Translator(this);
	public HumanErrorSimulator typoError = new HumanErrorSimulator();
	private HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
	
	private int charsPM = 500;//Chars per minute
	
	private boolean wordNeedsFixing, errorKeyIsAWordEnd;
	
	private int currentCharNo;
	private char[] charsInLine;
	
	public final char[] spChars = {'"' , ':', '?', '{', '}', '<', '>', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '|' };
	private final char[] toChars = {'\'', ';', '/', '[', ']', ',', '.', '`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\\'};
	private final char[] wordEnds = {' ', '.', '{', '}', '[', ']', '(', ')', '-', ',', '\n'}; //Used to find where a words ends or begins
	
	private double chanceToFixWordImmediately = .2;
	private double chanceToCompleteWordBeforeFixing = .5;
	private double chanceDeleteWholeWordThenFix = 1d;
	
	private int skipForward = 0;
	
	public KeySimulator(){
		try {
			r = new Robot();
			fillHashMap();
		}catch (AWTException e) {e.printStackTrace();}
	}
	
	public void start() throws HeadlessException, UnsupportedFlavorException, IOException{
		fileCreator.writeToFile("Paste Dump", translator.translate((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
		//fileCreator.createAndWriteFile();
		try {getFileThenType();} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	private void fillHashMap(){
		for(int i = 0; i<spChars.length; i++) hmap.put(spChars[i], KeyEvent.getExtendedKeyCodeForChar(toChars[i]));
	}
	
	private void shiftChar(char c, boolean inHash){
		r.keyPress(KeyEvent.VK_SHIFT);
		try{
			if(inHash) r.keyPress(hmap.get(c));
			else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
		}catch(Exception e){e.printStackTrace();}
		r.keyRelease(KeyEvent.VK_SHIFT);
	}
  	
	private void getFileThenType() throws InterruptedException{
		Thread.sleep(2000);//This will be removed later on (hard coded sleep)
		
		String[] allLines = fileCreator.readFromFile("Paste Dump");
		for(int i = 0; i<allLines.length; i++) typeLine(allLines[i]);
	}
	
	private void typeLine(String currentLine){
		//System.out.println(currentLine);
		char[] stringToChars = currentLine.toCharArray(); //Converts the current line into an array of chars
		
		charsInLine = stringToChars;//Records stringToChars in a global array so it can be used in other methods
		
		for(int i = 0; i<currentLine.length(); i++){
			sleep();//Sleeps for a certain amount of time dictated by the setCharsPerMinute method

			currentCharNo = i;//This save the location in the array of the char we are currently dealing with

			//If the char is one of the special chars (spChars at top of class) it will 'convert' it into it 'lowercase' and then shift it back
			if(Utilities.doesListContain(spChars, stringToChars[i])) shiftChar(stringToChars[i], true);
			else{
				//If the char is uppercase then we need to send it to be uppercased
				if(Character.isUpperCase(stringToChars[i])) shiftChar(stringToChars[i], false);
				else{
					try{
						char possibleErrorKey = typoError.testForError(stringToChars[i]);//This generates a char that might be wrong (Human Error)
						if(possibleErrorKey != stringToChars[i]){//Checks to see if there was an error (this error is fake and on purpose!)
							wordNeedsFixing = true;//Tells the program to backspace when it is time
							if(Utilities.doesListContain(wordEnds, stringToChars[i])){
								errorKeyIsAWordEnd = true;
							}
						}
						r.keyPress(KeyEvent.getExtendedKeyCodeForChar(possibleErrorKey));//Types the key (error or not)
					}
					catch(Exception e){e.printStackTrace();}
				}
			}
			
			if(wordNeedsFixing){
				fixWord();
				i += skipForward;
				skipForward = 0;
			}
		}
		sleep();
		r.keyPress(KeyEvent.VK_ENTER);
	}
	
	private void fixWord(){
		sleep(2);
		
		double randNum = rd.nextDouble();
		
//		if(randNum <= chanceToFixWordImmediately) fixWordImmediately();
//		else if(randNum <= chanceToCompleteWordBeforeFixing) finishWordThenFix();
//		else if(randNum <= chanceDeleteWholeWordThenFix) deleteWordThenFix();
		
		finishWordThenFix();
		
		wordNeedsFixing = false;
	}
	
	private void fixWordImmediately(){
		r.keyPress(KeyEvent.VK_BACK_SPACE);
		sleep();
		
		writeLetters(currentCharNo);
	}
	
	private void finishWordThenFix(){
		//Will finish the word (checks by reaches the end of the line or by reaching an end-word char defined at top of class)
		int displacement = 0;
		for(int i = 1; ((currentCharNo+i)<charsInLine.length &&(!Utilities.doesListContain(wordEnds, charsInLine[currentCharNo+i]))); i++){
			writeLetters(currentCharNo+i);
			displacement++;
		}
		
		//Finds bad char and uses left arrow to go back to it
		for(int i = 0; i<displacement; i++){
			r.keyPress(KeyEvent.VK_LEFT);
			sleep(.90);
		}
		
		fixWordImmediately();//Fixes the char
		
		sleep(1.25);
		
		//Goes back to the end of the word
		for(int i = 0; i<displacement; i++){
			r.keyPress(KeyEvent.VK_RIGHT);
			sleep(.90);
		}
		
		skipForward = displacement;
	}
	
	private void deleteWordThenFix(){
		int goBack = 0;
		
		for(int loops = 0; currentCharNo-loops > -1 && !Utilities.doesListContain(wordEnds, charsInLine[currentCharNo-loops]); loops++){
			goBack++;
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			sleep();
		}
		if(errorKeyIsAWordEnd){
			errorKeyIsAWordEnd = false;
			goBack++;
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			sleep();
		}
		
		for(int reWrite=0; reWrite<goBack; reWrite++) writeLetters(currentCharNo-goBack+reWrite+1);
	}
	
	private void writeLetters(int charLocation){
		if(Utilities.doesListContain(spChars, charsInLine[charLocation])) shiftChar(charsInLine[charLocation], true);
		else{
			if(Character.isUpperCase(charsInLine[charLocation])) shiftChar(charsInLine[charLocation], false);
			else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(charsInLine[charLocation]));
		}
		sleep();
	}
  	
	public void setCharsPerMinute(int cps){ charsPM = cps; }
	
	public int getCharsPerMinute(){ return charsPM; }
	
	public void sleep(){
		double milliStop = 1d/(charsPM*1d / 60d / 1000d); //60 for seconds per minute, and 1000 for milliseconds per second
		//long millis = (long) ((.75 + (rd.nextDouble()))*milliStop); //Test to generate human varibles
		long millis = (long) milliStop;
		try{
			Thread.sleep(millis);
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	public void sleep(double multiplier){
		double milliStop = 1d/(charsPM*1d / 60d / 1000d); //60 for seconds per minute, and 1000 for milliseconds per second
		//long millis = (long) ((.75 + (rd.nextDouble()))*milliStop); //Test to generate human varibles
		long millis = (long) (milliStop*multiplier);
		try{
			Thread.sleep(millis);
		}catch(Exception e){ e.printStackTrace(); }
	}
}