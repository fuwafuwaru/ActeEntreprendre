
public enum Chord {
	CM, CdM, DM, DdM, EM, FM, FdM, GM, GdM, AM, AdM, BM,
	Cm, Cdm, Dm, Ddm, Em, Fm, Fdm, Gm, Gdm, Am, Adm, Bm;
	
	private Chroma first;
	private Chroma third;
	private Chroma fifth;
	
	Chord(){
		int i = this.ordinal();
		if(i < 12){
			first = Chroma.values()[i];
			third = Chroma.values()[(i+4)%12];
			fifth = Chroma.values()[(i+7)%12];
		}
		else{
			i = i%12;
			first = Chroma.values()[i];
			third = Chroma.values()[(i+3)%12];
			fifth = Chroma.values()[(i+7)%12];
		}
	}
	
	public Chroma getFirst(){return first;}
	public Chroma getThird(){return third;}
	public Chroma getFifth(){return fifth;}
	
	public static void main(String[] args){
	}
}
