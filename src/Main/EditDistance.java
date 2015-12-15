package Main;

import java.io.IOException;

class EditDistance	{
	
	int[][] dp;
	int[][] ptr1;
	int[][] ptr2;
	
	ConfusionMatrixReader cmr;
	UniReader ur;
	BiReader br;
	Alphabet alpha;
	
	char yo;
	char yoyo;
	
	int len1;
	int len2;
	
	double demo = 0.5;
	
	double sub_val = 0.5;
	double del_val = 0.2;
	double add_val = 0.2;
	double rev_val = 0.1;
	
	public EditDistance(String dir) throws IOException {
		cmr = new ConfusionMatrixReader(dir);
		ur = new UniReader(dir);
		br = new BiReader(dir);
		alpha = new Alphabet();		
	}

	public int minDistance(String word1, String word2) throws IOException {				
		len1 = word1.length();
		len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		dp = new int[len1 + 1][len2 + 1];
		ptr1 = new int[len1 + 1][len2 + 1];
		ptr2 = new int[len1 + 1][len2 + 1];
		
//		dp[0][0] = 0;
		ptr1[0][0] = 0;
		ptr2[0][0] = 0;
		
		for (int i = 1; i <= len1; i++) {
			dp[i][0] = i;
			ptr1[i][0] = i-1;
			ptr2[i][0] = 0;
		}
	 
		for (int j = 1; j <= len2; j++) {
			dp[0][j] = j;
			ptr1[0][j] = 0;
			ptr2[0][j] = j-1;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
					ptr1[i + 1][j + 1] = i;
					ptr2[i + 1][j + 1] = j;
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					ptr1[i + 1][j + 1] = i;
					ptr2[i + 1][j + 1] = replace > insert ? j+1 : j;
					
					min = delete > min ? min : delete;
					ptr1[i + 1][j + 1] = delete > min ? i : i+1;
					ptr2[i + 1][j + 1] = delete > min ? ptr2[i + 1][j + 1] : j;
					
					if(replace == insert)
						if(replace == delete)	{
							min = replace;							
							ptr1[i + 1][j + 1] = i;
							ptr2[i + 1][j + 1] = j;
						}
					
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];	
	}	
	
	public double getLikelihood(String word1, String word2)	{		

//		System.out.println(word1 + " " + word2);
		
		for(int i=0 ; i<=len1 ; i++)	{
			for(int j=0 ; j<=len2 ; j++)	{
//				System.out.print(dp[i][j] + "\t");				
			}
	//		System.out.println("");			
		}
		
		for(int i=0 ; i<=len1 ; i++)	{
			for(int j=0 ; j<=len2 ; j++)	{
		//		System.out.print(ptr1[i][j] + "" + ptr2[i][j] + "\t");				
			}
	//	System.out.println("");			
		}
		
		int i = len1;
		int j = len2;
		double likelihood = 1;
		
		while(true)	{
			int m = ptr1[i][j];
			int n = ptr2[i][j];

//			System.out.println(i + " " + j + " " + m + " " + n);
			
			if(dp[i][j] != dp[m][n])	{				

				if(i==m && j==n+1){										
					char x, y;
					if(j==1)	{
						x = '@';				
					}
					else	{
						if(i==0)
						 x = word2.charAt(j);
						else
						 x = word1.charAt(i-1);						
					}
					y = word2.charAt(j-1);						

//					System.out.println("Addition - add[" + x + "," + y + "]");
										
					int a = alpha.alphabet.get(x);
					int b = alpha.alphabet.get(y);
					likelihood = (cmr.dm[a][b] + demo) * likelihood;
										
					String str;
					if(j==1)	{
						str = "" + y;
						likelihood = (double) likelihood / ur.charsX.get(str) * add_val;						
					}
					else	{
						str = "" + x; 
						likelihood = (double) likelihood / ur.charsX.get(str) * add_val;	 					
					}														
				}
				else if(i==m+1 && j==n)		{			
									
					char x, y;
					if(i==1)	{
						x = '@';				
					}
					else	{
						if(j==0)
						 x = word1.charAt(i);
						else
   						 x = word2.charAt(j-1); 						
					}
					y = word1.charAt(i-1);						

//					System.out.println("Deletion - del[" + x + "," + y + "]");

					int a = alpha.alphabet.get(x);
					int b = alpha.alphabet.get(y);
					likelihood = (cmr.dm[a][b] + demo) * likelihood;
					
					String str;
					if(i==1)	{
						str = "" + y;
						likelihood = (double) likelihood / ur.charsX.get(str) * del_val;						
					}
					else	{
						str = "" + x +  y;
						likelihood = (double) likelihood / br.charsXY.get(str) * del_val;						
					}					
				}
				else if(i==m+1 && j==n+1)	{
					
					String str;
					int flag = 0;
					try	{
						if(word1.charAt(i-2)==word2.charAt(j-1) && word1.charAt(i-1)==word2.charAt(j-2))
							flag = 1;
					}catch(Exception e) {}
					
					if(flag == 1)	{
//						System.out.println("Reversal - rev[" + word1.charAt(i-1) + "," + word2.charAt(j-1) + "]");

						int a = alpha.alphabet.get(word1.charAt(i-1));
						int b = alpha.alphabet.get(word2.charAt(j-1));
						likelihood = (cmr.rm[a][b] + demo) * likelihood;										

						str = "" + word1.charAt(i-1) + word2.charAt(j-1); 							
						likelihood = (double) likelihood / br.charsXY.get(str) * rev_val;
						
						int temp = m;
						m = ptr1[m][n];
						n = ptr2[temp][n];						
					}					
					else {
	//					System.out.println("Substitution - sub[" + word1.charAt(i-1) + "," + word2.charAt(j-1) + "]");
	
						int a = alpha.alphabet.get(word1.charAt(i-1));
						int b = alpha.alphabet.get(word2.charAt(j-1));
						likelihood = (cmr.dm[a][b] + demo) * likelihood;
						
						str = "" + word2.charAt(j-1); 							
						likelihood = (double) likelihood / ur.charsX.get(str) * sub_val;
					}
				}
			}			
			i = m;
			j = n;
			
						
			if(i==0)
				if(j==0)
					if(m==0)
						if(n==0)
							break;
		}	
		return likelihood;
	}
}