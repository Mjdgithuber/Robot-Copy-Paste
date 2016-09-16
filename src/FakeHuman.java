import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
 
public class FakeHuman {
        	
	Random rd = new Random();
	Scanner scan = new Scanner(System.in);
	Robot r;
	int charsPM;//Chars per minute
	HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
	char currentChar;
	char[] spChars = {'"' , ':', '?', '{', '}', '<', '>'};
	char[] toChars = {'\'', ';', '/', '[', ']', ',', '.'};
	
	public FakeHuman(){
		try {r = new Robot();}
		catch (AWTException e) {e.printStackTrace();}
	}
 
	public static void main(String[] args) throws InterruptedException{
		FakeHuman f = new FakeHuman();
		f.fillHashMap();
		//f.input();
		f.fileInput();
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
  	
	private void fileInput() throws InterruptedException{
		setCharsPerMinute(5000);
		Thread.sleep(1000);
		
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Matthew Dennerlein\\Desktop\\Hello.txt"))){//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Matthew Dennerlein\\Desktop\\Hello.txt"))
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				char[] sToChars = currentLine.toCharArray();
				
				for(int i = 0; i<currentLine.length(); i++){
					sleep();
					
					if(doesListContain(spChars, sToChars[i])) {
						r.keyPress(KeyEvent.VK_SHIFT);
						r.keyPress(hmap.get(sToChars[i]));
						r.keyRelease(KeyEvent.VK_SHIFT);
					}else{
						if(Character.isUpperCase(sToChars[i])){
							r.keyPress(KeyEvent.VK_SHIFT);
							r.keyPress(KeyEvent.getExtendedKeyCodeForChar(sToChars[i]));
							r.keyRelease(KeyEvent.VK_SHIFT);
						}else {
							r.keyPress(KeyEvent.getExtendedKeyCodeForChar(sToChars[i]));
						}
					}
				}
				r.keyPress(KeyEvent.VK_ENTER);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
  	
//  	private void input() throws InterruptedException{
//        	setCharsPerMinute(120);
//        	System.out.println("Enter String: ");
//        	String s = scan.nextLine();
//        	Thread.sleep(1000);
//        	char[] sToChars = s.toCharArray();
//        	
//        	for(int i = 0; i<s.length(); i++){
//              	//System.out.println(sToChars[i]);
//              	sleep();
//              	if(Character.isUpperCase(sToChars[i])){
//                    	r.keyPress(KeyEvent.VK_SHIFT);
//                    	r.keyPress(KeyEvent.getExtendedKeyCodeForChar(Character.toLowerCase(sToChars[i])));
//                    	r.keyRelease(KeyEvent.VK_SHIFT);
//              	}else r.keyPress(KeyEvent.getExtendedKeyCodeForChar(sToChars[i]));
//        	}
//		}
  	
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


