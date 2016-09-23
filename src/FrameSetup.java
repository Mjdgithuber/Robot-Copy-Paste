import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrameSetup extends JFrame{
	
	private KeySimulator keySim = new KeySimulator();
	
	private JButton help, run, options, 
			plusOne, plusTen, plusOneHundred, minusOne, minusTen, minusOneHundred, goBackToMainMenu;
	private JLabel charsPerMinute;
	
	private JPanel panelMain = new JPanel();
	private JPanel panelOptions = new JPanel();
	private JPanel parentPanel = new JPanel();
	
	private boolean isRunning = false;
	
	static FrameSetup main;
	
	public FrameSetup(){
		/***********************************************
		* This sets some default values for the JFrame *
		***********************************************/
		setTitle("Robot Copy Paste");
		setSize(378, 382);
		setVisible(true);
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
		options = new JButton("Options");
		
		panelMain.setLayout(new BorderLayout());
		panelMain.add(help, BorderLayout.NORTH);
		panelMain.add(run, BorderLayout.CENTER);
		panelMain.add(options, BorderLayout.SOUTH);
		
		/***************************************************
		* This is setting up the panel for the options menu*
		****************************************************/
		plusOne = new JButton("+");
		setSizeAndLocation(plusOne, 195, 40, 50, 20);
		
		plusTen = new JButton("++");
		setSizeAndLocation(plusTen, 250, 40, 50, 20);
		
		plusOneHundred = new JButton("+++");
		setSizeAndLocation(plusOneHundred, 305, 40, 60, 20);
		
		minusOne = new JButton("-");
		setSizeAndLocation(minusOne, 117, 40, 45, 20);
		
		minusTen = new JButton("--");
		setSizeAndLocation(minusTen, 65, 40, 45, 20);
		
		minusOneHundred = new JButton("---");
		setSizeAndLocation(minusOneHundred, 5, 40, 55, 20);
		
		goBackToMainMenu = new JButton("Back");
		setSizeAndLocation(goBackToMainMenu, (getWidth()-70)/2, 315, 70, 30);
		
		charsPerMinute = new JLabel(""+keySim.getCharsPerMinute());
		setSizeAndLocation(charsPerMinute, (getWidth()-50)/2, 25, 50, 50);
		
		panelOptions.setLayout(null);
		panelOptions.add(plusOne);
		panelOptions.add(plusTen);
		panelOptions.add(plusOneHundred);
		panelOptions.add(minusOne);
		panelOptions.add(minusTen);
		panelOptions.add(minusOneHundred);
		panelOptions.add(goBackToMainMenu);
		panelOptions.add(charsPerMinute);
		
//		add(panelMain);
		add(panelOptions);

		addListeners();
	}
	
	private void setSizeAndLocation(JComponent comp, int x, int y, int width, int height){
		comp.setLocation(x, y);
		comp.setSize(width, height);
	}
	
	private void addListeners(){
		/*******************
		* Main Menu Buttons*
		*******************/
		run.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!isRunning){
					isRunning = true;
					keySim.start();
					isRunning = false;
				}
		}});
		
		options.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePanel(panelMain, panelOptions);
		}});
		
		/**********************
		* Options Menu Buttons*
		**********************/
		plusOne.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()+1);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		plusTen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()+10);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		plusOneHundred.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()+100);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		minusOne.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()-1);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		minusTen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()-10);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		minusOneHundred.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				keySim.setCharsPerMinute(keySim.getCharsPerMinute()-100);
				charsPerMinute.setText(""+keySim.getCharsPerMinute());
		}});
		
		goBackToMainMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePanel(panelOptions, panelMain);
		}});
	}
	
	private void changePanel(JPanel fromPanel, JPanel toPanel){
		remove(fromPanel);
		add(toPanel);
		revalidate();
		repaint();
	}
	
	public void setIsRunning(boolean b){ isRunning = b; }

}
