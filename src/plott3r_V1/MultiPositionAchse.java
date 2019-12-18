package plott3r_V1;

import lejos.hardware.port.Port;

public class MultiPositionAchse extends Achse {

	public MultiPositionAchse(Sensor sensor, Port port, Einbaurichtung einbaurichtung, Reifen letzteEinheit, IUebersetzung... uebersetzungsEinheiten) {
		super(sensor, port, einbaurichtung, letzteEinheit, uebersetzungsEinheiten);
	}

	public void backward() {
		if (this.isAntriebsumkehrung()) 
			this.getMotor().forward();
		else 
			this.getMotor().backward();
	}

	public void backward(long timeInMillis) throws InterruptedException {
		this.backward();
		Thread.sleep(timeInMillis);
		this.stop();
	}

	public void forward() {
		if (this.isAntriebsumkehrung()) 
			this.getMotor().backward();
		else 
			this.getMotor().forward();
	}

	public void forward(long timeInMillis) throws InterruptedException {
		this.forward();
		Thread.sleep(timeInMillis);
		this.stop();
	}

	public double getPositionFromTachoCount() {
		final double gearWheelRatio = this.getUebersetzungsverhaeltnis();
		final double umfang = this.antriebsEinheit.getUmfang();
		final int tachoCount = this.getTachoCount();
		double mm = (tachoCount * umfang) / (gearWheelRatio * 360);
		if (this.getMotor().getEinbaurichtung() == Einbaurichtung.UMGEKEHRT)
			mm = mm * -1;
		return mm;
	}

	public int getTachoCount() {
		return this.getMotor().getTachoCount();
	}

	public void resetTachoCount() {
		this.getMotor().resetTachoCount();
	}

	public void rotateMm(double mm) {
		double rotation = ((mm / (this.antriebsEinheit.getDurchmesser() * Math.PI)) * (double)this.getUebersetzungsverhaeltnis() * 360);
		
		if (this.isAntriebsumkehrung())
			rotation = -rotation;
		
		this.getMotor().rotate((int) rotation);
	}
	
	protected void synchronizeWith(MultiPositionAchse a) {
		this.getMotor().synchronizeWith(a.getMotor());
	}
	
	protected void startSynchronization() {
		this.getMotor().startSynchronization();
	}
	
	protected void endSynchronization() {
		this.getMotor().endSynchronization();
	}

	protected void waitComplete() {
		this.getMotor().waitComplete();
	}
}
