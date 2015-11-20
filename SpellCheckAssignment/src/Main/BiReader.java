package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class BiReader {
		
	HashMap<String, Long> charsXY = new HashMap<String, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	
    BiReader(String dir) throws IOException	{
       	init(new File(dir + "trainedBigrams"));    		
	}
	
	public void init(File file1) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file1));
        
    	while((line=br.readLine()) != null)        {    		
    		String str[] = line.split("\\s");
    		charsXY.put(str[0], Long.parseLong(str[1]));    		    		
        }
	}
		
}