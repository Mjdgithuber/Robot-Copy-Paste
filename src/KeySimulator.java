import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
 
public class KeySimulator {
	private Robot r;
	private Random rd = new Random();
	public FileCreator fileCreator = new FileCreator();
	private Translator translator = new Translator(this);
	public HumanErrorSimulator typoError = new HumanErrorSimulator();
	private HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
	
	private int charsPM = 500;//Chars per minute
	
	private boolean wordNeedsFixing, errorKeyIsAWordEnd;
	
	private int currentCharNo;
	private char[] charsInLine;
	
	private final char[] spChars = {'"' , ':', '?', '{', '}', '<', '>', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '|' };
	private final char[] toChars = {'\'', ';', '/', '[', ']', ',', '.', '`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\\'};
	private final char[] wordEnds = {' ', '.', '{', '}', '[', ']', '(', ')', '-', ','}; //Used to find where a words ends or begins
	
	private boolean fixImm, compThenFix, deleteThenFix;
	private double baseChanceToFixWordImmediately = .5;
	private double baseChanceToCompleteWordBeforeFixing = .3;
	private double baseChanceDeleteWholeWordThenFix = .2;
	
	
	private double chanceToFixWordImmediately = .2;//20
	private double chanceToCompleteWordBeforeFixing = .5;//30
	private double chanceDeleteWholeWordThenFix = .3;//50
	private int previousErrorType=0;//1 is to fix immediatly, 2 to complete word before fixing, 3 is to delete word then fix
	
	private int skipForward = 0;
	
