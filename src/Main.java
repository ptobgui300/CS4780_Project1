
public class Main {

	public static void main(String[] args) {
		
		//Triple SDES 
		System.out.println("Raw Key 1" + "    " + "Raw Key" + "      " + "Plaintext" + "  " + "Ciphertext" );
		
		TripleSDES one = new TripleSDES();	
		one.start();

		System.out.println();
		System.out.println("Raw Key" + "         " + "Plaintext" + "       " + "Ciphertext" );

		SDES two = new SDES();
		two.start();
	}
}
