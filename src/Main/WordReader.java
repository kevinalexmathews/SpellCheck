package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class WordReader {
		
	public HashMap<String, Long> wordlist = new HashMap<String, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	long total_words = 0L;
	
    public WordReader(String dir) throws IOException	{
       	init(new File(dir + "trainedWords"));    		
	}
	
	public void init(File file1) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file1));
        
    	while((line=br.readLine()) != null)        {    		
    		String str[] = line.split("\\s");
    		wordlist.put(str[0].toLowerCase(), Long.parseLong(str[1]));    		    		
        	total_words += Long.parseLong(str[1]);
        }
	}
		
}