package ru.kpfu.itis.frenelzone;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Main {
	
	public static double  SPEED_IN_AIR= 2999792438.0;
	public static int  LITE_SPEED_IN_AIR = 3000; //ÌÃÖ
	
	public double getLengthOnda(double frequency){
		return SPEED_IN_AIR/frequency;
	}
	public double getLiteLengthOnda(double frequency){
		return LITE_SPEED_IN_AIR/(frequency);
	}
	
	public double getRadFren(double d1,double d2,double frequency){
		return Math.sqrt((d1*d2*this.getLengthOnda(frequency))/(d1+d2));
	}
	
	public double getHeightAntenn(double hAnt1,double distance,double frequency, Interference inter){
		if(inter.distance>distance){
			return 0;
		}
		if(inter.distance==distance){
			return inter.height; 
		}
		double heightAnt2;
		double radFren=this.getRadFren(inter.getDistance(), distance-inter.getDistance(), frequency);
		double izRadFren=radFren*0.8;
		double h=inter.height+izRadFren-hAnt1;
		heightAnt2=hAnt1+ (h*distance/inter.distance);
		System.out.println("radFren = "+radFren+" heigth antenna 2 ="+heightAnt2+" height inter ="+inter.height+" inter distance = "+inter.distance);
		return heightAnt2;
	}
	public double max(double x1,double x2){
		return x1>x2 ? x1 : x2;
	}
	
	
	/**
	 * @param hAnt1 - height of antenna ¹1, measured in meters
	 * @param distance - distance without antennas, measured in meters
	 * @param frequency - measured in Gertz
	 * @param interference  - object interference have properties height and distance from antenna 1
	 * @return
	 */
	public double getHeightAntenn(double hAnt1,double distance,double frequency, List<Interference> interference){
		Iterator<Interference> iter = interference.iterator();
		double maxHeight=-1;
		while(iter.hasNext()){
			maxHeight=this.max(maxHeight,this.getHeightAntenn(hAnt1, distance, frequency, iter.next()));
		}
		return maxHeight;
	}
	
	public static void main(String[] args) {
		List<Interference> list = new LinkedList<>();
		list.add(new Interference(5,10));
		list.add(new Interference(6,15));
		list.add(new Interference(10,30));
		list.add(new Interference(15,40));
		list.add(new Interference(125,150));
		double frequency = 3_000_000_000.0;
		Main logic = new Main();
		System.out.println(logic.getHeightAntenn(2, 100, frequency, list));
	}
	
}
