package model;

import java.util.LinkedList;
import java.util.List;

import main.Server;

public class Igra {

	private  List<Igrac> igraci=new LinkedList<Igrac>();
	private Spil spil=new Spil();
	private Karta[] karteNaStolu=new Karta[5];
	private int brojKarataNaStolu=0;
	private double ulog;
	private KalkulatorKombinacije kalkulator=new Kombinacije();
	public void podeliKarteIgracima(){
		for(Igrac ig: igraci){
			ig.setPrvaKarta(spil.izvuciKartu());
			ig.setDrugaKarta(spil.izvuciKartu());
		}
	}
	public synchronized void podeliDobitak(){
		
		for(Igrac igrac:igraci){
			Karta[] karte=new Karta[7];
			for(int i=0;i<5;i++)
				karte[i]=karteNaStolu[i];
			karte[5]=igrac.getPrvaKarta();
			karte[6]=igrac.getDrugaKarta();
			igrac.setRuka(kalkulator.dajRuku(karte));
			igrac.setUlog(0);
		}
		Igrac[] pobednici=new Igrac[10];
		int n=0;
		Ruka r=igraci.get(0).getRuka();
		for(Igrac igrac:igraci){
			if(igrac.getRuka().bolja(r)){
				n=1;
				pobednici[0]=igrac;
				r=igrac.getRuka();
				continue;
			}
			if(igrac.getRuka().equals(r)){
				pobednici[n++]=igrac;
			}
		}
		if(n==0){
			return;
		}
		System.out.println("Broj pobednika: "+n+" Ukupan ulog: "+ulog);
		double dob=ulog/n;
		for(int i=0;i<n;i++){
			pobednici[i].setNovac(pobednici[i].getNovac()+dob);
		}
		
	}
	
	public void pobednikFold(){
		if(brojAktivnih()>1)
			return;
		for(Igrac i:igraci){
			i.setUlog(0);
			if(i.isAktivan()){
				i.setNovac(i.getNovac()+ulog);
			}
		}
		
		ulog=0;
	}
	public void dodajIgraca(Igrac i){
		this.igraci.add(i);
	}
	public void izbrisiIgraca(Igrac i){
		this.igraci.remove(i);
	}
	public boolean imaIgraca(Igrac i){
		return this.igraci.contains(i);
	}
	public List<Igrac> getIgraci() {
		return igraci;
	}
	public double getUlog() {
		return ulog;
	}
	public void setUlog(double ulog) {
		this.ulog = ulog;
	}
	
	public Spil getSpil(){
		return this.spil;
	}
	public void setIgraci(List<Igrac> igraci) {
		this.igraci = igraci;
	}
	public void dodajKartuNaSto(Karta k){
		karteNaStolu[brojKarataNaStolu++]=k;
	}
	public void obrisiKarte(){
		
		brojKarataNaStolu=0;
	}
	public int brojAktivnih(){
		int a=0;
		for(Igrac i:igraci){
			if(i.isAktivan())
				a++;
		}
		return a;
	}
}
