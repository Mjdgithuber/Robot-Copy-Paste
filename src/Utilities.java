public final class Utilities {
	
	private Utilities(){}
	
	public static boolean doesListContain(char[] array, char c){
		for(char n : array){
			if(c == n) return true;
		}
		return false;
	}

}