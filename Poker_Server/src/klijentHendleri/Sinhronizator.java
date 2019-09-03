package klijentHendleri;

import java.util.LinkedList;
import java.util.List;

import main.Server;

public class Sinhronizator {

	
	public boolean zauzetiKlijenti;
	
	
	public synchronized List<KlijentVeza> dodajKlijenta(KlijentVeza k){
		List<KlijentVeza> veza=new LinkedList<KlijentVeza>();
		while(zauzetiKlijenti){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Server.klijenti;
			}
		}
		zauzetiKlijenti=true;
		for(KlijentVeza kl: Server.klijenti){
			veza.add(kl);
		}	
		veza.add(k);
		zauzetiKlijenti=false;
		notifyAll();
		return veza;
		
	}
	public synchronized List<KlijentVeza> obrisiKlijenta(KlijentVeza k){
		List<KlijentVeza> veza=new LinkedList<KlijentVeza>();
		while(zauzetiKlijenti){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Server.klijenti;
			}
		}
		zauzetiKlijenti=true;
		for(KlijentVeza kl: Server.klijenti)
			if(kl!=k)
				veza.add(kl);
				
		
		zauzetiKlijenti=false;
		notifyAll();
		return veza;
		
	}
	public synchronized void oslobodi(){
		zauzetiKlijenti=false;
		notifyAll();
	}
	public synchronized void pocniIgru(){
		while(!Server.igraJeMoguca){
			try {
				System.out.println("Ceka se");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			wait(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void azurirajIgrajeMoguca(boolean b){
		Server.igraJeMoguca=b;
		System.out.println("Igra je moguca: "+b);
		
		notifyAll();
	}
	public synchronized String cekajNaDobarOdgovor(KlijentVeza veza){
		String poruka=veza.trenutnaPoruka;
		String a=poruka.split(" ")[0];
		while(!poruka.equals("CHECK")&&!poruka.equals("FOLD")&&!a.equals("RAISE")){
			try {
				wait();
				System.out.println("Ne ceka vise");
				poruka=veza.trenutnaPoruka;
				a=poruka.split(" ")[0];
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return poruka;
	}
	public synchronized void obavestiODobromOdgovoru(KlijentVeza veza){
		String poruka=veza.trenutnaPoruka;
		String a=poruka.split(" ")[0];
		System.out.println("Obavesti o poruci, poruka: "+poruka);
		if(poruka.equals("CHECK")|| poruka.equals("FOLD")|| a.equals("RAISE"))
			notifyAll();
	}
}
