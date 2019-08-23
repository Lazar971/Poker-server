package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import klijentHendleri.KlijentVeza;
import model.Igra;
import model.Igrac;
import model.Karta;

public class Server extends Thread {

	public static Igra igra=new Igra();
	public static List<KlijentVeza> klijenti= new LinkedList<KlijentVeza>();
	public static boolean igraJeUToku=false;
	public static void main(String[] args) {
		int port=11500;
		
		try(ServerSocket server=new ServerSocket(port)){
			Server s=new Server();
			s.start();
			System.out.println("Server radi");
			while(true){
				Socket soket=server.accept();
				
				KlijentVeza veza=new KlijentVeza(soket);
				klijenti.add(veza);
				
				veza.start();
			}
			
		}catch(IOException ex){
			
		}
		
	}
	public static void azurirajKlijente(){
		
		for(KlijentVeza veza:klijenti){
			veza.getIzlaz().println("UPDATE");
			veza.posaljiIgrace();
		}
	}
	@Override
	public void run() {
		while(true){
			System.out.println("Pre cekanja");
			synchronized (this) {
				if(brojKojiImajuNovac()<2)
					try {
						System.out.println("U sinc");
						wait();
						System.out.println("Posle cekanja");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				System.out.println("Igra pocinje");
				inicijalizujIgru();
				primiUloge();
				izvuciKarte(3);
				primiUloge();
				izvuciKarte(1);
				primiUloge();
				izvuciKarte(1);
				primiUloge();
				podeliDobitkeKlijentima();
				igraJeUToku=false;
			}
		}
	}
	public static void primiUloge(){

		double visinaUloga=0;
		Igrac i=null;
		KlijentVeza[] veza=new KlijentVeza[6];
		veza=klijenti.toArray(veza);
		int n=0;
		while(true){
			try {
				if(veza[n].equals(i)||(i==null && n==veza.length))
					break;
				if(n==veza.length)
					n=0;
				veza[n].getIzlaz().println("ULOG");
				veza[n].getIzlaz().println(visinaUloga);
				
				String odgovor=veza[n].getUlaz().readLine();
				if(odgovor.equals("FOLD")){
					veza[n].getIgrac().setAktivan(false);
				}
				if(odgovor.equals("CHECK")){
					double u=veza[n].getIgrac().getUlog()+visinaUloga;
					veza[n].getIgrac().setUlog(u);
					veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
					igra.setUlog(igra.getUlog()+visinaUloga);
					continue;
				}
				String[] podaci=odgovor.split(" ");
				i=veza[n].getIgrac();
				visinaUloga=Double.parseDouble(podaci[1]);
				double u=veza[n].getIgrac().getUlog()+visinaUloga;
				veza[n].getIgrac().setUlog(u);
				veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
				igra.setUlog(igra.getUlog()+visinaUloga);
			} catch (Exception e) {
				veza[n].getIgrac().setAktivan(false);
			}finally{
				Server.azurirajKlijente();
				n++;
			}
			
		}
	}

	public static void podeliDobitkeKlijentima(){
		igra.podeliDobitak();
		Server.azurirajKlijente();
	}
	private static void podeliKarteKlijentima(){
		igra.getSpil().napuniSpil();
		for(KlijentVeza klijent:klijenti){
			klijent.getIgrac().setPrvaKarta(igra.getSpil().izvuciKartu());
			klijent.getIgrac().setDrugaKarta(igra.getSpil().izvuciKartu());
		}
		Server.azurirajKlijente();
	}
	public static void izvuciKarte(int a){
		Karta[] k=new Karta[3];
		for(int i=0;i<a;i++)
			k[i]=igra.getSpil().izvuciKartu();
		for(KlijentVeza klijent:klijenti){
			klijent.getIzlaz().println(k.length);
			for(int i=0;i<k.length;i++)
				klijent.getIzlaz().println(k[i].getBroj()+" "+k[i].getZnak());
		}
		
	}
	

	public static int brojKojiImajuNovac(){
		int n=0;
		for(KlijentVeza v:klijenti){
			if(v.getIgrac().isAktivan() && v.getIgrac().getNovac()>50)
				n++;
		}
		return n;
	}
	public void inicijalizujIgru(){
		igraJeUToku=true;
		igra=new Igra();
		for(KlijentVeza klijent:klijenti){
			klijent.getIgrac().setNovac(klijent.getIgrac().getNovac()-50);
			klijent.getIgrac().setUlog(50);
			podeliKarteKlijentima();
		}
	}
}
