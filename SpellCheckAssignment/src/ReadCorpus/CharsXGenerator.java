package ReadCorpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class CharsXGenerator {
		
	HashMap<Character, Long> charsX = new HashMap<Character, Long>();
	Scanner inp = new Scanner(System.in);
	BufferedReader br = null;
	String dir;
	
    public static void main(String args[]) throws IOException	{
    	CharsXGenerator r = new CharsXGenerator();
    	r.dir = args[0];
    	ContextMatrixGenerator bfg = new ContextMatrixGenerator(r.dir);
    	bfg.loadFileNamesofBrownCorpus(r.dir);
    	
    	for(String str : bfg.file_names)
        	r.init(new File("data/brown_notags/" + str));    		

    	long count=0L;
        PrintWriter writer = new PrintWriter("data/trainedUnigrams", "UTF-8");
        for (Character key : r.charsX.keySet()) {
            writer.println(key + " " + r.charsX.get(key));
            System.out.println(key + " " + r.charsX.get(key));
            count += r.charsX.get(key);
        }
        
        writer.close();
        System.out.println("Bye Bye!" + count);    	
	}
	
	public void init(File file1) throws IOException	{
        int r;
        br = new BufferedReader(new FileReader(file1));
        
    	while((r=br.read()) !=-1)        {    		
    		Character letter = (char) r;
    		letter = Character.toLowerCase(letter);
        	
            if(Character.isLetter(letter))
            if(!charsX.containsKey(letter))	{
        		charsX.put(letter, 0L);
        	}
        	else	{
        		charsX.put(letter, charsX.get(letter) + 1);
        	}        		
        }
	}
		
}
