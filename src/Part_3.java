import java.lang.String;
public class Part_3 {
	public static String SDES_Encoding(byte[] key, String message)
	{
		byte[] messageconverted= CASCII.Convert(message);
		byte[] SDESencrypted= SDES.Encrypt(key, messageconverted);
		
		return  CASCII.toString(SDESencrypted);
	}
	public static String msg1_Decode(String msg)
	{
		byte[] msgbyte=msgtobyte(msg);
		byte[] key = { 0,0,0,0,0,0,0,0,0,0};
		String output="";
		for (int i=0;i<1024;i++)
		{
			
			byte[] decrypted=SDES.Decrypt(key, msgbyte);
			key=incramentByteArray(key);
			String Sdecrypted=CASCII.toString(decrypted);
			if(validstring(Sdecrypted))
			{
				System.out.println(Sdecrypted);
			}
		}
		
		
		return output;
	}
	public static String msg2_Decode(String msg)
	{
		byte[] msgbyte=msgtobyte(msg);
		
		byte[] key2 = { 0,0,0,0,0,0,0,0,0,0};
		String output="";
		for (int i=0;i<1024;i++)
		{
			byte[] key1 = { 0,0,0,0,0,0,0,0,0,0};
			for (int j=0;j<1024;j++)
			{
				byte[] decrypted=TripleSDES.Decrypt(key1, key2, msgbyte);
				key1=incramentByteArray(key1);
				String Sdecrypted=CASCII.toString(decrypted);
				if(validstring(Sdecrypted))
				{
					System.out.println(Sdecrypted);
				}
			}
			key2=incramentByteArray(key2);
			
		}
		
		
		return output;
	}
	public static void print_byte_array(byte[] output)
	{
		for(int i=0;i<output.length;i++)
		{
			System.out.print(output[i]);
			
		}
		System.out.print('\n');
	}
	private static boolean validstring(String msg){
		for(int i = 0; i < msg.length() - 1; i++){
			char c = msg.charAt(i);
			if(c == '.' || c == '?' || c == ',' || c == ':'){
		
					if(i+1<msg.length() && msg.charAt(i + 1) != ' ')
						return false;
			
					
			}
		}
		return msg.toUpperCase().contains("THE");
	}
	public static byte[] incramentByteArray(byte[] incoming)
	{
		byte remainder=1;
		for (int i=0;i<incoming.length;i++)
		{
			
			byte temp=incoming[incoming.length-1-i];
			incoming[incoming.length-1-i]=(byte) ((temp==0)?1:0);
			remainder=temp;
			
			
			if(remainder==0)
			{
				return incoming;
			}
			
		}
		
		
		
		return incoming;
	}
	public static byte[] msgtobyte(String msg)
	{
		byte output[]=new byte[msg.length()];
		
		for(int i=0;i<msg.length();i++)
		{
			output[i]=(byte) ((msg.charAt(i)=='1')?1:0);
			
		}
		
		return output;
		
		
	}

	
	
}


