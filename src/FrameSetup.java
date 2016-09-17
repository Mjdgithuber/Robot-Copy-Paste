import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameSetup extends JPanel{
	
	boolean isRunning = false;
	Threading t;
	JButton runButton, optionsButton;
	
	public static void main(String[] args){
		FrameSetup frame = new FrameSetup();
		frame.setupFrame(frame);
	}
	
	private void setupFrame(FrameSetup motherClass){
		JFrame frame = new JFrame();
		frame.setSize(280, 250);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		
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
