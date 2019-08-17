package model;

public interface KalkulatorKombinacije {

	public boolean najvecaKarta(Karta[] karte);
	public boolean par(Karta[] karte);
	public boolean dvaPara(Karta[] karte);
	public boolean triIste(Karta[] karte);
	public boolean straight(Karta[] karte);
	public boolean flush(Karta[] karte);
	public boolean fullHouse(Karta[] karte);
	public boolean cetiriIste(Karta[] karte);
	public boolean straightFlush(Karta[] karte);
	public boolean royalFlush(Karta[] karte);
	public Ruka dajRuku(Karta[] karte);
	
}
