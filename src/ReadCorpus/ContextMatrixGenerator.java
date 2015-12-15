package ReadCorpus;
import Main.DictionaryReader;
import Main.StopWordsReader;
import Main.WordReader;
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
import Main.MostConfusingWordsReader;

public class ContextMatrixGenerator {
		
	ReverseAlphabet alpha = new ReverseAlphabet();
	public HashMap<String, HashMap<String, Long>> hashMap = null;
	Scanner inp = null;
	BufferedReader br = null;
	WordReader wr = null;
	DictionaryReader dr = null;
	PrintWriter pw;
	StopWordsReader swr = null;
	ArrayList<String> file_names = null;
	MostConfusingWordsReader mcwr = null;
	
    public ContextMatrixGenerator(String dir) throws IOException	{    	
		dr = new DictionaryReader(dir);
		swr = new StopWordsReader(dir);
		mcwr = new MostConfusingWordsReader(dir);
		inp = new Scanner(System.in);
		file_names = new ArrayList<String>();
		hashMap = new HashMap<String, HashMap<String, Long>>();
		
		loadFileNamesofBrownCorpus(dir);
    	loadMostConfusingWords(new File(dir + "trainedWords"));	    	
    	for(String str : file_names)
        	loadInputFile(new File(dir + "brown_notags/" + str));    	
//    	for(int i=0 ; i<22 ; i++)
//    		loadInputFile(new File("/home/kevin/Documents/NLP/reuters21578/reut2-" + i + ".sgm"));
//		loadInputFile(new File("/home/kevin/Documents/NLP/bigcorpus"));
//    	printResults("/home/kevin/Documents/NLP/bigramFrequencies");
	}
    
    public void loadFileNamesofBrownCorpus(String dir) throws IOException	{
    	br = new BufferedReader(new FileReader(dir + "brown_notags/topics.txt"));
    	String line;
    	
    	while((line=br.readLine()) != null)	{
    		String[] str = line.split("\\s"); 
    		file_names.add(str[0]);    		
    	}    	
    }

	public void loadMostConfusingWords(File file) throws IOException	{
		for(String str : mcwr.mostConfusingWords.keySet())
			hashMap.put(str, new HashMap<String, Long>());	
	}	
	
	public void loadInputFile(File file) throws IOException	{
//		System.out.println("Hi");
	
		br = new BufferedReader(new FileReader(file));
		String line;
		
		ArrayList<String> text = new ArrayList<String>();		
		while((line = br.readLine()) != null)
        		text.add(line);
		
		StringBuilder listString = new StringBuilder();
		for(String str : text)
			listString.append(str + " ");
					
		String yoyo = listString.toString();
		String[] sentences = yoyo.split("[.|?|!|//]");
		for(String str : sentences)
    		parseSentence(str.toLowerCase());
	}
	
	public void parseSentence(String sentence) throws IOException	{	
		ArrayList<String> words = new ArrayList<String>();
		
		Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(sentence);           
        while ( m.find() )	            	
        		words.add(m.group());
        
		for(int i=0 ; i<words.size() ; i++)	{							
			String current_word = words.get(i);

			if(!mcwr.mostConfusingWords.containsKey(current_word)) //Ignore words not in the most confusing words list
				continue;
			
			ArrayList<String> corpus_context_words = new ArrayList<String>();						
			int context_length = 12;
			int current_position = i;
			for(int j=-context_length/2 ; j<=context_length/2 ; j++)	{
				
				if(j==0)
					continue;
			
				String context_word = "";
				try {
					context_word = words.get(current_position + j);
				}
				catch(Exception e)	{				
				}
				
				if(!swr.stopwords.contains(context_word) && !context_word.isEmpty()) //remove all stopwords
					corpus_context_words.add(context_word);
			}		
			
			for(String str : corpus_context_words)	{
				if(hashMap.get(current_word).keySet().contains(str))
					hashMap.get(current_word).put(str, hashMap.get(current_word).get(str) + 1 );
				else
					hashMap.get(current_word).put(str, 1L );
			}
		}
	}
	
	public void printResults(String file_name) throws IOException{
    	pw = new PrintWriter(file_name);
    	for(String str : hashMap.keySet())	{
			if(hashMap.get(str).keySet().isEmpty())
				continue;				
						
//			pw.format("%10s" + " " + "%10s" + " ", str, wr.dictionary.get(str));
			for(String s : hashMap.get(str).keySet())	{
//				pw.format("%10s" + " " + "%10s" + "", s, hashMap.get(str).get(s));
				pw.format("%10s" + " " + "%10s" + " " + "%10s" + " " + "%10s" + " " + "%10s" + "\n", str, dr.dictionary.get(str), s, hashMap.get(str).get(s), (double) ((double)hashMap.get(str).get(s))/((double)dr.dictionary.get(str)));
			}
		}
    	pw.close();    	
	}	
}