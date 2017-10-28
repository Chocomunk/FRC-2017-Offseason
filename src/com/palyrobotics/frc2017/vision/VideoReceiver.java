package com.palyrobotics.frc2017.vision;

import com.palyrobotics.frc2017.config.Constants;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VideoReceiver extends DataReceiverBase{
	
	byte[] image;

	protected VideoReceiver() {

		super("Video Receiver", Constants.kVisionVideoFileName, Constants.kVideoPort, Constants.kAndroidVisionSocketUpdateRate, false);
	}
	
	@Override
	protected void update() {

		ConcurrentLinkedQueue<byte[]> frameQueue = VisionData.getVideoQueue();
		try {
			image = mReceiverSelector.getReceiver().extractDataBytes();
			if (image != null && image.length != 0) {

				// Make sure queue does not get too big
				while (frameQueue.size() > 10)
					frameQueue.remove();

				frameQueue.add(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void tearDown() {

	}
	
	public byte[] getImage() {
		return image;
	}

}
