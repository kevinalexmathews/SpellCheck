package Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DictionaryReader {
		
	public HashMap<String, Long> dictionary = new HashMap<String, Long>();
	ArrayList<String> dictionary2 = new ArrayList<String>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	public long total_words = 0L;
	
    public DictionaryReader(String dir) throws IOException	{
       	init(new File(dir + "count_1w100k"));
	}
    
	public void init(File file) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file));
        
    	while((line=br.readLine()) != null)        {    		
    		String str[] = line.split("\\s");
    		
//    		if(dictionary2.contains(str[0].toLowerCase()))	{
    			dictionary.put(str[0].toLowerCase(), Long.parseLong(str[1]));
//   		}
    		
        	total_words += Long.parseLong(str[1]);
        }
    	br.close();
	}
		
}