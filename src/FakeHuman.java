import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
	
	public FakeHuman(){//Hello
		try {r = new Robot();}
		catch (AWTException e) {e.printStackTrace();}
	}

	public static void main(String[] args) throws InterruptedException{
		FakeHuman f = new FakeHuman();
		f.fillHashMap();
		//f.input();
		f.fileInput();
	}
	
	private boolean isCapped(char c){
		switch(c){
			case 'A':
				currentChar='a';
				return true;
			case 'B':
				currentChar='b';
				return true;
			case 'C':
				currentChar='c';
				return true;
			case 'D':
				currentChar='d';
				return true;
			case 'E':
				currentChar='e';
				return true;
			case 'F':
				currentChar='f';
				return true;
			case 'G':
				currentChar='g';
				return true;
			case 'H':
				currentChar='h';
				return true;
			case 'I':
				currentChar='i';
				return true;
			case 'J':
				currentChar='j';
				return true;
			case 'K':
				currentChar='k';
				return true;
			case 'L':
				currentChar='l';
				return true;
			case 'M':
				currentChar='m';
				return true;
			case 'N':
				currentChar='n';
				return true;
			case 'O':
				currentChar='o';
				return true;
			case 'P':
				currentChar='p';
				return true;
			case 'Q':
				currentChar='q';
				return true;
			case 'R':
				currentChar='r';
				return true;
			case 'S':
				currentChar='s';
				return true;
			case 'T':
				currentChar='t';
				return true;
			case 'U':
				currentChar='u';
				return true;
			case 'V':
				currentChar='v';
				return true;
			case 'W':
				currentChar='w';
				return true;
			case 'Y':
				currentChar='y';
				return true;
			case 'X':
				currentChar='x';
				return true;
			case 'Z':
				currentChar='z';
				return true;
		}
		return false;
	}
	
	private void fillHashMap(){
		hmap.put('a', KeyEvent.VK_A);
		hmap.put('b', KeyEvent.VK_B);
		hmap.put('c', KeyEvent.VK_C);
		hmap.put('d', KeyEvent.VK_D);
		hmap.put('e', KeyEvent.VK_E);
		hmap.put('f', KeyEvent.VK_F);
		hmap.put('g', KeyEvent.VK_G);
		hmap.put('h', KeyEvent.VK_H);
		hmap.put('i', KeyEvent.VK_I);
		hmap.put('j', KeyEvent.VK_J);
		hmap.put('k', KeyEvent.VK_K);
		hmap.put('l', KeyEvent.VK_L);
		hmap.put('m', KeyEvent.VK_M);
		hmap.put('n', KeyEvent.VK_N);
		hmap.put('o', KeyEvent.VK_O);
		hmap.put('p', KeyEvent.VK_P);
		hmap.put('q', KeyEvent.VK_Q);
		hmap.put('r', KeyEvent.VK_R);
		hmap.put('s', KeyEvent.VK_S);
		hmap.put('t', KeyEvent.VK_T);
		hmap.put('u', KeyEvent.VK_U);
		hmap.put('v', KeyEvent.VK_V);
		hmap.put('w', KeyEvent.VK_W);
		hmap.put('y', KeyEvent.VK_Y);
		hmap.put('x', KeyEvent.VK_X);
		hmap.put('z', KeyEvent.VK_Z);
		hmap.put(' ', KeyEvent.VK_SPACE);
		hmap.put('.', KeyEvent.VK_PERIOD);
		hmap.put('-', KeyEvent.VK_ASTERISK);
		hmap.put('\'', KeyEvent.VK_QUOTE);
	}
	
	private void fileInput() throws InterruptedException{
		setCharsPerMinute(350);
		Thread.sleep(1000);

		
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Matthew Dennerlein\\Desktop\\Hello.txt"))){
			
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				char[] sToChars = currentLine.toCharArray();
				
				for(int i = 0; i<currentLine.length(); i++){
					//System.out.println(sToChars[i]);
					sleep();
					if(isCapped(sToChars[i])){
						r.keyPress(KeyEvent.VK_SHIFT);
						r.keyPress(hmap.get(currentChar));
						r.keyRelease(KeyEvent.VK_SHIFT);
					}else r.keyPress(hmap.get(sToChars[i]));
				}
				r.keyPress(KeyEvent.VK_ENTER);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void input() throws InterruptedException{
		setCharsPerMinute(120);
		System.out.println("Enter String: ");
		String s = scan.nextLine();
		Thread.sleep(1000);
		char[] sToChars = s.toCharArray();
		
		for(int i = 0; i<s.length(); i++){
			//System.out.println(sToChars[i]);
			sleep();
			if(isCapped(sToChars[i])){
				r.keyPress(KeyEvent.VK_SHIFT);
				r.keyPress(hmap.get(currentChar));
				r.keyRelease(KeyEvent.VK_SHIFT);
			}else r.keyPress(hmap.get(sToChars[i]));
		}
	}
	
//	private void test() throws InterruptedException{
//		Thread.sleep(1000);
//		setCharsPerMinute(200);
//		while(true){
//			r.keyPress(KeyEvent.VK_CAPS_LOCK);
//			r.keyRelease(KeyEvent.VK_CAPS_LOCK);
//			r.keyPress(KeyEvent.VK_M);
//			r.keyPress(KeyEvent.VK_CAPS_LOCK);
//			r.keyRelease(KeyEvent.VK_CAPS_LOCK);
//			
//			sleep();
//			r.keyPress(KeyEvent.VK_A);
//			sleep();
//			r.keyPress(KeyEvent.VK_T);
//			sleep();
//			r.keyPress(KeyEvent.VK_SPACE);
//			sleep();
//		}
//	}
	
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
