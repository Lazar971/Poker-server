package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import klijentHendleri.KlijentVeza;
import klijentHendleri.Sinhronizator;
import model.Igra;
import model.Igrac;
import model.Karta;

public class Server extends Thread {

	public static Igra igra=new Igra();
	public static List<KlijentVeza> klijenti= new LinkedList<KlijentVeza>();
	public static Server s;
	public static Sinhronizator sinh=new Sinhronizator();
	public static boolean igraJeUToku=false;
	public static boolean igraJeMoguca=false;
	public static int n=0;
	public static void main(String[] args) {
		int port=11500;
		
		try(ServerSocket server=new ServerSocket(port)){
			System.out.println("Server radi");
			s=new Server();
			s.start();
			 
			while(true){
				
				Socket soket=server.accept();
				
				KlijentVeza veza=new KlijentVeza(soket);
				veza.start();
					
				sinh.zauzetiKlijenti=true;
				klijenti=sinh.dodajKlijenta(veza);
				System.out.println("Server main");
				Server.azurirajKlijente(true);
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public  static void azurirajKlijente(boolean b){
		List<Igrac> igr=new LinkedList<Igrac>();
		for(KlijentVeza veza:klijenti){
			igr.add(veza.getIgrac());
		}
		igra.setIgraci(igr);
		for(KlijentVeza veza:klijenti){
			
			veza.getIzlaz().println("UPDATE");
			veza.posaljiIgrace(b);
		}
		Server.sinh.azurirajIgrajeMoguca(Server.brojKojiImajuNovac()>1 && !Server.igraJeUToku);
		
	}
	@Override
	public  void run() {
			while (true) {
				sinh.pocniIgru();
				igraJeUToku = true;
				inicijalizujIgru();
				if(primiUloge()){
					dobitakFold();
					sinh.cekaj(10000);
					obrisiKarte();
					
					continue;
				}
				izvuciKarte(3);
				if(primiUloge()){
					dobitakFold();
					sinh.cekaj(10000);
					obrisiKarte();
					
					continue;
				}
				izvuciKarte(1);
				if(primiUloge()){
					dobitakFold();
					sinh.cekaj(10000);
					obrisiKarte();
					continue;
				}
				izvuciKarte(1);
				if(primiUloge()){
					dobitakFold();
					sinh.cekaj(10000);
					obrisiKarte();
					continue;
				}
				podeliDobitkeKlijentima();
				sinh.cekaj(20000);
				obrisiKarte();
				
			}
			

	}
	private void obrisiKarte() {
		for(KlijentVeza veza:klijenti){
			veza.getIgrac().setPrvaKarta(null);
			veza.getIgrac().setDrugaKarta(null);
			veza.getIzlaz().println("KARTE");
			veza.getIzlaz().println(-1);
			azurirajKlijente(true);
			igraJeUToku=false;
			igraJeMoguca=brojKojiImajuNovac()>1;
		}
		
	}

	public boolean primiUloge(){

		double visinaUloga=0;
		
		int brojIgraca=0;
		KlijentVeza[] veza=new KlijentVeza[klijenti.size()];
		veza=klijenti.toArray(veza);
		System.out.println("Duzina niza: "+veza.length);	
			while(true){
				try {
					if(brojIgraca==veza.length)
						break;
					if(n>=veza.length ){
						n=0;
					}
						
					if(!veza[n].getIgrac().isAktivan()){
						brojIgraca++;
						n++;
						continue;
					}
					obavestiOUloguITrenutnomIgracu(veza[n], visinaUloga);
					
					veza[n].getIzlaz().flush();
					veza[n].getIzlaz().println("ULOG");
					
					veza[n].getIzlaz().println(visinaUloga);
					
					String odgovor=sinh.cekajNaDobarOdgovor(veza[n]);
					veza[n].trenutnaPoruka="";
					if(odgovor.equals("FOLD")){
						veza[n].getIgrac().setAktivan(false);
						brojIgraca++;
						n++;
						if(igra.brojAktivnih()==1){
							return true;
						}
					}
					if(odgovor.equals("CHECK")){
						double u=visinaUloga;
						veza[n].getIgrac().setUlog(u);
						veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
						igra.setUlog(igra.getUlog()+u);
						brojIgraca++;
						n++;
						continue;
					}
					String a=odgovor.split(" ")[0];
					if(a.equals("RAISE")){
						String[] podaci=odgovor.split(" ");
						brojIgraca=1;
						visinaUloga=Double.parseDouble(podaci[1]);
						double u=veza[n].getIgrac().getUlog()+visinaUloga;
						veza[n].getIgrac().setUlog(u);
						veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
						igra.setUlog(igra.getUlog()+visinaUloga);
						
						n++;
						continue;
					}
					brojIgraca++;
					n++;
				} catch (Exception e) {
					if(n<veza.length && veza[n]!=null)
						veza[n].getIgrac().setAktivan(false);
					e.printStackTrace();
				}finally{
					
					Server.azurirajKlijente(true);
				}
			}
		return false;
	}

	public synchronized static void podeliDobitkeKlijentima(){
		igra.podeliDobitak();
		for(KlijentVeza veza:klijenti){
			for(Igrac igr:igra.getIgraci())
				if(igr.equals(veza.getIgrac())){
					
					veza.setIgrac(igr);
					continue;
				}
			
		}
		
		Server.azurirajKlijente(false);
	}
	private static void dobitakFold(){
		igra.pobednikFold();
		for(KlijentVeza veza:klijenti){
			for(Igrac igr:igra.getIgraci())
				if(igr.equals(veza.getIgrac())){
					
					veza.setIgrac(igr);
					continue;
				}
			
		}
		
		Server.azurirajKlijente(true);
	}
	private synchronized static void podeliKarteKlijentima(){
			igra.getSpil().napuniSpil();
			for(KlijentVeza klijent:klijenti){
				Karta k=igra.getSpil().izvuciKartu();
				
				klijent.getIgrac().setPrvaKarta(k);
				k=igra.getSpil().izvuciKartu();
				
				klijent.getIgrac().setDrugaKarta(k);
			}
			
			Server.azurirajKlijente(true);
		
	}
	public synchronized static void izvuciKarte(int a){
		Karta[] k=new Karta[3];
		for(int i=0;i<a;i++){
			k[i]=igra.getSpil().izvuciKartu();
			igra.dodajKartuNaSto(k[i]);
		}
		for(KlijentVeza klijent:klijenti){
			klijent.getIzlaz().println("KARTE");
			klijent.getIzlaz().println(a);
			for(int i=0;i<a;i++)
				klijent.getIzlaz().println(k[i].getBroj()+" "+k[i].getZnak());
		}
		
	}
	

	public synchronized static int brojKojiImajuNovac(){
		if(klijenti==null)
			return 0;
		int n=0;
		for(KlijentVeza v:klijenti){
			if(v.getIgrac()!=null && v.getIgrac().isAktivan() && v.getIgrac().getNovac()>50)
				n++;
		}
		return n;
	}
	public synchronized void inicijalizujIgru(){
		
		igra=new Igra();
		n=0;
		for(KlijentVeza klijent:klijenti){
			if(klijent.getIgrac()==null || !klijent.getIgrac().isAktivan() || klijent.getIgrac().getNovac()<50)
				continue;
			klijent.getIgrac().setNovac(klijent.getIgrac().getNovac()-50);
			klijent.getIgrac().setUlog(klijent.getIgrac().getUlog()+50);
			igra.setUlog(igra.getUlog()+50);
			podeliKarteKlijentima();
			
		}
		
	}
	public static void obavestiOUloguITrenutnomIgracu(KlijentVeza veza,double ulog){
		if(veza==null)
			return;
		for(KlijentVeza klijent:klijenti){
			klijent.getIzlaz().println("TRENUTNI");
			klijent.getIzlaz().println(veza.getIgrac().getKorisnickoIme()+" "+ulog+" "+igra.getUlog());
		}
	}
}
