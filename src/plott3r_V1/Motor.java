package plott3r_V1;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

public class Motor {

	private Einbaurichtung einbaurichtung;

	private RegulatedMotor regulatedMotor;

	public Motor(Port port, Einbaurichtung einbaurichtung) {
		regulatedMotor = new EV3LargeRegulatedMotor(port);
		this.einbaurichtung = einbaurichtung;
	}
	
	protected void synchronizeWith(Motor m) {
		this.regulatedMotor.synchronizeWith(new RegulatedMotor[] {m.regulatedMotor});
	}
	
	protected void startSynchronization() {
		this.regulatedMotor.startSynchronization();
	}
	
	protected void endSynchronization() {
		this.regulatedMotor.endSynchronization();
	}

	protected void waitComplete() {
		this.regulatedMotor.waitComplete();
	}
	
	public void backward() {
		if (einbaurichtung == Einbaurichtung.UMGEKEHRT) {
			this.regulatedMotor.forward();
			return;
		}
		this.regulatedMotor.backward();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		regulatedMotor.close();
	}

	public void forward() {
		if (einbaurichtung == Einbaurichtung.UMGEKEHRT) {
			this.regulatedMotor.backward();
			return;
		}
		this.regulatedMotor.forward();
	}

	protected Einbaurichtung getEinbaurichtung() {
		return einbaurichtung;
	}

	public int getTachoCount() {
		return this.regulatedMotor.getTachoCount();
	}

	public void resetTachoCount() {
		this.regulatedMotor.resetTachoCount();
	}

	public void rotate(int grad) {
		this.regulatedMotor.rotate(grad, true);
	}

	public void rotateTo(int limitAngle) {
		this.regulatedMotor.rotateTo(limitAngle);

	}

	public void setSpeed(int speed) {
		this.regulatedMotor.setSpeed(speed);
	}

	public void stop() {
		regulatedMotor.stop();
	}

}
