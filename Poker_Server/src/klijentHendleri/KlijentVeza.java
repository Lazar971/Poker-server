package klijentHendleri;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import main.Server;
import model.Igrac;


public class KlijentVeza extends Thread {

	private Socket soket;
	private Igrac igrac;
	public String trenutnaPoruka;
	BufferedReader ulaz;
	PrintStream izlaz;
	public KlijentVeza(Socket s){
		soket=s;
		try {
			ulaz=new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlaz=new PrintStream(soket.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
			try {
				
				while (true) {
				String akcija = ulaz.readLine();
				trenutnaPoruka = akcija;
				Server.sinh.obavestiODobromOdgovoru(this);
				if (akcija.equals("LOGIN")) {
					dodajIgracaLogin();

				}
				if (akcija.equals("REGISTER")) {

					dodajIgracaRegister();

				}
				
				}
			} catch (Exception ex) {
				Server.igra.izbrisiIgraca(igrac);
				Server.klijenti=Server.sinh.obrisiKlijenta(this);
				System.out.println("Izbirsan klijent");
				
				Server.azurirajKlijente();
			} 
		
		
		
	}
	
	private synchronized void dodajIgracaRegister()throws IOException {
		
			
		String[] podaci=ulaz.readLine().split(" ");
		
		Igrac i=new Igrac(podaci[2], podaci[3]);
		izlaz.println("REGISTER");
		//Baza
		if(Server.igra.imaIgraca(i)){
			izlaz.println("1");
			return;
		};
		if(Server.igra.getIgraci().size()==6){
			izlaz.println("2");
			return;
		}
		i.setIme(podaci[0]);
		i.setPrezime(podaci[1]);
		izlaz.println("0");
		izlaz.println(i.getNovac());
		i.setAktivan(!Server.igraJeUToku);
		this.igrac=i;
		
		
		
		Server.sinh.oslobodi();
		
		
		
	}
	
	public synchronized void posaljiIgrace(){
		//System.out.println("salje igraca");
		izlaz.println(Server.igra.getIgraci().size());
		for(Igrac igr:Server.igra.getIgraci()){
			//System.out.println(igr.getKorisnickoIme()+" "+igr.getNovac()+" "+igr.getUlog()+" "+igr.isAktivan());
			izlaz.println(igr.getKorisnickoIme()+" "+igr.getNovac()+" "+igr.getUlog());
			if(igr.getPrvaKarta()==null){
				izlaz.println("1");
				continue;
			}
			izlaz.println("0");
			izlaz.println((igr.equals(this.igrac))?igr.getPrvaKarta().getBroj()+" "+igr.getPrvaKarta().getZnak():"0 0");
			if(igr.getDrugaKarta()==null){
				izlaz.println("1");
				continue;
			}
			izlaz.println("0");
			izlaz.println((igr.equals(this.igrac))?igr.getDrugaKarta().getBroj()+" "+igr.getDrugaKarta().getZnak():"0 0");
			
		}
	}
	
	
	
	private void dodajIgracaLogin()throws IOException{
		String[] podaci=ulaz.readLine().split(" ");
		Igrac i=new Igrac(podaci[0], podaci[1]);
		//Baza
		izlaz.println("LOGIN");
		if(!Server.igra.imaIgraca(i)){
			izlaz.println("1");
			return;
		}
		//Isto Baza
		for(Igrac igr:Server.igra.getIgraci()){
			if(igr.equals(i)){
				if(!i.getPassword().equals(igr.getPassword())){
					izlaz.println("2");
					return;
				}
			}
		}
		if(Server.igra.getIgraci().size()==6){
			izlaz.println("3");
			return;
		}
		i.setAktivan(!Server.igraJeUToku);
		this.igrac=i;
		izlaz.println("0");
		Server.igra.dodajIgraca(igrac);
		Server.azurirajKlijente();
		
		
		
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((igrac == null) ? 0 : igrac.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KlijentVeza other = (KlijentVeza) obj;
		if (igrac == null) {
			if (other.igrac != null)
				return false;
		} else if (!igrac.equals(other.igrac))
			return false;
		return true;
	}
	public Igrac getIgrac() {
		return igrac;
	}
	public void setIgrac(Igrac igrac) {
		this.igrac = igrac;
	}
	public BufferedReader getUlaz() {
		return ulaz;
	}
	public PrintStream getIzlaz() {
		return izlaz;
	}
	
	
}
