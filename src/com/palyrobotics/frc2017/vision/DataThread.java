package com.palyrobotics.frc2017.vision;

import com.palyrobotics.frc2017.config.Constants;
import com.palyrobotics.frc2017.vision.util.VisionUtil;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DataThread extends DataReceiverBase {

	protected DataThread() {
		super("Data Thread", Constants.kVisionDataFileName,Constants.kVisionDataPort, Constants.kAndroidDataSocketUpdateRate, false);
	}

	private static DataThread s_instance;

	public static DataThread getInstance() {

		if (s_instance == null)
			s_instance = new DataThread();

		return s_instance;
	}

	@Override
	protected void update() {
		
		try {
			String raw_data = mReceiverSelector.getReceiver().extractData();
			JSONObject json = VisionUtil.parseJSON(raw_data);

			if (json != null) {
				String state = (String)json.get("state");
				if (!(state == null) && !state.equals("")) {	// Handle based on state
					switch (state) {

						case "STREAMING": {

							// Get image data
							final double
									data_x = Double.parseDouble((String)json.get("x_displacement")),
									data_z = Double.parseDouble((String)json.get("z_displacement"));

							VisionData.setXDataValue(data_x);
							VisionData.setZDataValue(data_z);

							break;
						}

						case "STOPPED":
						case "PAUSED": {

							VisionData.getXData().setToDefault();
							VisionData.getZData().setToDefault();

							break;
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	protected void tearDown() {

	}

}
