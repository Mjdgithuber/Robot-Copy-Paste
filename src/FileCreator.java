import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.PrintWriter;

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

}
