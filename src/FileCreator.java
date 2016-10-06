import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileCreator {
	
	//TODO delete this because it was updated for the new file creator methods 
	//private Translator translator = new Translator();
//	public void createAndWriteFile(){
//		try{
//			String s = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
//			//s = translator.translate(s);
//			
//			File file = new File("robotCopy.txt");
//			file.createNewFile();
//			
//			PrintWriter writer = new PrintWriter("robotCopy.txt", "UTF-8");
//			writer.println(s);
//			writer.close();
//		}catch (Exception e){System.out.println("Something is wrong with the creation of file");}
//	}
	
	public boolean doesFileExist(String path){
		File file = new File(path+".txt");
		
		if(file.exists()) return true;
		return false;
	}
	
	/**
	 * This method allows you to write a string array to a file
	 * @param path The path of the file you are writing to
	 * @param lines Each string in the array is going to be written as one line
	 */
	public void writeToFile(String path, String[] lines){
		try {
			PrintWriter writer = new PrintWriter(path+".txt", "UTF-8");
			for(int i = 0; i<lines.length; i++) writer.println(lines[i]);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method writes a single line to a file
	 * @param path This is the path of the file
	 * @param line this is the line you want to write to the file
	 */
	public void writeToFile(String path, String line){
		try {
			PrintWriter writer = new PrintWriter(path+".txt", "UTF-8");
			writer.println(line);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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