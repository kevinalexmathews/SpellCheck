package SplitWords;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryReader2 {
		
	public ArrayList<String> dictionary2 = new ArrayList<String>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	public long total_words = 0L;
	
    public DictionaryReader2(String dir) throws IOException	{
       	init(new File(dir + "wordlist"));
	}
    

	public void init(File file) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file));
        
    	while((line=br.readLine()) != null)        {    		
    		String str[] = line.split("\\s");
    			dictionary2.add(str[0].toLowerCase());
        }
    	br.close();
	}
		
}