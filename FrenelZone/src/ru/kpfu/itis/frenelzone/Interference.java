package ru.kpfu.itis.frenelzone;

public class Interference {
	double height;
	double distance;
	
	public Interference(double height,double distance){
		this.height=height;
		if(distance>1){
			this.distance=distance;
		}
		else{
			this.distance=1;
		}
	}
	public double getDistance(){
		return distance;
	}
	public double getHeight(){
		return height;
	}
}
