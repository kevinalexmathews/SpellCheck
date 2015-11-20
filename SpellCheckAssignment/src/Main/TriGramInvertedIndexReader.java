package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TriGramInvertedIndexReader {
		
	HashMap<String, ArrayList<String>> hashMap = null;
	ArrayList<String> candidates = null;
	BufferedReader br = null;
	ArrayList<String> al;
	EditDistance ed = null;
	
    public TriGramInvertedIndexReader(String dir) throws IOException	{
    	this.hashMap = new HashMap<String, ArrayList<String>>();
    	
    	init(new File( dir + "triGramInvertedIndex"));
	}

	public void init(File file) throws IOException	{	
		br = new BufferedReader(new FileReader(file));
		String line = null;		    		

		while((line = br.readLine()) != null)	{
			String str[] = line.split("\\s");
			
	        al = new ArrayList<String>();		
			
			for(int i=1 ; i<str.length ; i++)
				al.add(str[i]);

			hashMap.put(str[0], al);		
		}								
	}	
	
}