import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FrameSetup extends JFrame{
	
	private KeySimulator keySim = new KeySimulator();
	
	private JButton help, run, settings, 
			plusOne, plusTen, plusOneHundred, minusOne, minusTen, minusOneHundred, goBackToMainMenu;
	private JLabel charsPerMinute, charsPerMinuteTitle,
			fullNameTitle, poemTitle, authorTitle, chanceForErrorTitle, mustEnterNumberError, percentSign;
	private JTextField fullName, poem, author, chanceForError;
	
	private ArrayList<JComponent> optionMenuObjects = new ArrayList<JComponent>();
	private ArrayList<JButton> plusMinusButtons = new ArrayList<JButton>();
	
	private JPanel panelMain = new JPanel();
	private JPanel panelSettings = new JPanel();
	
	private boolean isRunning = false, firstTimeRunning = false;
	
	static FrameSetup main;
	
	public FrameSetup(){
		
		/******************************************************
		* This creates the necessary files need to run program*
		******************************************************/
		if(!keySim.fileCreator.doesFileExist("Settings") || !keySim.fileCreator.doesFileExist("Paste Dump")){
			keySim.fileCreator.createFile("Paste Dump");
			
			//Creates the settings file
			keySim.fileCreator.createFile("Settings");
			String[] values = {"Chars_Per_Minute=250", "Name=", "Poem_Title=", "Author=", "Chance_For_Error=3"};
			
			keySim.fileCreator.writeToFile("Settings", values);
			firstTimeRunning = true;
		}
		
		/***********************************************
		* This sets some default values for the JFrame *
		***********************************************/
		setTitle("Robot Copy Paste");
		setSize(378, 382);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		addGui();
	}
	
	public static void main(String[] args){
		main = new FrameSetup();
	}
	
	private void addGui(){
		
		/************************************************
		* This is setting up the panel for the main menu*
		************************************************/
		help = new JButton("Help");
		run = new JButton("Copy The Text You Want And Click Here");
		settings = new JButton("Settings");
		
		panelMain.setLayout(new BorderLayout());
		panelMain.add(help, BorderLayout.NORTH);
		panelMain.add(run, BorderLayout.CENTER);
		panelMain.add(settings, BorderLayout.SOUTH);
		
		
		
		/****************************************************
		* This is setting up the panel for the settings menu*
		*****************************************************/
		//Setting up the chars per minute buttons
		plusOne = new JButton("+");setBounds(plusOne, 195, 40, 50, 20);plusMinusButtons.add(plusOne);
		plusTen = new JButton("++");setBounds(plusTen, 250, 40, 50, 20);plusMinusButtons.add(plusTen);
		plusOneHundred = new JButton("+++");setBounds(plusOneHundred, 305, 40, 60, 20);plusMinusButtons.add(plusOneHundred);
		minusOne = new JButton("-");setBounds(minusOne, 117, 40, 45, 20);plusMinusButtons.add(minusOne);
		minusTen = new JButton("--");setBounds(minusTen, 67, 40, 45, 20);plusMinusButtons.add(minusTen);
		minusOneHundred = new JButton("---");setBounds(minusOneHundred, 7, 40, 55, 20);plusMinusButtons.add(minusOneHundred);
		
		//This is for the save button (to save settings)
		goBackToMainMenu = new JButton("Save");setBounds(goBackToMainMenu, (getWidth()-70)/2, 315, 70, 30);
		
		charsPerMinuteTitle = new JLabel("Set How Many Characters Per Minute");setBounds(charsPerMinuteTitle, ((getWidth()-220)/2)+4, 0, 215, 50);
		
		charsPerMinute = new JLabel(""+keySim.getCharsPerMinute());setBounds(charsPerMinute, ((getWidth()-50)/2)+4, 25, 50, 50);
		
		//Setting up the personalized user options
		fullNameTitle = new JLabel("Full Name:");setBounds(fullNameTitle, 8, 70, 150, 25);
		fullName = new JTextField(1);setBounds(fullName, 75, 70, 150, 25);
		
		poemTitle = new JLabel("Poem:");setBounds(poemTitle, 8, 110, 150, 25);
		poem = new JTextField(1);setBounds(poem, 75, 110, 150, 25);

		authorTitle = new JLabel("Author:");setBounds(authorTitle, 8, 150, 150, 25);
		author = new JTextField(1);setBounds(author, 75, 150, 150, 25);
		
		chanceForErrorTitle = new JLabel("% Error:");setBounds(chanceForErrorTitle, 8, 190, 150, 25);
		chanceForError = new JTextField(1);setBounds(chanceForError, 75, 190, 35, 25);
		mustEnterNumberError = new JLabel("*You Must Enter A Number Here");setBounds(mustEnterNumberError, 125, 190, 180, 25);
		mustEnterNumberError.setForeground(Color.red);mustEnterNumberError.setVisible(false);
		percentSign = new JLabel("%");setBounds(percentSign, 113, 190, 15, 25);
		
		//This loads the settings from the file (On the first run these will be preset)
		String[] stringValues = keySim.fileCreator.readFromFile("Settings");
		keySim.setCharsPerMinute(Integer.parseInt(stringValues[0].split("=", 2)[1]));changeCPM(0);//Change cpm updates the label
		fullName.setText(stringValues[1].split("=", 2)[1]);
		poem.setText(stringValues[2].split("=", 2)[1]);
		author.setText(stringValues[3].split("=", 2)[1]);
		chanceForError.setText(stringValues[4].split("=", 2)[1]);keySim.typoError.setChanceForError((Double.parseDouble(chanceForError.getText())/100));
		
		
		
		//Sets the panel's layout to null so that objects locations can be manually input
		panelSettings.setLayout(null);
		for(int i = 0; i<optionMenuObjects.size(); i++) panelSettings.add(optionMenuObjects.get(i));//Adds all of the objects aforementioned
		addListeners();//Adds all of the button listeners
		
		//Will send the user to the settings page if this is the first time they have loaded the program, or will send them to the main menu
		if(firstTimeRunning) add(panelSettings);
		else add(panelMain);
		
		setVisible(true);
	}
	
	private void setBounds(JComponent comp, int x, int y, int width, int height){
		comp.setBounds(x, y, width, height);
		optionMenuObjects.add(comp);
	}
	
	private void addListeners(){
		/*******************
		* Main Menu Buttons*
		*******************/
		run.addActionListener(new ActionListener(){//Run is the main big button used to start the main function
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!isRunning){
					Thread t = new Thread(){
						public void run(){
							isRunning = true;
							try{keySim.start();}
							catch (Exception e){}
							isRunning = false;
						}
					};
					t.start();
				}
		}});
		
		settings.addActionListener(new ActionListener(){//This is the settings button at the bottom of the menu
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePanel(panelMain, panelSettings);
		}});
		
		
		
		/**************************
		* Chars Per Minute Buttons*
		**************************/
		
		plusOne.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(1);}});
		plusTen.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(10);}});
		plusOneHundred.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(100);}});
		minusOne.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(-1);}});
		minusTen.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(-10);}});
		minusOneHundred.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent arg0) {changeCPM(-100);}});
		
		/*************
		* Save Button*
		*************/
		goBackToMainMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					double errorChance = Double.parseDouble(chanceForError.getText());//This makes sure that the text in the percent error box is a number (not words)
					keySim.typoError.setChanceForError((errorChance/100));
					
					if(mustEnterNumberError.isVisible())mustEnterNumberError.setVisible(false);//This will turn the warning off
					
					//This is an array containing all of the users settings from this page
					String[] toSettingsFile = {"Chars_Per_Minute="+keySim.getCharsPerMinute(), "Name="+fullName.getText(), "Poem_Title="+poem.getText(), 
							"Author="+author.getText(), "Chance_For_Error="+chanceForError.getText()};
					
					keySim.fileCreator.writeToFile("Settings", toSettingsFile);//This writes the string to the settings files
					
					changePanel(panelSettings, panelMain);//Goes back to the main menu
				}catch(Exception e){mustEnterNumberError.setVisible(true);}
		}});
	}
	
	/**
	 * This method changes the current chars per minute and updates the label and the varible in the key simulator class
	 * @param dX This is what you want to change the chars per minute by (for instance changeCPM(1); will add one to the chars per minute)
	 */
	private void changeCPM(int dX){
		int newCPM = keySim.getCharsPerMinute()+dX;
		if(!(newCPM < 100) && !(newCPM > 2000)){
			keySim.setCharsPerMinute(newCPM);//Sets the new CPM in the key simulator class
			charsPerMinute.setText(""+keySim.getCharsPerMinute());//Changes the label text
			
			//This part will change the location of the label to appear like it is centered (because no layout is being used)
			if(keySim.getCharsPerMinute()<1000) charsPerMinute.setLocation(((getWidth()-50)/2)+4, 25);
			else charsPerMinute.setLocation((getWidth()-50)/2, 25);
		}
	}
	
	/**
	 * This method changes the current JPanel by removing the first parameter from the JFrame, and then adding the second parameter to the JFrame
	 * @param fromPanel The panel that you want to change from
	 * @param toPanel The panel you want to go to
	 */
	private void changePanel(JPanel fromPanel, JPanel toPanel){
		remove(fromPanel);
		add(toPanel);
		revalidate();
		repaint();
	}
	
	public void setIsRunning(boolean b){ isRunning = b; }

}
