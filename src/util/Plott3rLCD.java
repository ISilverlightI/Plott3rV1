package util;

import lejos.hardware.lcd.LCD;

public class Plott3rLCD {

	private static int row = 1;

	public static void drawString(String message) {
		// LCD.clearDisplay();
		// LCD.clear();
		LCD.drawString(message, 1, row++);
	}

}
