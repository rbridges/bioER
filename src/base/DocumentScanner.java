package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DocumentScanner {
	
	public void parse(String fileName)
	{
		StringBuilder words = new StringBuilder();
		ArrayList<String> tokens = new ArrayList<String>();
		Scanner scanner = null;
		try {
			 scanner = new Scanner(new File(fileName));
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get the text out of the document, putting spaces between tags
		while(scanner.hasNext())
		{
			String currentToken = scanner.next();
			
			words.append( currentToken.replaceAll("<"," <").replaceAll(">","> ") );
			words.append(" ");
	
		}
	
		// tokenize this text into an array
		for(String s : words.toString().split(" "))
		{
			// don't include nonsense or artifical spaces
			if(s.equals("") || s.equals(" ") )
			{
				continue;
			}
			tokens.add(s);
		}
		
		
	}
	
	private void slice(ArrayList<String> tokens)
	{
		
		
		
		
	}
	
}
