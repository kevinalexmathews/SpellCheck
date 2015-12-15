package ReadCorpus;

import Main.DictionaryReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordGenerator {
		
	HashMap<String, Long> hashMap = new HashMap<String, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br1 = null, br2 = null;
	DictionaryReader dr = null;
	String dir;
	
    public static void main(String args[]) throws IOException	{
    	WordGenerator r = new WordGenerator();
    	r.dir = args[0];
    	ContextMatrixGenerator bfg = new ContextMatrixGenerator(r.dir);
    	bfg.loadFileNamesofBrownCorpus(r.dir);
        r.dr = new DictionaryReader(r.dir);
    	
    	for(String str : bfg.file_names)
        	r.init(new File("data/brown_notags/" + str));    		

      long total = 0L;    	
   	  PrintWriter writer = new PrintWriter("data/trainedWords", "UTF-8");
      for (String key : r.hashMap.keySet()) {
    	  writer.println(key + " " + r.hashMap.get(key));
    	  total += r.hashMap.get(key);
      }
                            
     writer.close();
     System.out.println("Bye Bye!" + total);    	  	
	}
	
	public void init(File file1) throws IOException	{
        String line1 = null;
        String word = null;
        br1 = new BufferedReader(new FileReader(file1));
                
    	while((line1 = br1.readLine()) != null)        {    		
    		Pattern p = Pattern.compile("\\w+");
            Matcher m = p.matcher(line1);
            while ( m.find() ) {
                word = (line1.substring(m.start(), m.end()));
                word = word.toLowerCase();
                
                if(dr.dictionary.containsKey(word))	{
                    if(!hashMap.containsKey(word))            	
                    	hashMap.put(word, 1L);
                    else
                    	hashMap.put(word, hashMap.get(word) + 1);	
//                	System.out.println(file1 + " " + word + " " + hashMap.get(word));
                }
            }           
        }
    	br1.close();
	}		
}
