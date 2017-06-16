package model;

import java.util.Random;

public class Manchot {

	private double _esperance;
	private double _variance;
	
	public Manchot(double p_esperance, double p_variance) {
		_esperance = p_esperance;
		_variance = p_variance;
	}
	
	/** Getters and Setters **/
	
	public double get_esperance() {
		return _esperance;
	}
	public void set_esperance(double _esperance) {
		this._esperance = _esperance;
	}
	public double get_variance() {
		return _variance;
	}
	public void set_variance(double _variance) {
		this._variance = _variance;
	}
	
	/** End Getters and Setters	**/
	
	public double tirerBras() {
		
		double gain = 0;
		Random r = new Random();
		
		double random1 = r.nextDouble();
		double random2 = r.nextDouble();
		
		gain = _variance * Math.sqrt(-2.0 * Math.log(random1)) * Math.cos(2 * Math.PI * random2) + _esperance;
		
		return gain;
	}
	
}
