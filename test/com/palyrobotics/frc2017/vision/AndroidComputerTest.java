package com.palyrobotics.frc2017.vision;

/**
 * Created by Alvin on 2/15/2017.
 */
public class AndroidComputerTest {

	public static void main(String[] args) throws InterruptedException {
		AndroidConnectionHelper.getInstance().start(true);
//		AndroidConnectionHelper.getInstance().StartVisionApp();
//		String inp = RuntimeExecutor.getInstance().exec("adb devices");
//		String[] lines = inp.split("\\n");
//		boolean hasDevice = false;
//		for(int i=1; i<lines.length && !hasDevice; i++){
//			hasDevice = lines[i].contains("device");
//		}
//		System.out.println(inp);
//		System.out.println(hasDevice);

		while(true){
			if (AndroidConnectionHelper.getInstance().isAppStarted()) {
				System.out.println(AndroidConnectionHelper.getInstance().getXDist());
			}
			Thread.sleep(500);
		}
	}
}
