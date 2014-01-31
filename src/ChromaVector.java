
public class ChromaVector {
	public double[] chromaVector = new double[12]; 
	ChromaVector(){
	}
	
	ChromaVector(double[] array){
		for(int k=0; k<array.length;k++){
			chromaVector[(k+43)%12] += array[k];
		}
	}
	
	
	ChromaVector(Chord c, int i){ //0 = pas d'harmoniques, 1 = 4 harmoniques, 2 = 6 harmoniques
		if(i == 0){
			chromaVector[c.getFirst().ordinal()] = 1;
			chromaVector[c.getThird().ordinal()] = 1;
			chromaVector[c.getFifth().ordinal()] = 1;
		}
		else if(i == 1){
			chromaVector[c.getFirst().ordinal()] = 1;
			chromaVector[c.getThird().ordinal()] = 1;
			chromaVector[c.getFifth().ordinal()] = 1;
			for (int j = 1; j < 4; j++) {
				int k = (int) (12*Math.log(j+1)/Math.log(2));
				chromaVector[(c.getFirst().ordinal() + k)%12] += Math.pow(0.6, j);
				chromaVector[(c.getThird().ordinal() + k)%12] += Math.pow(0.6, j);
				chromaVector[(c.getFifth().ordinal() + k)%12] += Math.pow(0.6, j);
			}
		}
		else{
			chromaVector[c.getFirst().ordinal()] = 1;
			chromaVector[c.getThird().ordinal()] = 1;
			chromaVector[c.getFifth().ordinal()] = 1;
			for (int j = 1; j < 6; j++) {
				int k = (int) (12*Math.log(j+1)/Math.log(2));
				chromaVector[(c.getFirst().ordinal() + k)%12] += Math.pow(0.6, j);
				chromaVector[(c.getThird().ordinal() + k)%12] += Math.pow(0.6, j);
				chromaVector[(c.getFifth().ordinal() + k)%12] += Math.pow(0.6, j);
			}
		}
		
	}
	
	public void print(){
		for(int k=0; k<12; k++){
			System.out.println((k) + " : " + this.chromaVector[k]);
		}
	}
	
	public void normalise(){
		double m=0;
		for(double x : chromaVector){
			m +=x;
		}
		for(int k=0; k<12;k++){
			chromaVector[k] = chromaVector[k]/m;
		}
	}
	
	
	/*
	Warning this function only works with normalized vectors
	 */
	
	public double computeCorrelation(ChromaVector v){
		double correl = 0;
		for(int k=0; k<12; k++){
			correl += this.chromaVector[k]*v.chromaVector[k];
		}
		return correl;
	}
	
	public Chord findMaxCorrelation(){
		Chord tmp = Chord.values()[0];;
		double maxCorrel=0.;
		double correl=0;
		for(Chord c : Chord.values()){
			ChromaVector v = new ChromaVector(c, 2);// 6 harmoniques considérés
			v.normalise();
			correl = this.computeCorrelation(v);
			if(correl > maxCorrel){
				maxCorrel = correl;
				tmp = c;
			}
		}
		return tmp;
	}
	
	/*
	 * We take into account the probabilty of having a transition from the previous alledged chord to the tested potential chord
	 * to perform the computation of the likeliest current alledged chord
	 */
	
	public Chord findMaxWeightedCorrelation(Chord previousChord){
		Chord tmp = Chord.values()[0];;
		double maxCorrel=0.;
		double correl=0;
		for(Chord c : Chord.values()){
			ChromaVector v = new ChromaVector(c, 2);// 6 harmoniques considérés
			v.normalise();
			correl = this.computeCorrelation(v)*CommonsInterface.transitionMatrix[c.ordinal()][previousChord.ordinal()];
			if(correl > maxCorrel){
				maxCorrel = correl;
				tmp = c;
			}
		}
		return tmp;
	}
	

	
	public int getMaxIndex(){
		double m = chromaVector[0];
		int tmp = 0;
		for(int k=1; k<12;k++){
			if(chromaVector[k] > m){
				tmp = k;
				m = chromaVector[k];
			}
		}
		return tmp;
	}
	
	
	public static void main(String[] args){
		/*ChromaVector cv = new ChromaVector(Chord.CM,0);
		System.out.println(cv.findMaxCorrelation());
		System.out.println(cv.computeCorrelation(new ChromaVector(cv.findMaxCorrelation(), 1)));
		for(double x : (new ChromaVector(Chord.CM, 2)).chromaVector){
			System.out.printf("%f ",x);
		}*/
	}
}
