package Metaphone;

import Main.Alphabet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class myMetaphone {

	Alphabet alpha = new Alphabet();
	
	public static void main(String args[])	{
		myMetaphone s = new myMetaphone();
		String word = "knFishpool";
		
		word = word.toLowerCase();
		word = s.removeDuplicates(word);		
		word = s.updateBeginning(word);
		
		System.out.println(word);
	}
	
	public String updateBeginning(String query)	{

		String[] str = {"kn", "gn", "pn", "ae", "wr"};

		for(String regex : str)	{
			Pattern p = Pattern.compile("^" + regex);
			Matcher m = p.matcher(query);		
			if(m.find())
				query = query.replaceFirst(p.pattern(), "" + regex.charAt(1));		
		}
			
		return query;		
	}
	
	public String removeDuplicates(String query)	{		
		
		for(Character str : alpha.alphabet.keySet())	{
			String regex = "" + str + '+'; 
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(query);		
			if(m.find())
				query = query.replaceAll(p.pattern(), "" + str);
		}
		
		return query;		
	}

}
