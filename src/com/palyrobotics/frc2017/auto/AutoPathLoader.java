package com.palyrobotics.frc2017.auto;

import com.palyrobotics.frc2017.util.archive.team254.TextFileReader;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.io.TextFileDeserializer;

import java.io.File;
import java.util.Hashtable;

/**
 * Load all autonomous mode paths.
 * Revised for Team 8 2017 by Nihar
 * @author Jared341
 * @author Stephen Pinkerton
 */
public class AutoPathLoader {
	// Make sure these match up!
	public final static String[] kPathNames = {
			"Baseline",
			"BlueBoiler",
			"BlueBoilerVision",
			"BlueCenter",
			"BlueCenterVision",
			"BlueLoading",
			"BlueLoadingVision",
			"RedBoiler",
			"RedBoilerVision",
			"RedCenter",
			"RedCenterVision",
			"RedLoading",
			"RedLoadingVision",
	};

	public final static String[] kPathDescriptions = {
			"Baseline",
			"Blue boiler",
			"Blue boiler vision",
			"Blue center",
			"Blue center vision",
			"Blue loading",
			"Blue loading vision",
			"Red boiler",
			"Red boiler vision",
			"Red center",
			"Red center vision",
			"Red loading",
			"Red loading vision",
	};

	static Hashtable paths_ = null;

	public static void loadPaths() {
		// Only run once
		if (paths_ == null) {
			paths_ = new Hashtable();
		} else {
			return;
		}
		double startTime = System.currentTimeMillis();
		String os = System.getProperty("os.name");
		String sourceDir;
		if (os.startsWith("Mac") || os.startsWith("Windows")) {
			sourceDir = "."+File.separatorChar+"paths"+File.separatorChar;
		} else {
			// Pray that this is a roborio because I don't know a programmer using Linux
			sourceDir = "/home/lvuser/paths/";
		}

		TextFileDeserializer deserializer = new TextFileDeserializer();
		for (int i = 0; i < kPathNames.length; ++i) {
			TextFileReader reader = new TextFileReader(sourceDir + kPathNames[i] +
					".txt");

			Path path = deserializer.deserialize(reader.readWholeFile());
			paths_.put(kPathNames[i], path);
		}
		System.out.println("Parsing paths took: " + (System.currentTimeMillis()-startTime/1000));
	}

	public static Path get(String name) {
		return (Path) paths_.get(name);
	}

	public static Path getByIndex(int index) {
		return (Path) paths_.get(kPathNames[index]);
	}
}