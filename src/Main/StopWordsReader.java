package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StopWordsReader {
		
	public ArrayList<String> stopwords = new ArrayList<String>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	
    public StopWordsReader(String dir) throws IOException	{    	
       	init(new File( dir + "stopwords"));       	
	}
	
	public void init(File file1) throws IOException	{
        String line;
        br = new BufferedReader(new FileReader(file1));
        
    	while((line=br.readLine()) != null)        {    		
    		Pattern p = Pattern.compile("[\\w']+");
            Matcher m = p.matcher(line);	           
            while ( m.find() )        	
        		stopwords.add(m.group());    		    		
        }
	}
		
}