package ru.kpfu.itis.frenelzone;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Frenel {
	
	public static double  SPEED_IN_AIR= 2999792438.0;
	public static int  LITE_SPEED_IN_AIR = 3000; //МГЦ
	
	public double getLengthOnda(double frequency){
		return SPEED_IN_AIR/frequency;
	}
	public double getLiteLengthOnda(double frequency){
		return LITE_SPEED_IN_AIR/(frequency);
	}
	
	public double getRadFren(double d1,double d2,double frequency){
		return Math.sqrt((d1*d2*this.getLengthOnda(frequency))/(d1+d2));
	}
	
/*	public double getHeightAntenn(double hAnt1,double distance,double frequency, Interference inter){
		double h2=h
	}
*/	
	public double getH2(double hTrans,double hRec,double distance,Interference inter,double frec){
		double startH=hRec;
		double d2=distance-inter.distance,d1=distance-d2;
		double y0,x0,a1,a2,lenOfLine;
		double rad;
		double delta=1;
		int znakDelta=1;
		int kolPrib=5,didPrib=1;
		
		while(true){	//TODO
			y0=hRec*(d1*d2-hRec*inter.height)/(distance*distance+hRec*hRec)  +hTrans;
			x0=y0*distance/hRec;
			a1=Math.sqrt(Math.pow((x0),2)+Math.pow((y0),2));
			a2=Math.sqrt(Math.pow((distance-x0),2)+Math.pow((hRec-y0),2));
			rad=0.6*Math.sqrt((3000/frec)*a1*a2/(a1+a2));
			lenOfLine=Math.sqrt(Math.pow((x0-inter.distance),2)+Math.pow((y0-inter.height),2));
			if(rad<lenOfLine&znakDelta==1||znakDelta==-1&rad>lenOfLine){//TODO
				if(didPrib==kolPrib){
					break;
				}
				else{
					didPrib++;
					znakDelta = znakDelta ==1 ? -1 : 1;
					delta*=0.1;
				}
			}
			hRec+=delta*znakDelta;
		}
		
		return max(startH,hRec);
	}
	
	public static double max(double x1,double x2){
		return x1>x2 ? x1 : x2;
	}
	
	
	/**
	 * @param hAnt1 - height of antenna №1, measured in meters
	 * @param distance - distance without antennas, measured in meters
	 * @param frequency - measured in Gertz
	 * @param interference  - object interference have properties height and distance from antenna 1
	 * @return
	 */
	public double getHeightAntenn(double hAnt1,double hAnt2,double distance,double frequency, List<Interference> interference){
		Iterator<Interference> iter = interference.iterator();
		double maxHeight=hAnt2;
		while(iter.hasNext()){
			//maxHeight=this.max(maxHeight,this.getHeightAntenn(hAnt1, distance, frequency, iter.next()));
			maxHeight=max(maxHeight,this.getH2(hAnt1, hAnt2, distance, iter.next(), frequency));
		}
		return maxHeight;
	}
	
	public static void main(String[] args) {
		//List<Interference> list = new LinkedList<>();
		//list.add(new Interference(30,75));
		//list.add(new Interference(10,50));
		//list.add(new Interference(2,30));
		//list.add(new Interference(2,40));
		//list.add(new Interference(125,150));
		//double frequency = 900_000_000.0;
		Frenel logic = new Frenel();
		//System.out.println(logic.getH2(15, 15, 100, list.iterator().next(), frequency));
		System.out.println(logic.deltaH(100, 210,200, 500,1000, 3000));
		System.out.println(logic.deltaH(15,15,50,75,100,900,10));
	}

	public double deltaH(double hTrans,double hRec,double hInter,double distanseToInter,double distanseWithoutAnten,double frequency,int kPrib){
		double newH=this.retH(hTrans, hRec, hInter, distanseToInter, distanseWithoutAnten, frequency,kPrib);
		if(newH>hRec){return newH-hRec;}
		return 0;
	}
	
	public double deltaH(double hTrans,double hRec,double hInter,double distanseToInter,double distanseWithoutAnten,double frequency){
		return this.deltaH(hTrans, hRec, hInter, distanseToInter, distanseWithoutAnten, frequency,5);
	}
	
	public double retH(double hTrans,double hRec,double hInter,double distanseToInter,double distanseWithoutAnten,double frequency){
		return this.retH(hTrans, hRec, hInter, distanseToInter, distanseWithoutAnten, frequency,5);
	}
	
	public double retH(double hTrans,double hRec,double hInter,double distanseToInter,double distanseWithoutAnten,double frequency,int kPrib){
		double H2=0;
		double Hp=hInter-hTrans,H=hRec-hTrans,d=distanseWithoutAnten,d1=distanseToInter;//подправить определение высоты
		double rastToL=1,a1=d1,a2=d-d1,rad;
		double y0=0,x0=0;
		double i,j;
		double lambda=3_000/frequency;
		double kRaz=1,kMn=0.1,kolIt=0;
		//.out.println("new H 2an = "+H+" new Hp="+Hp+" lambda = "+lambda);
		
		//поднять антену 2 до линии видимости
		if(H<d*Hp/d1){
			H=d*Hp/d1;
		}
		
		//System.out.println("new H2 ="+H);
		
		H-=kRaz;
		do{
			H+=kRaz;
			if(H==0){
				rad=0.6*Math.sqrt(lambda*d1*(d-d1)/(d));
				rastToL=hTrans-hInter;
			}
			else{
				rastToL=Math.abs((H*d1-d*Hp)/Math.sqrt(H*H+d*d));
				y0=H*(d*d1+H*Hp)/(d*d+H*H);
				x0=y0*d/H;
				a1=Math.sqrt(x0*x0+y0*y0);
				i=d-x0;j=H-y0;
				a2=Math.sqrt(i*i+j*j);
				rad=0.6*Math.sqrt(lambda*a1*a2/(a1+a2));
			}
			//System.out.println("H ="+H+" y0 ="+y0+" x0 = "+x0+" a1="+a1+" a2="+a2);
			//System.out.println("Rad = "+rad+" Line = "+rastToL+" diff= "+(rastToL-rad));
			if(rad<rastToL&&kRaz>0){
				kolIt++;
				kRaz*=-kMn;
			}
			else{
				if(rad>rastToL&&kRaz<0){
					kolIt++;
					kRaz*=-kMn;
				}
			}
			
			
		}while(kolIt<10);
		System.out.println("относительно высоты 1 антены "+H);
		
		return this.max(hRec,(H+hTrans));
	}
	
}
