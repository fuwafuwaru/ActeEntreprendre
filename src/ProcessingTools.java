
public class ProcessingTools {
	
	public static double[] blackmanWindow(double[] a, float rate, int FFT_SIZE){
    	double[] b = new double[FFT_SIZE];
    	for(int k = 0; k < FFT_SIZE; k++){
    		b[k] = a[k] * (0.42 - 0.5*Math.cos((2*Math.PI*k)/FFT_SIZE) + 0.08*Math.cos((4*Math.PI*k)/FFT_SIZE));
    	}
    	return b;
    }
	
	public static Complex[] fft(Complex[] x) {
        int N = x.length;
        if (N == 1) return new Complex[] { x[0] };
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);
        Complex[] odd  = even;
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
	}
	
	
	/*
	 * We only take the first half of the complex array output of the fft for we're only interested in the real part of
	 * Fourier's coefficients.
	 */
	
	public static double[] onlyHalf(Complex[] arr){
		double[] array = new double[arr.length/2];
		for(int k = 0; k < arr.length/2; k++){
			array[k] = arr[k].abs();
		}
		return array;
	}
	
	
	public static double getMax(double[] array){
		double max = 0;
		for(double x : array){
			if(x > max){
				max = x;
			}
		}
		return max;
	}
	
	public static int[] findLocalMax(double[] array){
		int[] output = new int[10];   //Il faut créer variable globale donnant le max de notes qu'on s'impose pour une frame
		double dif = 0;
		int k = 0;
		int i = 0;
		double max = getMax(array);
		while(k < array.length - 2){ 
			dif = (array[k+1]-array[k])/array[k];
			if(dif >= 2.0 && (max/array[k+1] <= 10 | max/array[k+2] <= 10)){ 
				double dif2 = 0.;
				int j = 0;
				while(j <= 4){ //On cherche le pic au plus tard quatre cases plus loin
					k++;
					if(k+1 >= array.length){
						break;
					}
					else{
						dif2 = (array[k+1]-array[k])/array[k];
						if(dif2 <= 0 && max/array[k] <= 10){ //La deuxième condition sert à ne pas marquer comme pic des amplitudes infimes
							if(i < output.length){
								output[i] = k;
								i++;
								j=5; //On sort de la boucle on a plus de maximum à chercher.
							}
						}
					}
					j++;
				}
				k++;
			}
			else{
				k++;
			}
		}
		return output;
	}
	
}
