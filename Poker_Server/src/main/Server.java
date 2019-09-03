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
				Server.azurirajKlijente();
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public  static void azurirajKlijente(){
		List<Igrac> igr=new LinkedList<Igrac>();
		System.out.println("Broj igraca: "+klijenti.size());
		for(KlijentVeza veza:klijenti){
			igr.add(veza.getIgrac());
		}
		igra.setIgraci(igr);
		System.out.println("Broj igraca igra: "+igra.getIgraci().size());
		for(KlijentVeza veza:klijenti){
			
			veza.getIzlaz().println("UPDATE");
			veza.posaljiIgrace();
		}
		Server.sinh.azurirajIgrajeMoguca(Server.brojKojiImajuNovac()>1 && !Server.igraJeUToku);
		
	}
	@Override
	public  void run() {
			while (true) {
				sinh.pocniIgru();
				igraJeUToku = true;
				inicijalizujIgru();
				System.out.println("pre primi uloge");
				if(primiUloge()){
					dobitakFold();
					obrisiKarte();
					igraJeUToku = false;
					continue;
				}
				izvuciKarte(3);
				if(primiUloge()){
					dobitakFold();
					obrisiKarte();
					igraJeUToku = false;
					continue;
				}
				izvuciKarte(1);
				if(primiUloge()){
					dobitakFold();
					obrisiKarte();
					igraJeUToku = false;
					continue;
				}
				izvuciKarte(1);
				if(primiUloge()){
					dobitakFold();
					obrisiKarte();
					igraJeUToku = false;
					continue;
				}
				podeliDobitkeKlijentima();
				obrisiKarte();
				igraJeUToku = false;
			}
			

	}
	private void obrisiKarte() {
		for(KlijentVeza veza:klijenti){
			veza.getIgrac().setPrvaKarta(null);
			veza.getIgrac().setDrugaKarta(null);
			veza.getIzlaz().println("KARTE");
			veza.getIzlaz().println(-1);
			azurirajKlijente();
		}
		
	}

	public boolean primiUloge(){

		double visinaUloga=0;
		Igrac i=null;
		KlijentVeza[] veza=new KlijentVeza[6];
		try {
			
			veza=klijenti.toArray(veza);
			
			int n=0;
			while(true){
				try {
					if((veza[n]==null && i==null)||(veza[n]!=null && veza[n].getIgrac().equals(i)))
						break;
					if(n==veza.length || veza[n]==null)
						n=0;
					obavestiOUloguITrenutnomIgracu(veza[n], visinaUloga);
					System.out.println("Pre slanja uloga");
					veza[n].getIzlaz().flush();
					veza[n].getIzlaz().println("ULOG");
					System.out.println("Poslat ulog");
					veza[n].getIzlaz().println(visinaUloga);
					
					String odgovor=sinh.cekajNaDobarOdgovor(veza[n]);
					veza[n].trenutnaPoruka="";
					System.out.println("Primljen ulog: "+odgovor);
					if(odgovor.equals("FOLD")){
						veza[n].getIgrac().setAktivan(false);
						
						if(igra.brojAktivnih()==1){
							return true;
						}
					}
					if(odgovor.equals("CHECK")){
						double u=veza[n].getIgrac().getUlog()+visinaUloga;
						veza[n].getIgrac().setUlog(u);
						veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
						igra.setUlog(igra.getUlog()+visinaUloga);
						continue;
					}
					String a=odgovor.split(" ")[0];
					if(a.equals("RAISE")){
						String[] podaci=odgovor.split(" ");
						i=veza[n].getIgrac();
						visinaUloga=Double.parseDouble(podaci[1]);
						double u=veza[n].getIgrac().getUlog()+visinaUloga;
						veza[n].getIgrac().setUlog(u);
						veza[n].getIgrac().setNovac(veza[n].getIgrac().getNovac()-visinaUloga);
						igra.setUlog(igra.getUlog()+visinaUloga);
						continue;
					}
					
				} catch (Exception e) {
					if(veza[n]!=null)
						veza[n].getIgrac().setAktivan(false);
					e.printStackTrace();
				}finally{
					
					Server.azurirajKlijente();
					n++;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public synchronized static void podeliDobitkeKlijentima(){
		igra.podeliDobitak();
		for(KlijentVeza veza:klijenti){
			for(Igrac igr:igra.getIgraci())
				if(igr.equals(veza.getIgrac())){
					System.out.println("Naso igraca");
					veza.setIgrac(igr);
					continue;
				}
			
		}
		
		Server.azurirajKlijente();
	}
	private static void dobitakFold(){
		igra.pobednikFold();
		for(KlijentVeza veza:klijenti){
			for(Igrac igr:igra.getIgraci())
				if(igr.equals(veza.getIgrac())){
					System.out.println("Naso igraca");
					veza.setIgrac(igr);
					continue;
				}
			
		}
		
		Server.azurirajKlijente();
	}
	private synchronized static void podeliKarteKlijentima(){
			igra.getSpil().napuniSpil();
			for(KlijentVeza klijent:klijenti){
				Karta k=igra.getSpil().izvuciKartu();
				
				klijent.getIgrac().setPrvaKarta(k);
				k=igra.getSpil().izvuciKartu();
				
				klijent.getIgrac().setDrugaKarta(k);
			}
			
			Server.azurirajKlijente();
		
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
			klijent.getIzlaz().println(veza.getIgrac().getKorisnickoIme()+" "+ulog);
		}
	}
}
