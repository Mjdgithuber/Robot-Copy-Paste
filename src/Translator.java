import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Translator {
	
	private KeySimulator keySim;
	
	private HashMap<Character, Character> hm = new HashMap<Character, Character>();
	
	public Translator(KeySimulator keySimulator){
		keySim = keySimulator;
		fillHashMap();
	}
	
	private void fillHashMap(){
		addChars("”“", '"');
		addChars("’‘", '\'');
		addChars("…", '.');
	}
	
	private void addChars(String s, char c){
		char[] array = s.toCharArray();//Converts the string into chars so they can be added to the hash map
		
		for(int i = 0; i<array.length; i++) hm.put(array[i], c);
	}
	
	public String translate(String s){
		String translatedString = ""; //This is the string that is going to be returned
		char[] charsInString = s.toCharArray();//Converts the string into a char array so it can easily be amended
		
		//This loop goes through all of the chars and converts them into readable one's if they need it (The File Reader can't read all chars)
		for(int i = 0; i<charsInString.length; i++){
			int amountOfTimes = 1;
			if(hm.containsKey(charsInString[i])){
				if(hm.get(charsInString[i]) == '.') amountOfTimes = 3;
				else amountOfTimes = 1;
				charsInString[i] = hm.get(charsInString[i]);
			}

//			try{KeyEvent.getExtendedKeyCodeForChar(charsInString[i]);}
//			catch(Exception e){
//				System.out.println("In here");
//				if(!keySim.doesListContain(keySim.spChars, charsInString[i])) writeChar = false;
//			}
			
			//This adds the translated chars into the main string (this for loop is here in case of the ellipsis which has three periods in a row)
			for(int times = 0; times<amountOfTimes; times++) translatedString += charsInString[i];
		}
		
		return translatedString;
	}
}