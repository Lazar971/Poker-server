package model;

public class Ruka {
	private Karta najvecaKarta;
	private int kombinacija;
	public Ruka(Karta najvecaKarta, int kombinacija) {
		super();
		this.najvecaKarta = najvecaKarta;
		this.kombinacija = kombinacija;
	}
	
	public Ruka(){
		
	}

	public Karta getNajvecaKarta() {
		return najvecaKarta;
	}

	public void setNajvecaKarta(Karta najvecaKarta) {
		this.najvecaKarta = najvecaKarta;
	}

	public int getKombinacija() {
		return kombinacija;
	}

	public void setKombinacija(int kombinacija) {
		this.kombinacija = kombinacija;
	}
	public boolean bolja(Ruka r){
		return this.kombinacija>r.kombinacija
				||(this.kombinacija==r.kombinacija && this.najvecaKarta.getBroj()>r.najvecaKarta.getBroj());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + kombinacija;
		result = prime * result + ((najvecaKarta == null) ? 0 : najvecaKarta.hashCode());
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
		Ruka other = (Ruka) obj;
		if (kombinacija != other.kombinacija)
			return false;
		if (najvecaKarta == null) {
			if (other.najvecaKarta != null)
				return false;
		} else if (!najvecaKarta.istiBroj(other.najvecaKarta))
			return false;
		return true;
	}
	
}
