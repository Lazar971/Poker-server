package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import klijentHendleri.KlijentVeza;
import model.Igra;
import model.Igrac;

public class Server  {

	public static Igra igra=new Igra();
	public static List<KlijentVeza> klijenti= new LinkedList<KlijentVeza>();
	public static boolean igraJeUToku=false;
	public static void main(String[] args) {
		int port=11500;
		
		try(ServerSocket server=new ServerSocket(port)){
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
	public static void obavestiONovomKlijentu(){
		
		for(KlijentVeza veza:klijenti){
			veza.getIzlaz().println("UPDATE");
			veza.posaljiIgrace();
		}
	}
}
