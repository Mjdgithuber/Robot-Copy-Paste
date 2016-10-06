import java.util.HashMap;

public class Translator {
	
	private HashMap<Character, Character> hm = new HashMap<Character, Character>();
	
	private String availableChars = "1234567890-=!@#$%^&*()_+"//Top line on keyboard
			+ "qwertyuiop[]\\asdfghjkl;'zxcvbnm,./QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?"//Letters and what what not
			+ "”“’‘…" //Special chars
			+ " "//Space 
			+ "\n"//New line char
			+ "\t";//Tab
	
	public Translator(KeySimulator keySimulator){
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
			if(Utilities.doesListContain(availableChars.toCharArray(), charsInString[i])){//This loop makes sure the program supports that specific char
				if(hm.containsKey(charsInString[i])){
					if(hm.get(charsInString[i]) == '.') amountOfTimes = 3;//This checks to see if the char is an ellipsis (in which case it has to draw 3 periods)
					else amountOfTimes = 1;
					charsInString[i] = hm.get(charsInString[i]);
				}
	
				//This adds the translated chars into the main string (this for loop is here in case of the ellipsis which has three periods in a row)
				for(int times = 0; times<amountOfTimes; times++) translatedString += charsInString[i];
			}
		}
		
		return translatedString;
	}
}