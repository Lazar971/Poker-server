package klijentHendleri;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import main.Server;
import model.Igrac;


public class KlijentVeza extends Thread {

	private Socket soket;
	private Igrac igrac;
	BufferedReader ulaz;
	PrintStream izlaz;
	public KlijentVeza(Socket s){
		soket=s;
		
	}
	@Override
	public void run() {
		try{
			
			ulaz=new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlaz=new PrintStream(soket.getOutputStream());
			
			
			while(true){
				String akcija=ulaz.readLine();
				
				
				if(akcija.equals("LOGIN")){
					dodajIgracaLogin();
					continue;
				}
				if(akcija.equals("REGISTER")){
					
					dodajIgracaRegister();
					continue;
				}
			}
			
		}catch(Exception ex){
			Server.igra.izbrisiIgraca(igrac);
			Server.klijenti.remove(this);
			
			Server.azurirajKlijente();
		}
		
		
	}
	private void dodajIgracaRegister()throws IOException {
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
		Server.igra.dodajIgraca(i);
		
		Server.azurirajKlijente();
		
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
		if(Server.brojKojiImajuNovac()>1){
			synchronized (this) {
				try {
					
					notify();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public void posaljiIgrace(){
		izlaz.println(Server.igra.getIgraci().size());
		for(Igrac igr:Server.igra.getIgraci()){
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
