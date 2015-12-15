package SplitWords;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Main.DictionaryReader;

public class Splitter {
	
	DictionaryReader dr = null;
	DictionaryReader2 dr2 = null;
	public PriorityQueue<String> pqSplitter = null;

	public Splitter(String dir, String word) throws IOException	{
		dr = new DictionaryReader(dir);
		dr2 = new DictionaryReader2(dir);

		pqSplitter = new PriorityQueue<String>(10, new Comparator<String>(){
			public int compare(String a, String b)	{
				String[] str1 = a.split("\\s");
				String[] str2 = b.split("\\s");
				if(str1.length > str2.length) return 1;
				else if(str1.length == str2.length) {
					long x = getIndividualCounts(a);
					long y = getIndividualCounts(b);
					if(x<y) return 1;
					else if(x==y) return 0;
					else return -1;
				}
				else return -1;
			}
		});	
	}
	
	public  long getIndividualCounts(String w)	{
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(w);
		long count = 0L;
		while(m.find())
			count += dr.dictionary.get(m.group());				
		return count;
	}
	
	public void splitWord(CharSequence word, int length, String al) throws IOException	{
		if(length==0)	{				
			pqSplitter.add(al);
			return;			
		}
				
		Pattern p = Pattern.compile("[a-z&&[^ai]]");
		
		for(int i=0 ; i<word.length() ; i++)	{
			Matcher m = p.matcher(word.subSequence(0, i+1));
			if(dr.dictionary.containsKey(word.subSequence(0, i+1)) && !m.matches() && dr2.dictionary2.contains(word.subSequence(0, i+1)))	{
				splitWord(word.subSequence(i+1, word.length()), word.subSequence(i+1, word.length()).length(), al + " " + word.subSequence(0, i+1));
			}
		}
	}

}
