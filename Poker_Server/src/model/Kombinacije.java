package model;

public class Kombinacije implements KalkulatorKombinacije{

	private void sortirajPoBroju(Karta[] karte){
		for(int i=0;i<karte.length-1;i++){
			for(int j=i;j<karte.length;j++){
				if(karte[i].getBroj()<karte[j].getBroj()){
					Karta temp=karte[i];
					karte[i]=karte[j];
					karte[j]=temp;
				}
			}
		}
	}
	private void sortirajPoBoji(Karta[] karte){
		for(int i=0;i<karte.length-1;i++){
			for(int j=i;j<karte.length;j++){
				if(karte[i].getZnak()>karte[j].getZnak()){
					Karta temp=karte[i];
					karte[i]=karte[j];
					karte[j]=temp;
				}
			}
		}
	}
	
	@Override
	public boolean par(Karta[] karte) {
		sortirajPoBroju(karte);
		for(int i=0;i<karte.length-1;i++){
			if(karte[i].istiBroj(karte[i+1]))
				return true;
		}
		
		return false;
	}
	@Override
	public boolean dvaPara(Karta[] karte) {
		sortirajPoBroju(karte);
		int i=0;
		boolean prviPar=false;
		while(i<karte.length-3){
			if(karte[i].istiBroj(karte[i+1])){
				prviPar=true;
				i++;
				break;
			}
			i++;
		}
		for(;i<karte.length-1;i++){
			if(karte[i].istiBroj(karte[i+1]))
				return true;
		}
		return false;
	}
	@Override
	public boolean triIste(Karta[] karte) {
		sortirajPoBroju(karte);
		for(int i=0;i<karte.length-2;i++){
			if(karte[i].istiBroj(karte[i+1])&&karte[i].istiBroj(karte[i+2]) )
				return true;
		}
		return false;
	}
	@Override
	public boolean straight(Karta[] karte) {
		sortirajPoBroju(karte);
		for(int i=0;i<karte.length-4;i++){
			boolean flush=false;
			for(int j=1;j<5;j++){
				flush=karte[i].getBroj()==karte[i+j].getBroj()+j;
			}
			if(flush)
				return true;
		}
		
		return false;
	}
	@Override
	public boolean flush(Karta[] karte) {
		sortirajPoBoji(karte);
		for(int i=0;i<karte.length-4;i++){
			if(!karte[i].istiZnak(karte[i+1]))
				continue;
			if(!karte[i].istiZnak(karte[i+2]))
				continue;
			if(!karte[i].istiZnak(karte[i+3]))
				continue;
			if(!karte[i].istiZnak(karte[i+4]))
				continue;
			return true;
		}
		return false;
	}
	@Override
	public boolean fullHouse(Karta[] karte) {
		return triIste(karte)&& dvaPara(karte);
	}
	@Override
	public boolean cetiriIste(Karta[] karte) {
		for(int i=0;i<karte.length-3;i++){
			if(karte[i].istiBroj(karte[i+1])&&karte[i].istiBroj(karte[i+2])&&karte[i].istiBroj(karte[i+3]))
				return true;
		}
		return false;
	}
	@Override
	public boolean straightFlush(Karta[] karte) {
		return straight(karte)&&flush(karte);
	}
	@Override
	public boolean royalFlush(Karta[] karte) {
		
		return flush(karte)&&straight(karte)&&  karte[0].getBroj()==13;
	}
	@Override
	public boolean najvecaKarta(Karta[] karte) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Ruka dajRuku(Karta[] karte) {
		Ruka r=new Ruka();
		
		if(straightFlush(karte)){
			r.setKombinacija(9);
			
		}else{
			if(cetiriIste(karte)){
				r.setKombinacija(8);
			}else{
				if(fullHouse(karte)){
					r.setKombinacija(7);
				}else{
					if(flush(karte)){
						r.setKombinacija(6);
					}else{
						if(straight(karte)){
							r.setKombinacija(5);
						}else{
							if(triIste(karte)){
								r.setKombinacija(4);
							}else{
								if(dvaPara(karte)){
									r.setKombinacija(3);
								}else{
									if(par(karte)){
										r.setKombinacija(2);
									}else{
										r.setKombinacija(1);
									}
								}
							}
						}
					}
				}
			}
		}
		
		sortirajPoBroju(karte);
		r.setNajvecaKarta(karte[0]);
		return r;
	}
}
