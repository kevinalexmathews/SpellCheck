package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfusionMatrixReader {
	
	BufferedReader br = null;
	int am[][] = new int[26][26];
	int dm[][] = new int[27][26];
	int sm[][] = new int[26][26];
	int rm[][] = new int[26][26];
	
	public ConfusionMatrixReader(String dir) throws IOException	{				
		init(new File(dir + "additionmatrix"), am);
		init(new File(dir + "deletionmatrix"), dm);
		init(new File(dir + "substitutionmatrix"), sm);
		init(new File(dir + "reversalmatrix"), rm);
	}
	
	public void init(File file, int am[][]) throws IOException	{
		br = new BufferedReader(new FileReader(file));
		String line;
		
		int i=0, j=0;
		while((line = br.readLine())!=null)	{
			Pattern p = Pattern.compile("[\\d]+");
			Matcher m = p.matcher(line);
			while(m.find())	{
				am[i][j++] = Integer.parseInt(m.group(),10);
			}

			if(j==26)	{
				j=0;
				i++;
			}
		}
	}
}