	/**
	 * Makes a new robot and fills the hashmap for the object
	 */
	public KeySimulator(){
		try {
			r = new Robot();
			fillHashMap();
		}catch (AWTException e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the clipboard to the file (Paste Dump) and then starts to type it
	 */
	public void start() throws HeadlessException, UnsupportedFlavorException, IOException{
		fileCreator.writeToFile("Paste Dump", translator.translate((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
		try {getFileThenType();} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * Fills the hash map with the chars that are not letters, but still need to be typed (like { * %)
	 */
	private void fillHashMap(){
		for(int i = 0; i<spChars.length; i++) hmap.put(spChars[i], KeyEvent.getExtendedKeyCodeForChar(toChars[i]));
	}
	
	/**
	 * Takes a char and types it with the shift key on
	 * @param c The char you want to shift
	 * @param inHash This tells the method if the key is a specail (not a letter, like { or +)
	 */
	private void shiftChar(char c, boolean inHash){
		r.keyPress(KeyEvent.VK_SHIFT);
		try{
			if(inHash) r.keyPress(hmap.get(c));
			else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
		}catch(Exception e){e.printStackTrace();}
		r.keyRelease(KeyEvent.VK_SHIFT);
	}
  	
	/**
	 * This gets the file (Paste Dump is the name) and reads from it and sends it to be typed
	 * @throws InterruptedException sends the InterruptedException to the caller of the method
	 */
	private void getFileThenType() throws InterruptedException{
		Thread.sleep(2000);//This will be removed later on (hard coded sleep)
		
		String[] allLines = fileCreator.readFromFile("Paste Dump");
		for(int i = 0; i<allLines.length; i++) typeLine(allLines[i]);
	}
	
	/**
	 * Types the current line
	 * @param currentLine The string of the line that is to be typed
	 */
	private void typeLine(String currentLine){
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
						char possibleErrorKey = typoError.testForError(stringToChars[i], hasLetterFixer());//This generates a char that might be wrong (Human Error)
						if(possibleErrorKey != stringToChars[i]){//Checks to see if there was an error (this error is fake and on purpose!)
							wordNeedsFixing = true;//Tells the program to backspace when it is time
							if(Utilities.doesListContain(wordEnds, stringToChars[i]))
								errorKeyIsAWordEnd = true;
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
		sleep(3);
	}
	
	private boolean hasLetterFixer(){
		if(fixImm) return true;
		if(compThenFix) return true;
		if(deleteThenFix) return true;
		return false;
	}
	
	/**
	 * This method determines what kind of error it should be and calls the appropriate methods
	 */
	private void fixWord(){
		sleep(2);
		
		//This determines what kind of error it is going to be (It is random, but it won't be the same error twice)
		double randNum = rd.nextDouble();
		
		double totalErrorPercent=0, error1Chance = 0, error2Chance = 0, error3Chance = 0;
		double factor=1;
		
		if(fixImm) totalErrorPercent += baseChanceToFixWordImmediately;
		if(compThenFix) totalErrorPercent += baseChanceToCompleteWordBeforeFixing;
		if(deleteThenFix)totalErrorPercent += baseChanceDeleteWholeWordThenFix;
		
		if(totalErrorPercent<1d) factor = 1/totalErrorPercent;
		
		if(fixImm) error1Chance = baseChanceToFixWordImmediately*factor;
		if(compThenFix) error2Chance = baseChanceToCompleteWordBeforeFixing*factor;
		if(deleteThenFix)error3Chance = baseChanceDeleteWholeWordThenFix*factor;
		
		if(randNum <= error1Chance && error1Chance!=0) fixWordImmediately();
		else if(randNum <= error2Chance+error1Chance && error2Chance!=0) finishWordThenFix();
		else if(randNum <= error3Chance+error2Chance+error1Chance && error3Chance!=0) deleteWordThenFix();
		
		//These just reset the booleans so it doesn't mess with future errors
		errorKeyIsAWordEnd = false;
		wordNeedsFixing = false;
	}
	
	/**
	 * This method will fix the char immediately
	 */
	private void fixWordImmediately(){
		previousErrorType = 1;//Sets the error type to the corresponding type
		
		backspaceAndSleep();
		
		writeLetters(currentCharNo);
	}
	
	/**
	 * This method finishes the word and then goes back to fix the error using the arrow keys 
	 */
	private void finishWordThenFix(){
		previousErrorType = 2;//Sets the error type to the corresponding type
		
		//Will finish the word with no more errors (by reaching the end of the line or by reaching an end-word char defined at top of class)
		int displacement = 0;
		for(int i = 1; ((currentCharNo+i)<charsInLine.length &&(!Utilities.doesListContain(wordEnds, charsInLine[currentCharNo+i]))); i++){
			writeLetters(currentCharNo+i);
			displacement++;
		}
		
		//Goes back to the bad char by using the left arrow key
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
		
		skipForward = displacement;//Makes sure the main method doesn't write keys that this method just did
	}
	
	/**
	 * This method deletes the entire word after the error key is pressed and re-types up to the point of the error (fixing the bad key)
	 */
	private void deleteWordThenFix(){
		previousErrorType = 3;//Sets the error type to the corresponding type
		
		int goBack = 0;
		
		//The loop backspaces a certain amount of times and records how many times it does it
		for(int loops = 0; currentCharNo-loops > -1 && !Utilities.doesListContain(wordEnds, charsInLine[currentCharNo-loops]); loops++){
			goBack++;
			backspaceAndSleep();
		}
		
		//This if checks to see if the key that was ther error is a word end (because if it is the error key wouldn't be deleted)
		if(errorKeyIsAWordEnd){
			goBack++;
			backspaceAndSleep();
		}
		
		//This loop re-writes the deleted chars (currentCharNo-goBack+reWrite+1 = the char that was the error key - how many times it went back+the times it has already gone back+1)
		for(int reWrite=0; reWrite<goBack; reWrite++) writeLetters(currentCharNo-goBack+reWrite+1);
	}
	
	/**
	 * This method is used by all of the error types, and is used to re-write (or write) letters, and won't generate any errors
	 * @param charLocation This is where the wrong char is located
	 */
	private void writeLetters(int charLocation){
		if(Utilities.doesListContain(spChars, charsInLine[charLocation])) shiftChar(charsInLine[charLocation], true);
		else{
			if(Character.isUpperCase(charsInLine[charLocation])) shiftChar(charsInLine[charLocation], false);
			else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(charsInLine[charLocation]));
		}
		sleep();
	}
	
	/**
	 * It backspaces one key and then sleeps
	 */
	private void backspaceAndSleep(){
		r.keyPress(KeyEvent.VK_BACK_SPACE);
		sleep();
	}
  	
	/**
	 * Sets the chars per minute to change the amount of time the thread sleeps
	 * @param cps What you are changing the chars per minute to
	 */
	public void setCharsPerMinute(int cps){ charsPM = cps; }
	
	/**
	 * Returns the chars per minute
	 * @return Returns the chars per minute as an int
	 */
	public int getCharsPerMinute(){ return charsPM; }
	
	public void setFixImm(boolean b){ fixImm = b; }
	public void setCompThenFix(boolean b){ compThenFix = b; }
	public void setDeleteThenFix(boolean b){ deleteThenFix = b; }
	
	/**
	 * This method stop the thread for a amount of time (dictated by the chars per minute)
	 */
	public void sleep(){superSleep(1);}
	
	/**
	 * This method stop the thread for a amount of time (dictated by the chars per minute) multiplied by the multiplier parameter
	 * @param multiplier Makes the sleep longer or shorter reletive to the chars per minute
	 */
	public void sleep(double multiplier){superSleep(multiplier);}
	
	/**
	 * This method contains all of the code for the sleep method so they both don't repeat the same code
	 * @param multiplier Multipies the amount of time stoped by the thread (1 is the default if you don't want to change anything)
	 */
	private void superSleep(double multiplier){
		double milliStop = 1d/(charsPM*1d / 60d / 1000d); //60 for seconds per minute, and 1000 for milliseconds per second
		long millis = (long) (((1 + rd.nextDouble() + (rd.nextDouble()*.5) + (rd.nextDouble()*.25))*milliStop)*multiplier); //Test to generate human varibles long millis = (long) (((.75 + (rd.nextDouble()))*milliStop)*multiplier); 
		//long millis = (long) (milliStop*multiplier);
		try{
			Thread.sleep(millis);
		}catch(Exception e){ e.printStackTrace(); }
	}
}