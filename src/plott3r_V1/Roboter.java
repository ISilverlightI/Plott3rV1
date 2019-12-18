package plott3r_V1;

import lejos.hardware.Sound;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import positions.Position2D;
import positions.Position3D;

public class Roboter {
	public static void main(String args[]) {
		try {
			Roboter roboter = new Roboter();
			Sound.beep();
			roboter.moveToHomePosition();

			Sound.twoBeeps();
			
			Position2D position = new Position2D(100, 50);
			Position2D position2 = new Position2D(50, 100);
			
			for (int i=0; i<20; i+=2) {
			System.out.println("");
			System.out.println("Round " + i + ":");
			roboter.moveToPosition(position, 50);
			System.out.println("");
			System.out.println("Round " + (i+1) + ":");
			roboter.moveToPosition(position2, 50);
			}
			
			Sound.twoBeeps();
			
			roboter.moveToHomePosition();
			Sound.beep();
			
//			Sound.beep();
//			Delay.msDelay(1000);
//			Sound.beep();
//			
//			roboter.bereitePapierVor();
//
//			Delay.msDelay(1000);
//			roboter.entfernePapier();
//			roboter.moveToHomePosition();
			Sound.twoBeeps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Position3D currentPosition;

	private MultiPositionAchse xAchse = new MultiPositionAchse(new TouchSensor(SensorPort.S2), MotorPort.A, Einbaurichtung.UMGEKEHRT, new Reifen(40.0), new Zahnradsatz(new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_GROSS), new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_KLEIN)));
	private MultiPositionAchse yAchse = new MultiPositionAchse(new LichtSensor(SensorPort.S1), MotorPort.D, Einbaurichtung.UMGEKEHRT, new Reifen(43.2), new Zahnradsatz(new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_GROSS), new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_KLEIN)));
	private DualPositionAchse zAchse = new DualPositionAchse(null, MotorPort.C, Einbaurichtung.REGULAER, null, null);

	public Roboter() {

	}

	private void bereitePapierVor() throws InterruptedException {
		// TODO: auf funktion testen!
		if(zAchse.isAktiv())
			zAchse.deaktiviere();
		
		getYAchse().setSpeed(25);
		
		while(!getYAchse().getSensor().isAktiv()) {
			getYAchse().backward();
		}
		getYAchse().stop();
		
		while(getYAchse().getSensor().isAktiv()) {
			getYAchse().forward();
		}
		getYAchse().stop();
	}

	private void entfernePapier() throws InterruptedException {
		zAchse.deaktiviere();
		yAchse.setSpeed(Integer.MAX_VALUE);
		yAchse.forward(2000);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.exit(0);
	}

	public Position3D getCurrentPosition() {
		return this.currentPosition;
	}

	public MultiPositionAchse getXAchse() {
		return this.xAchse;
	}

	public MultiPositionAchse getYAchse() {
		return this.yAchse;
	}

	protected void moveToHomePosition() throws InterruptedException {
		//TODO nicht vergessen!
		bereitePapierVor();
		
		getXAchse().setSpeed(25);
		
		while(!getXAchse().getSensor().isAktiv()) {
			getXAchse().forward();
		}
		getXAchse().stop();
		
		while(getXAchse().getSensor().isAktiv()) {
			getXAchse().backward();
		}
		getXAchse().stop();
		currentPosition = new Position3D(new Position2D(0, 0), zAchse.isAktiv());
		resetTachoCounts();
	}

	private void moveToPosition(Position2D position2D, int mmSec) throws InterruptedException {
		moveToPosition(new Position3D(position2D, zAchse.isAktiv()), mmSec);
	}

	private void moveToPosition(Position3D position, int mmSec) throws InterruptedException {
		if(position.isZ())
			zAchse.aktiviere();
		else
			zAchse.deaktiviere();
		
		getXAchse().synchronizeWith(getYAchse());
		
		getXAchse().setSpeed(mmSec);
		getYAchse().setSpeed(mmSec);
		
		getXAchse().startSynchronization();
		System.out.println("pos.getX: " + position.getX());
		System.out.println("curr-pos.getX: " + currentPosition.getX());
		getXAchse().rotateMm(position.getX() - currentPosition.getX());
		System.out.println("pos.getY: " + position.getY());
		System.out.println("curr-pos.getY: " + currentPosition.getY());
		getYAchse().rotateMm(position.getY() - currentPosition.getY());
		getXAchse().endSynchronization();
		
		getXAchse().waitComplete();
		getYAchse().waitComplete();
		
		currentPosition = new Position3D(new Position2D(getXAchse().getPositionFromTachoCount(), getYAchse().getPositionFromTachoCount()), zAchse.isAktiv());
	}

	private void resetTachoCounts() {
		this.xAchse.resetTachoCount();
		this.yAchse.resetTachoCount();
		if (xAchse.getTachoCount() != 0 || yAchse.getTachoCount() != 0)
			throw new RuntimeException("Konnte Tachocount nicht zurücksetzen");
	}

	public void stop() {
		xAchse.stop();
		yAchse.stop();
		zAchse.stop();
	}

}
