package model;

import java.util.Random;

public class Spil {

	private int brojKarata=52;
	private Karta karte[]=new Karta[brojKarata];
	private Random r=new Random();
	
	public Spil(){
		napuniSpil();
	}
	public void napuniSpil(){
		
		int n=0;
		brojKarata=52;
		for(int i =0;i<4;i++){
			for(int j=2;j<15;j++){
				karte[n++]=new Karta(j, i);
				
			}
		}
		promesaj();
		
	}
	public void promesaj(){
		
		for(int i=0;i<1500;i++){
			int n1=r.nextInt(brojKarata);
			int n2=r.nextInt(brojKarata);
			Karta temp=karte[n1];
			karte[n1]=karte[n2];
			karte[n2]=temp;
		}
	}
	public Karta izvuciKartu(){
		if(brojKarata<10){
			napuniSpil();
			promesaj();
		}
		
		return karte[--brojKarata];
	}
	
}
