/*
 * Interface containing all that we would like to render accessible from any class
 */

public class Commons {
	 static double transitionMatrix[][] = new double[24][24];
	 static Chord circleOfFifth[] = new Chord[24];
	 static private double epsilon = 0.01;
		 
		 /*{Chord.CM, Chord.Em, Chord.GM, Chord.Bm, Chord.DM,
			 Chord.Fdm, Chord.AM, Chord.Cdm, Chord.EM, Chord.Gdm, Chord.BM, Chord.Ddm,
			 Chord.FdM, Chord.Adm, Chord.CdM, Chord.Fm, Chord.GdM, Chord.Cm,
			 Chord.DdM, Chord.Gm, Chord.AdM, Chord.Dm, Chord.FM, Chord.Am};*/
	 
	 
	 
	 public static void initCircle(){
		 Chord c = Chord.CM;
		 circleOfFifth[0] = c;
		 for(int k = 1; k< 12; k++){
			 c = Chord.values()[(c.ordinal()+7)%12];
			 circleOfFifth[k*2] = c;
		 }
		 c = Chord.Em;
		 circleOfFifth[1] = c;
		 for(int k = 1; k< 12; k++){
			 c = Chord.values()[(c.ordinal()+7)%12+12];
			 circleOfFifth[k*2+1] = c;
		 }
	 }
	 
	 
	 public static int computeDistance(Chord a, Chord b){
		 int k = 0;
		 int j = 0;
		 for(int i = 0; i< 24; i++){
			 if(circleOfFifth[i] == a ){
				 k = i;
			 }
			 if(circleOfFifth[i] == b){
				 j = i;

			 }
		 }
		 
		 if(Math.abs(k-j) < 12){
			 return(Math.abs(k-j));
			 
		 }
		 
		 else if(Math.abs(k-j) > 12){
			 return (Math.abs(Math.abs(k-j) - 24));
			 
		 }
		 else{
			 return (Math.abs(k-j));
		 }
	 }
	 
	 public static double[] permuteRight(double[] array, int i){
		 double[] ret = new double[24];
		 for(int k = 0; k < 24; k++){
			 ret[(k+i)%24] = array[k];
		 }
		 return ret;
	 }
	 
	 public static void initMatrix(){
		 int dist = 0;
		 for(int k = 0; k<24; k++){
			 for(int j = 0; j<24; j++){
				 dist = computeDistance(Chord.values()[j], Chord.values()[k]);
				 transitionMatrix[j][k] = (12 - dist + epsilon)/(144+24*epsilon);
			 }
			
		 }
	 }
	 
	 public static void main(String[] args){
		 initCircle();
		 System.out.print("{");
		 for(int k=0; k<24; k++){
			 System.out.print("Chord." + circleOfFifth[k] + ", ");
		 }
		 System.out.print("}");
		 System.out.println("");
		 System.out.println(computeDistance(Chord.AM, Chord.AM));
		 initMatrix();
		 System.out.print("{");
		 for(int k = 0; k< 24; k++){
			 System.out.print("{");
			 for(int i = 0; i < 24; i++){
				 System.out.print(transitionMatrix[k][i]);
				 if(i < 23){
					 System.out.print(", ");
				 }
			 }
			 System.out.print("}");
			 if(k<23){
				 System.out.println(",");
			 }
		 }
		 System.out.print("}");
	 }
	 
	 
	/* public static void main(String[] args){
		 
		 
		 initCircle();
		 System.out.println(computeDistance(Chord.Adm, Chord.CM));
		 for(Chord c : circleOfFifth){
			 
			 System.out.println(c);
			 
		 }
		 
	 
	 }*/
	 
}
