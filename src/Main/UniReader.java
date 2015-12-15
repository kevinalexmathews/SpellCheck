package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class UniReader {
		
	HashMap<String, Long> charsX = new HashMap<String, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	
    public UniReader(String dir) throws IOException	{
       	init(new File(dir + "trainedUnigrams"));    		
	}
	
	public void init(File file1) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file1));
        
    	while((line=br.readLine()) != null)        {    		
    		String str[] = line.split("\\s");
    		charsX.put(str[0], Long.parseLong(str[1]));    		    		
        }
	}
		
}