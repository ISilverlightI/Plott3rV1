package basics;

import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import util.FileAccess;

/**
 * https://sourceforge.net/p/lejos/wiki/
 * 
 * @author René Gerlach
 *
 */
public class Basics {

	public static void bestimmeHelligkeit(Port port) throws InterruptedException {
		EV3ColorSensor sensor = new EV3ColorSensor(port);
		sensor.setFloodlight(false);
		LCD.drawString("Init", 2, 2);
		LCD.setAutoRefresh(false);
		SensorMode ambientSensorMode = sensor.getAmbientMode();
		float[] sample = new float[ambientSensorMode.sampleSize()];

		for (int i = 0; i < 100; i++) {
			ambientSensorMode.fetchSample(sample, 0);
			LCD.refresh();
			LCD.clear();
			LCD.drawString("Intensity: " + sample[0], 1, 1);
			Thread.sleep(100);
		}
		sensor.close();
	}

	private static void demoTachoCount() {
		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
		int tachoCount = mB.getTachoCount();
		mB.resetTachoCount();
		tachoCount = mB.getTachoCount();
		mB.setSpeed(100);
		mB.rotate(20);
		tachoCount = mB.getTachoCount();
		mB.close();
	}

	public static void dreheMotorFuerXSekunden(float sekunden, Port port) throws InterruptedException {
		RegulatedMotor m = new EV3LargeRegulatedMotor(port);
		m.setSpeed(200);
		m.forward();
		// Thread.sleep((int) (sekunden * 1000));
		Delay.msDelay((int) (sekunden * 1000));
		m.stop();
		m.close();
	}

	public static void dreheMotorUmXGrad(int grad, Port port) {
		RegulatedMotor m = new EV3LargeRegulatedMotor(port);
		m.rotate(grad);
		m.close();
	}

	public static void dreheMotorUmXGradMitMaximalerGeschwindigkeit(int grad, Port port) {
		RegulatedMotor m = new EV3LargeRegulatedMotor(port);
		LCD.drawString(m.getMaxSpeed() + "", 0, 4);
		Delay.msDelay(5000);
		m.setSpeed((int) m.getMaxSpeed());
		m.rotate(grad);
		m.close();
	}

	public static void dreheMotorUmXGradMitYGradProSekunde(int grad, int gradSekunde, Port port) {
		RegulatedMotor m = new EV3LargeRegulatedMotor(port);
		LCD.drawString(m.getMaxSpeed() + "", 0, 4);
		Delay.msDelay(5000);
		m.setSpeed(gradSekunde);
		m.rotate(grad);
		m.close();
	}

	private static void fahreTurmAuf0Punkt() {
		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
		SensorMode sensorMode = touchSensor.getTouchMode();
		float[] sample = new float[sensorMode.sampleSize()];
		sensorMode.fetchSample(sample, 0);
		m.setSpeed(20);
		while (sample[0] == 0) {
			m.forward();
			sensorMode.fetchSample(sample, 0);
		}
		m.stop();
	}

	public static void main(String[] args) throws InterruptedException {
		 dreheMotorUmXGrad(2000, MotorPort.C);
		// dreheMotorFuerXSekunden(2f, MotorPort.B);
		// dreheMotorUmXGradMitMaximalerGeschwindigkeit(1800, MotorPort.A);
		// bestimmeHelligkeit(SensorPort.S3);
		// pruefeTouchSensor();
		// fahreTurmAuf0Punkt();
		// synchroExample();
		// demoTachoCount();
		//playSound();

	}

	public static void playSound() {
		FileAccess fileAccess = new FileAccess(".");
		FileAccess.displayDirectoryContents(fileAccess.getFile());

		// Datei kann über Control Center hochgeladen werden
		int time = Sound.playSample(new File("./tetris-16bit.wav"), 100);
		System.out.println("Time: " + time);

		System.out.println("Done");

	}

	private static void pruefeTouchSensor() {
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S4);
		SensorMode sensorMode = touchSensor.getTouchMode();
		float[] sample = new float[sensorMode.sampleSize()];
		sensorMode.fetchSample(sample, 0);
		while (sample[0] == 0) {
			LCD.drawString("Nicht gedrückt: " + sample[0], 0, 0);
			sensorMode.fetchSample(sample, 0);
			Delay.msDelay(200);
		}
		LCD.drawString("Gedrückt!", 0, 0);
	}

	private static void synchroExample() {
		RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);

		mA.synchronizeWith(new RegulatedMotor[] { mB });

		mA.setSpeed(180);
		mB.setSpeed(180);
		mA.setAcceleration(Integer.MAX_VALUE);
		mB.setAcceleration(Integer.MAX_VALUE);

		mA.startSynchronization();
		// setSpeed nach startSynchronization() wirkungslos!
		mA.rotate(180, true);
		mB.rotate(180, true);
		mA.endSynchronization();

		mA.waitComplete();
		mB.waitComplete();
	}

}
