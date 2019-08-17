package model;

public class Karta {

	private int broj;
	private int znak;
	
	
	
	public Karta(int broj, int znak) {
		super();
		this.broj = broj;
		this.znak = znak;
	}
	public Karta(){
		
	}
	public int getBroj() {
		return broj;
	}
	public void setBroj(int broj) {
		this.broj = broj;
	}
	public int getZnak() {
		return znak;
	}
	public void setZnak(int znak) {
		this.znak = znak;
	}
	
	public static int poredi(Karta k1,Karta k2){
		
		return (int)Math.signum(k1.broj-k2.broj);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + broj;
		result = prime * result + znak;
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
		Karta other = (Karta) obj;
		if (broj != other.broj)
			return false;
		if (znak != other.znak)
			return false;
		return true;
	}
	public boolean istiBroj(Karta k){
		return this.broj==k.broj;
	}
	public boolean istiZnak(Karta k){
		return this.znak==k.znak;
	}
}
