package Main;

import java.util.HashMap;


public class Alphabet {
	public HashMap<Character, Integer> alphabet = new HashMap<Character, Integer>();
		
	public Alphabet()	{
		generateAlphabet();
	}
	
    public void generateAlphabet()	{
    	alphabet.put('a', 0);
    	alphabet.put('b', 1);
    	alphabet.put('c', 2);
    	alphabet.put('d', 3);
    	alphabet.put('e', 4);
    	alphabet.put('f', 5);
    	alphabet.put('g', 6);
    	alphabet.put('h', 7);
    	alphabet.put('i', 8);
    	alphabet.put('j', 9);
    	alphabet.put('k', 10);
    	alphabet.put('l', 11);
    	alphabet.put('m', 12);
    	alphabet.put('n', 13);
    	alphabet.put('o', 14);
    	alphabet.put('p', 15);
    	alphabet.put('q', 16);
    	alphabet.put('r', 17);
    	alphabet.put('s', 18);
    	alphabet.put('t', 19);
    	alphabet.put('u', 20);
    	alphabet.put('v', 21);
    	alphabet.put('w', 22);
    	alphabet.put('x', 23);
    	alphabet.put('y', 24);
    	alphabet.put('z', 25);    	   	
    	alphabet.put('@', 26);    	   	
    }


}
