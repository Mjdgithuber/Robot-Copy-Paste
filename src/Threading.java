
public class Threading extends Thread {
	
	FrameSetup frame;
	
	public Threading(FrameSetup f){
		frame = f;
	}
	
	public void run(){
		KeySimulator keySim = new KeySimulator();
		keySim.start();
		frame.endThread();
	}
}
