import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileCreator {
	
	private Translator translator = new Translator();
	
	public void createAndWriteFile(){
		try{
			String s = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			s = translator.translate(s);
			
			File file = new File("robotCopy.txt");
			file.createNewFile();
			
			PrintWriter writer = new PrintWriter("robotCopy.txt", "UTF-8");
			writer.println(s);
			writer.close();
		}catch (Exception e){System.out.println("Something is wrong with the creation of file");}
	}
	
	public boolean doesFileExist(String path){
		File file = new File(path+".txt");
		
		if(file.exists()) return true;
		return false;
	}
	
	public void writeToFile(String path, String[] values){
		try {
			PrintWriter writer = new PrintWriter(path+".txt", "UTF-8");
			for(int i = 0; i<values.length; i++) writer.println(values[i]);
			writer.close();
		} catch (Exception e) {}
	}
	
	public String[] readFromFile(String path){
		try {
			int arraySize = 0;
			
			//This is to find out how long the file is to determine how big the array sent back should be
			BufferedReader br = new BufferedReader(new FileReader(path+".txt"));
			while(br.readLine() != null){
				arraySize++;
			}
			br.close();
			
			br = new BufferedReader(new FileReader(path+".txt"));
			String[] allLines = new String[arraySize];
			String currentLine;
			int location = 0;
			while((currentLine = br.readLine()) != null){
				allLines[location] = currentLine;
				location++;
			}
			br.close();
			
			return allLines;
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public void createFile(String path){
		try {
			File file = new File(path+".txt");
			file.createNewFile();
		}catch (IOException e) {e.printStackTrace();}
	}
}