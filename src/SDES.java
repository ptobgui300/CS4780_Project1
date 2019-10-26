import java.util.Arrays;
public class SDES {

	private static final byte[][] S0_TABLE = {
		{1, 0, 3, 2}, 
		{3, 2, 1, 0}, 
		{0, 2, 1, 3}, 
		{3, 1, 3, 2}
	};
	
	private static final byte[][] S1_TABLE = {
		{0, 1, 2, 3}, 
		{2, 0, 1, 3}, 
		{3, 0, 1, 0}, 
		{2, 1, 0, 3}
	};

	public void start() {
		runEncrypt();
		runDecrypt();
	}

	private static void runEncrypt() {
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		key[2] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };

		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };
		plaintext[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };

		for(int i = 0; i < 4; i++) {
			ciphertext[i] = SDES.Encrypt(key[i], plaintext[i]);
			printArray(key[i]);
			System.out.print("\t");
			printArray(plaintext[i]);
			System.out.print("\t");
			printArray(ciphertext[i]);
			System.out.println();
		}
	}

	private static void runDecrypt() {
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[2] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };

		ciphertext[0] = new byte[]{ 0, 0, 0, 1, 1, 1, 0, 0 };
		ciphertext[1] = new byte[]{ 1, 1, 0, 0, 0, 0, 1, 0 };
		ciphertext[2] = new byte[]{ 1, 0, 0, 1, 1, 1, 0, 1 };
		ciphertext[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 0, 0 };

		for(int i = 0; i < 4; i++) {
			plaintext[i] = SDES.Decrypt(key[i], ciphertext[i]);
			printArray(key[i]);
			System.out.print("      ");
			printArray(plaintext[i]);
			System.out.print("        ");
			printArray(ciphertext[i]);
			System.out.println();
		}
	}

	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);


		int size = (int) Math.ceil(plaintext.length / 8) * 8;
		byte[] ciphertext = new byte[size];

		for(int i = 0; i < plaintext.length; i += 8) {
			byte[] subplaintext = Arrays.copyOfRange(plaintext, i, i+8);
			byte[] temp = EncryptBlock(key1, key2, subplaintext);
			for(int j = 0; j < 8; j++) {
				ciphertext[j + i] =  temp[j];
			}
		}
		return ciphertext;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);


		int size = (int) Math.ceil(ciphertext.length / 8) * 8;
		byte[] plaintext = new byte[size];

		for(int i = 0; i < ciphertext.length; i += 8) {
			byte[] subciphertext = Arrays.copyOfRange(ciphertext, i, i+8);
			byte[] temp = DecryptBlock(key1, key2, subciphertext);
			for(int j = 0; j < 8; j++) {
				plaintext[j + i] =  temp[j];
			}
		}
		return plaintext;
	}


	public static byte[] EncryptBlock(byte[] key1, byte[] key2, byte[] plaintext) {
		byte[] temp = iPermutation(plaintext);
		fkFunction(temp, key1);
		temp = switchFunction(temp);
		fkFunction(temp, key2);
		temp = fPermutation(temp);
		return temp;
	}
	
	public static byte[] DecryptBlock(byte[] key1, byte[] key2, byte[] ciphertext) {
		byte[] temp = iPermutation(ciphertext);
		fkFunction(temp, key2);
		temp = switchFunction(temp);
		fkFunction(temp, key1);
		temp = fPermutation(temp);
		return temp;
	}
	
	private static void generateKeys(byte[] rawkey, byte[] k1, byte[] k2) {
		byte[] afterP10 = keyGen10Permutation(rawkey);
		byte[] afterS1 = shiftKeyGen(afterP10, 1);
		keyGen8Permutation(afterS1, k1);
		byte[] afterS2 = shiftKeyGen(afterS1, 2);
		keyGen8Permutation(afterS2, k2);
	}

	private static byte[] shiftKeyGen(byte[] input, int shiftAmount) {
		byte[] output = new byte[10];
		output[0] = input[(0 + shiftAmount) % 5];
		output[1] = input[(1 + shiftAmount) % 5];
		output[2] = input[(2 + shiftAmount) % 5];
		output[3] = input[(3 + shiftAmount) % 5];
		output[4] = input[(4 + shiftAmount) % 5];
		output[5] = input[(0 + shiftAmount) % 5 + 5];
		output[6] = input[(1 + shiftAmount) % 5 + 5];
		output[7] = input[(2 + shiftAmount) % 5 + 5];
		output[8] = input[(3 + shiftAmount) % 5 + 5];
		output[9] = input[(4 + shiftAmount) % 5 + 5];
		
		return output;
	}
	
	private static byte[] keyGen10Permutation(byte[] input) {
		byte[] output = new byte[10];
		output[0] = input[2];
		output[1] = input[4];
		output[2] = input[1];
		output[3] = input[6];
		output[4] = input[3];
		output[5] = input[9];
		output[6] = input[0];
		output[7] = input[8];
		output[8] = input[7];
		output[9] = input[5];
		
		return output;
	}
	
	private static void keyGen8Permutation(byte[] input, byte[] output) {
		if (input == null) {
			System.out.println("Error: Got null for input");
			System.exit(1);
		}
		if (output == null) {
			System.out.println("Error: Got null for output");
			System.exit(1);
		}
		if (input.length != 10) {
			System.out.println("Error: Got input of incorrect size: " + input.length + " instead of 10");
			System.exit(1);
		}		
		if (output.length != 8) {
			System.out.println("Error: Got output of incorrect size: " + input.length + " instead of 10");
			System.exit(1);
		}
		
		output[0] = input[5];
		output[1] = input[2];
		output[2] = input[6];
		output[3] = input[3];
		output[4] = input[7];
		output[5] = input[4];
		output[6] = input[9];
		output[7] = input[8];
	}
	
	private static byte[] iPermutation(byte[] input) {
		byte[] output = new byte[8];
		output[0] = input[1];
		output[1] = input[5];
		output[2] = input[2];
		output[3] = input[0];
		output[4] = input[3];
		output[5] = input[7];
		output[6] = input[4];
		output[7] = input[6];
		
		return output;
	}
	
	private static byte[] fPermutation(byte[] input) {
		byte[] output = new byte[8];
		output[0] = input[3];
		output[1] = input[0];
		output[2] = input[2];
		output[3] = input[4];
		output[4] = input[6];
		output[5] = input[1];
		output[6] = input[7];
		output[7] = input[5];
		
		return output;
	}
	
	private static byte[] switchFunction(byte[] input) {
		
		byte[] output = new byte[8];
		output[0] = input[4];
		output[1] = input[5];
		output[2] = input[6];
		output[3] = input[7];
		output[4] = input[0];
		output[5] = input[1];
		output[6] = input[2];
		output[7] = input[3];
		
		return output;
	}
	
	private static void fkFunction(byte[] input, byte[] key) {
		byte[] fromF = F(input, key);
		for(int i = 0; i < 4; i++)
			input[i] = (byte)(input[i] ^ fromF[i]);
	}
	
	private static byte[] F(byte[] input, byte[] key) {
		
		byte[] temp = new byte[8];
		temp[0] = input[7];
		temp[1] = input[4];
		temp[2] = input[5];
		temp[3] = input[6];
		temp[4] = input[5];
		temp[5] = input[6];
		temp[6] = input[7];
		temp[7] = input[4];
		
		
		for(int i = 0; i < 8; i++)
			temp[i] = (byte)(temp[i] ^ key[i]);
		
		s0(temp);
		s1(temp);
		
		byte[] output = new byte[4];
		output[0] = temp[1];
		output[1] = temp[3];
		output[2] = temp[2];
		output[3] = temp[0];
		
		return output;
	}
	
	private static void s0(byte[] input) {
		byte row = (byte)(input[0] * 2 + input[3]);
		byte col = (byte)(input[1] * 2 + input[2]);
		
		byte value = S0_TABLE[row][col];
		
		input[0] = (byte)(value / 2);
		input[1] = (byte)(value % 2);
	}
	
	private static void s1(byte[] input) {
		byte row = (byte)(input[4] * 2 + input[7]);
		byte col = (byte)(input[5] * 2 + input[6]);
		
		byte value = S1_TABLE[row][col];
		
		input[2] = (byte)(value / 2);
		input[3] = (byte)(value % 2);
	}

	public static void printArray(byte[] array) {
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i]);
	}

}