package model;

public class Igrac {
	private String korisnickoIme;
	private String ime;
	private String prezime;
	private String password;
	private Karta prvaKarta;
	private Karta drugaKarta;
	private Ruka ruka;
	private double novac=5000;
	private double ulog;
	private boolean aktivan;
	public Igrac(String ime, String password) {
		super();
		this.korisnickoIme = ime;
		this.password = password;
	}

	public Igrac(){
		
	}

	
	public Karta getPrvaKarta() {
		return prvaKarta;
	}
	public void setPrvaKarta(Karta prvaKarta) {
		this.prvaKarta = prvaKarta;
	}
	public Karta getDrugaKarta() {
		return drugaKarta;
	}
	public void setDrugaKarta(Karta drugaKarta) {
		this.drugaKarta = drugaKarta;
	}
	public Ruka getRuka() {
		return ruka;
	}
	public void setRuka(Ruka ruka) {
		this.ruka = ruka;
	}
	public double getNovac() {
		return novac;
	}
	public void setNovac(double novac) {
		this.novac = novac;
	}
	public double getUlog() {
		return ulog;
	}
	public void setUlog(double ulog) {
		this.ulog = ulog;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAktivan() {
		return aktivan;
	}

	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnickoIme == null) ? 0 : korisnickoIme.hashCode());
		return result;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Igrac other = (Igrac) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}
	
	
}
