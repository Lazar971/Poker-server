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
		double dob=ulog/n;
		for(int i=0;i<n;i++){
			pobednici[n].setNovac(pobednici[i].getNovac()+dob);
		}
		Server.igraJeUToku=false;
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
}
