package ReadCorpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Main.DictionaryReader;

public class TriGramInvertedIndexGenerator {
		
	ReverseAlphabet alpha = new ReverseAlphabet();
	HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	ArrayList<String> al;
	DictionaryReader dr = null;
	private static PrintWriter pw;
	String dir;
	
    public static void main(String args[]) throws IOException	{
    	TriGramInvertedIndexGenerator r = new TriGramInvertedIndexGenerator();
    	r.dir = args[0];
    	r.init(new File(r.dir + "triGramInvertedIndex"));
	}

	public void init(File file) throws IOException	{	
    	pw = new PrintWriter(file);
		dr = new DictionaryReader(dir);
    	
		for(int i=0 ; i<26 ; i++)
		{
			for(int j=0 ; j<26 ; j++)
			{
				for(int k=0 ; k<26 ; k++)
				{
					String str = "" + alpha.alphabet.get(i) + alpha.alphabet.get(j) + alpha.alphabet.get(k);
		    		pw.write(str);
					
		            al = new ArrayList<String>();		
		    		String line2 = null;		    		
					br = new BufferedReader(new FileReader(dir + "count_1w100k"));

		    		while((line2 = br.readLine()) != null)	{
			    		Pattern p = Pattern.compile("[\\w']+");
			            Matcher m = p.matcher(line2);	           
			            while ( m.find() ) {	            	
			            	if(m.group().toLowerCase().contains(str)){
			            		al.add(m.group().toLowerCase());
			            	}
			            }
					}						
		    		
					System.out.println(str +  " " + al.size());
					hashMap.put(str, al);
		
					for(String data : al)
		        		pw.write(" " + data);
		    		pw.write("\n");
				}				
			}
		}
		pw.close();
	}		
}