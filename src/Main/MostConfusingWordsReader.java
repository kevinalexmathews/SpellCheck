package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MostConfusingWordsReader {
	
	BufferedReader br = null;
	public HashMap<String, ArrayList<String>> mostConfusingWords = new HashMap<String, ArrayList<String>>(); 
	
	public MostConfusingWordsReader(String dir) throws IOException	{
		init(new File(dir + "mostConfusingWords"));
	}
	
	public void init(File file) throws IOException	{
		br = new BufferedReader(new FileReader(file));

		String line;
		while((line = br.readLine()) != null)	{
			
			ArrayList<String> al = new ArrayList<String>();

			Pattern p = Pattern.compile("[\\w']+");
            Matcher m = p.matcher(line);	           
            while ( m.find() )
            		al.add(m.group().toLowerCase());

			for(String s : al)	{
				ArrayList<String> al2 = new ArrayList<String>();
				al2.addAll(al);
//				al2.remove(s);
				mostConfusingWords.put(s, al2);
			}					
		}

//		for(String s : mostConfusingWords.keySet())	{
//			System.out.print(s + " ");
//			for(String y : mostConfusingWords.get(s))
//				System.out.print(y + " ");
//			System.out.println();
//		}
	}
}