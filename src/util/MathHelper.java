package util;

import positions.Position2D;

public class MathHelper {
	public static double getHypothenuse(Position2D p1, Position2D p2) {
		double deltaX = p1.getX() - p2.getX();
		double deltaY = p1.getY() - p2.getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	public static double round(double zahl, int stellen) {
		return (int) zahl + (Math.round(Math.pow(10, stellen) * (zahl - (int) zahl))) / (Math.pow(10, stellen));
	}
}
