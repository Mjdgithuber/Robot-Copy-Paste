import java.util.HashMap;

public class Translator {
	
	HashMap<Character, Character> hm = new HashMap<Character, Character>();

	public Translator(){
		fillHashMap();
	}
	
	private void fillHashMap(){
		addChars("”“", '"');
		addChars("’", '\'');
	}
	
	private void addChars(String s, char c){
		char[] array = s.toCharArray();//Converts the string into chars so they can be added to the hash map
		
		for(int i = 0; i<array.length; i++) hm.put(array[i], c);
	}
	
	public String translate(String s){
		String translatedString; //This is the string that is going to be returned
		char[] charsInString = s.toCharArray();//Converts the string into a char array so it can easily be amended
		
		//This loop goes through all of the chars and converts them into readable one's if they need it (The File Reader can't read all chars)
		for(int i = 0; i<charsInString.length; i++) if(hm.containsKey(charsInString[i])) charsInString[i] = hm.get(charsInString[i]);
		
		translatedString = String.valueOf(charsInString);//This takes the array of chars and converts it back into a string
		
		return translatedString;
	}

}
