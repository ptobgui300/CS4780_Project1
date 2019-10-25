
public class Main {

	public static void main(String[] args) {
		
		//Triple SDES 
		System.out.println("Raw Key 1" + "     " + "Raw Key" + "    " + "Plaintext" + "   " + "Ciphertext" );
		System.out.println();
		
		TripleSDES one = new TripleSDES();	
		one.start();
		
		
	}
}
