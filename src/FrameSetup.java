import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameSetup extends JFrame{
	
	boolean isRunning = false;
	Threading t;
	JButton runButton, optionsButton;
	
	JPanel panelMain = new JPanel();
	JPanel panelOptions = new JPanel();
	
	public FrameSetup(){
		setTitle("Robot Copy Paste");
		setSize(378, 382);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JButton help = new JButton("Help");
		JButton run = new JButton("Copy The Text You Want And Click Here");
		JButton options = new JButton("Options");
		
		panelMain.setLayout(new BorderLayout());
		panelMain.add(help, BorderLayout.NORTH);
		panelMain.add(run, BorderLayout.CENTER);
		panelMain.add(options, BorderLayout.SOUTH);
		add(panelMain);
		
		JButton plusOne;
		JButton plusTen;
		JButton minusOne;
		JButton minusTen;
		
//		run.setLocation(500, 500);
//		
//		panel.setLayout(new FlowLayout(1, 50, 30));
//		panel.add(help);
//		panel.add(run);
//		panel.add(options);
//		
//		add(panel);
		
		
//		help.setSize(100,50);
//		help.setLocation(10, 10);
//		panel.setLayout(null);
	}
	
	public static void main(String[] args){
		FrameSetup main = new FrameSetup();
	}
	

	
	private void setupFrame(FrameSetup motherClass){
		JFrame frame = new JFrame();
		frame.setSize(280, 250);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new FlowLayout());
		
		runButton = new JButton("Copy The Text You Want And Click Here");
		frame.add(runButton, BorderLayout.CENTER);

		optionsButton = new JButton("Options");
		optionsButton.setBackground(Color.CYAN);
		frame.add(optionsButton, BorderLayout.PAGE_END);
		
		addActionList(motherClass);
	}
	
	private void hideMainMenuButtons(){
		runButton.setVisible(false);
		optionsButton.setVisible(false);
	}
	
	private void addActionList(FrameSetup frame){
		runButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!isRunning){
					t = null;
					t = new Threading(frame);
					t.start();
					isRunning = true;
				}
			}
			
		});
		
		optionsButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideMainMenuButtons();
			}
			
		});
	}
	
	public void setIsRunning(boolean b){ isRunning = b; }
	
	public void endThread(){
		try {
			setIsRunning(false);
			//jb.setVisible(true);
			t.join();
		} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

}
