package positions;

/**
 * Beschreibt eine Position in mm im Koordinatensystem.
 */
public class Position3D extends Position2D {

	private boolean z;

	public Position3D(double x, double y, boolean z) {
		super(x, y);
		this.z = z;
	}

	public Position3D(Position2D position2d, boolean z) {
		this(position2d.getX(), position2d.getY(), z);
	}

	public boolean isZ() {
		return z;
	}

}
