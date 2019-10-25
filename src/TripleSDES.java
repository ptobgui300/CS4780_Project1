public class TripleSDES {
	
	public void start() {
		Encryption();
		Decryption();
	}
	
	public static byte[] Encrypt(byte[] rawkey1, byte[] rawkey2, byte[] plaintext) {
		return SDES.Encrypt(rawkey1, SDES.Decrypt(rawkey2, SDES.Encrypt(rawkey1, plaintext)));
	}

	public static byte[] Decrypt(byte[] rawkey1, byte[] rawkey2, byte[] ciphertext) {
		return SDES.Decrypt(rawkey1, SDES.Encrypt(rawkey2, SDES.Decrypt(rawkey1, ciphertext)));

	}

	private static void Encryption() {

		byte[][] rawKey1 = new byte[4][];
		byte[][] rawKey2 = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		rawKey1[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		rawKey1[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		rawKey1[2] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		rawKey1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		rawKey2[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		rawKey2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		rawKey2[2] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		rawKey2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 0, 1, 0, 1, 1, 1 };
		plaintext[2] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		plaintext[3] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		
		for (int i = 0; i < 4; i++) {
			
			ciphertext[i] = TripleSDES.Encrypt(rawKey1[i], rawKey2[i], plaintext[i]);
			printTable(rawKey1[i],rawKey2[i],plaintext[i],ciphertext[i]);
		
		}
	}

	private static void Decryption() {

		byte[][] rawKey1 = new byte[4][];
		byte[][] rawKey2 = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		rawKey1[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		rawKey1[1] = new byte[]{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 };
		rawKey1[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		rawKey1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		rawKey2[0] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		rawKey2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		rawKey2[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		rawKey2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		ciphertext[0] = new byte[]{ 1, 1, 1, 0, 0, 1, 1, 0 };
		ciphertext[1] = new byte[]{ 0, 1, 0, 1, 0, 0, 0, 0 };
		ciphertext[2] = new byte[]{ 1, 0, 0, 0, 0, 0, 0, 0 };
		ciphertext[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 1, 0 };
		
		for (int i = 0; i < 4; i++) {	
			
			plaintext[i] = TripleSDES.Decrypt(rawKey1[i], rawKey2[i], ciphertext[i]);
			printTable(rawKey1[i],rawKey2[i],plaintext[i],ciphertext[i]);
		
		
		}
	}
	
	private static void printTable(byte[] rawKey1, byte[] rawKey2,byte [] plaintext,byte [] ciphertext) {
		
		print(rawKey1);
		System.out.print("   ");
		
		print(rawKey2);
		System.out.print("   ");
	
		print(plaintext);
		System.out.print("   ");
		
		print(ciphertext);
		System.out.println();
		
	}
	
	private static void print(byte[] myArr) {
		
		for(int i=0; i<myArr.length;i++) {
			System.out.print(myArr[i]);
		}
	}
}
