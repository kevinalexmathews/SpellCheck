package ReadCorpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class CharsXYGenerator {
		
	HashMap<String, Long> charsXY = new HashMap<String, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	String dir;
	
    public static void main(String args[]) throws IOException	{
    	CharsXYGenerator r = new CharsXYGenerator();

    	r.demo();
    	r.dir = args[0];
    	
    	ContextMatrixGenerator bfg = new ContextMatrixGenerator(r.dir);
    	bfg.loadFileNamesofBrownCorpus(r.dir);
    	
    	for(String str : bfg.file_names)
        	r.init(new File("data/brown_notags/" + str));    		

    	long total=0L;    	
        PrintWriter writer = new PrintWriter("data/trainedBigrams", "UTF-8");
        for (String key : r.charsXY.keySet()) {
            writer.println(key + " " + r.charsXY.get(key));
            System.out.println(key + " " + r.charsXY.get(key));
            total += r.charsXY.get(key);
        }
                               
        writer.close();
        
        System.out.println("Bye Bye!" + total);    	       
	}
	
    public void demo()	{
    	ReverseAlphabet ra = new ReverseAlphabet();
    	
    	String yoyo = null;
    	
    	for(int i=0 ; i<26 ; i++)	{
    		for(int j=0 ; j<26 ; j++)	{
            	yoyo = "" + ra.alphabet.get(i) + ra.alphabet.get(j); 
    			charsXY.put(yoyo, 1L);    			
    		}
    	}    	
    }
    
	public void init(File file1) throws IOException	{
        int r;
        br = new BufferedReader(new FileReader(file1));
        Character pre = 0;
        
    	while((r=br.read()) !=-1)        {    		
    		
    		Character letter = (char) r;
        	letter = Character.toLowerCase(letter);
    		
            if(Character.isLetter(letter) && Character.isLetter(pre)){                
            	String bigram = pre.toString() + letter.toString();

            	charsXY.put(bigram, charsXY.get(bigram) + 1L);
            }
            pre = letter;
        }
	}
		
}
