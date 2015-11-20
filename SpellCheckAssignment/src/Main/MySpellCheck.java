package Main;

import java.io.BufferedReader;
import SplitWords.Splitter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.language.Metaphone;
import ReadCorpus.ContextMatrixGenerator;

public class MySpellCheck {
	
	PriorityQueue<Dummy> pq = null;
	EditDistance ed = null;
	UniReader ur = null;
	BiReader br = null;
	WordReader wr = null;
	ConfusionMatrixReader cmr = null;
	BiGramInvertedIndexReader bgir = null;
	TriGramInvertedIndexReader tgir = null;
	Metaphone m = null;
	MostConfusingWordsReader mcwr = null;
	StopWordsReader swr = null;
	ContextMatrixGenerator cmg = null;
	ArrayList<String> candidates = null;
	final long startTime = System.nanoTime();
	PrintWriter pw = null;
	private BufferedReader bReader;
	
	public static void main(String args[]) throws IOException	{
		MySpellCheck sc = new MySpellCheck();
		
		sc.ed = new EditDistance(args[0]);
		sc.wr =  new WordReader(args[0]);
		sc.ur  = new UniReader(args[0]);
		sc.br = new BiReader(args[0]);
		sc.cmr = new ConfusionMatrixReader(args[0]);
		sc.bgir = new BiGramInvertedIndexReader(args[0]);
		sc.tgir = new TriGramInvertedIndexReader(args[0]);
		sc.mcwr = new MostConfusingWordsReader(args[0]);
		sc.swr = new StopWordsReader(args[0]);
		sc.cmg = new ContextMatrixGenerator(args[0]);
		sc.pw = new PrintWriter(args[0] + "outputFile");
		sc.m = new Metaphone();		
		
		sc.readInputFile(args[0], args[0] + "inputFile");
		sc.pw.close();

		final long duration = System.nanoTime() - sc.startTime;
		final double seconds = ((double) duration / 1000000000);
		System.out.println("solution Time : " + new DecimalFormat("#.##########").format(seconds) + " Seconds");
	}
    
	public void readInputFile(String dir, String file) throws IOException	{
        bReader = new BufferedReader(new FileReader(file));
        
        String line = null;
		while((line = bReader.readLine()) != null)
    		parseSentence(dir, line.toLowerCase());		
	}
	
	public void parseSentence(String dir, String sentence) throws IOException	{
		ArrayList<String> words = new ArrayList<String>();	
		Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(sentence);           
        while ( m.find() )	            	
        		words.add(m.group());
		
		for(int i=0 ; i<words.size() ; i++)	{
			String word = words.get(i);
			String next_word = "";
			try	{
			next_word = words.get(i+1);
			}catch(Exception e)	{
				
			}
				
    		pq = new PriorityQueue<Dummy>(10, new Comparator<Dummy>(){
    			public int compare(Dummy a, Dummy b)	{
    				if(a.posterior < b.posterior) return 1;
    				else if(a.posterior == b.posterior) return 0;
    				else return -1;
    			}
			});		
//		    System.out.println("Pivot word is " + word+ ": "); 	
    		
    		if(!wr.wordlist.containsKey(word))	{
				int num_of_suggestions = words.size()==1?10:3;

				if(word.length()<6)
					getBigramCandidates(word);  			//generating candidates list
				else
					getTrigramCandidates(word);
//        		getPhoneticCandidates(word);			//add to candidates list
    			calculatePosteriorProbability(word); 		//ranking candidates
    			displaySuggestions(dir, word, num_of_suggestions);	//displaying candidates            		
        	}     
        	else if(mcwr.mostConfusingWords.containsKey(word))	{
    			ArrayList<String> input_text_context_words = new ArrayList<String>();						
    			int context_length = 6;
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
    				
    				if(!swr.stopwords.contains(context_word) && !context_word.isEmpty())
    					input_text_context_words.add(context_word);    				
    			}
					
				for(String str : mcwr.mostConfusingWords.get(word))	{
					double prob = 1;
					for(String s : cmg.hashMap.get(str).keySet())	{
						if(input_text_context_words.contains(s))
							prob = prob * (double)((double)cmg.hashMap.get(str).get(s) / (double)wr.wordlist.get(str)); 	 //Calculate Likelihood				
					}
					prob = prob * calculatePriorProbability(str);		 //Calculate prior
					pq.add(new Dummy(str, 0, 0L, 0D, 0D, prob*100000));									
				}
				displaySuggestions(dir, word, 3);
        	}
        	else        	{
    			if(!next_word.isEmpty())	{
            		String str = "" + word + next_word;
            		DictionaryReader dr = new DictionaryReader(dir);
            		if(dr.dictionary.containsKey(str) && dr.dictionary.get(str) > dr.dictionary.get(word))	{		
                		System.out.format("%10s\t%10s\t%10s\t\n", word, str, dr.dictionary.get(str));            			
                		pw.format("%10s\t%10s\t%10s\t\n", word, str, dr.dictionary.get(str));            			
        			}
        		}        		
    		}
        }    		
	}
	
	public void displaySuggestions(String dir, String query, int num_of_suggestions) throws IOException	{		
		System.out.format("%10s\t", query);
		pw.format("%10s\t", query);

		if(pq.isEmpty())	{
			getTopSuggestionFromSplitter(dir, query);
		}
		else {
			int j = 0;
			while(!pq.isEmpty())	{
				if((j++)==num_of_suggestions) break;
				
				if(j==3)	{
					getTopSuggestionFromSplitter(dir, query);
					continue;
				}
				
				Dummy d = pq.poll();
				System.out.format("%10s\t%10s\t", d.str, d.posterior*100000);
				pw.format("%10s\t%10s\t", d.str, d.posterior*100000);
			}			
		}
		System.out.format("\n");
		pw.format("\n");

//		int i=0;
//		System.out.println("Suggestions for " + query + " are:");
//		System.out.format("%15s %15s %15s %15s %15s %15s \n", "Word", "EditDistance", "WordCount", "PriorProb", "Likelihood", "Posterior");
//		while(!pq.isEmpty())	{
//			Dummy d = pq.poll();
//			if((i++)==num_of_suggestions) break;
//			System.out.format("%15s %15d %15d %15f %15f %15f \n", d.str, d.edit_distance, d.word_count, d.prior_prob*100000, d.likelihood*100000, d.posterior*100000);
//		}
		
	}
	
	public void getTopSuggestionFromSplitter(String dir, String query) throws IOException	{
		Splitter s = new Splitter(dir, query);
		s.splitWord(query, query.length(),"");
				
		if(!s.pqSplitter.isEmpty())	{
			String w = s.pqSplitter.poll();
			long count = s.getIndividualCounts(w);
			System.out.format("%10s\t%10s\t", w, count);			
			pw.format("%10s\t%10s\t", w, count);			
		}
	}
	
	public void calculatePosteriorProbability(String query) throws IOException	{
		for(String str : candidates)	{
			int k = ed.minDistance(str, query);
			if( k <= 3)	{				
				double a = calculatePriorProbability(str);
				double b = ed.getLikelihood(str, query);				
				pq.add(new Dummy(str, k, wr.wordlist.get(str), a, b, a*b));
			}
		}
	}
	
	public double calculatePriorProbability(String query)	{
		double prior_prob = 0;
		long query_count=0;
		
		try	{
			query_count = wr.wordlist.get(query);
		}catch(NullPointerException e)	{
		}	
		
		prior_prob = (double) (query_count + 0.5D)/wr.total_words ;
		return prior_prob;
	}	

	public void getTrigramCandidates(String query) throws IOException	{
    	candidates = new ArrayList<String>();
    	
    	for(int i=0 ; i<query.length()-2 ; i++)	{
    		String trigram = "" + query.charAt(i) + query.charAt(i+1) + query.charAt(i+2);
    		for(String str : tgir.hashMap.get(trigram))	{  			   			
    			if(!candidates.contains(str))
    				candidates.add(str);    			    			    				
    		}
    	}   	
//    	System.out.println("Number of words generated from trigrams of " + query + " is "  + candidates.size());
    }
	
    public void getBigramCandidates(String query) throws IOException	{
    	candidates = new ArrayList<String>();
    	
    	for(int i=0 ; i<query.length()-1 ; i++)	{
    		String bigram = "" + query.charAt(i) + query.charAt(i+1);
    		for(String str : bgir.hashMap.get(bigram))	{    			   			
    			if(!candidates.contains(str))
    				candidates.add(str);    			    			    				
    		}
    	}   	
//    	System.out.println("Number of words generated from bigrams of " + query + " is "  + candidates.size());
    }
    
	public void getPhoneticCandidates(String query) throws IOException	{
		for(String str : wr.wordlist.keySet())
			if(m.isMetaphoneEqual(query,  str))	{
				double a = calculatePriorProbability(str);
				double b = 1.0;				
				pq.add(new Dummy(str, ed.minDistance(str, query), wr.wordlist.get(str), a, b, a*b));				
			}
	}

}