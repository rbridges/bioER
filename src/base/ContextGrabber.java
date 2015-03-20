package base;

import java.util.ArrayList;

public class ContextGrabber 
{
	/** Returns an ArrayList of the given token (whichToken) sandwiched by the number of surrounding entities requested.
	 *  If either parameter is greater than the number of tokens available in the sentence,
	 *  method returns the tokens up to the beginning or end of sentence.
	 * @param text The text from which to grab surrounding tokens
	 * @param whichToken The index into the text of the token you'd like to extract neighbors of
	 * @param numberBefore Number of tokens to return before the given token
	 * @param numberAfter Number of  tokens to return after the given token
	 * @return
	 */
	ArrayList<String> getTokens(String text, int whichToken, int numberBefore, int numberAfter)
	{
		ArrayList<String> returnableTokens = new ArrayList<String>();
		String t[] = text.split(" ");
		int actualNumberBefore; // to bound within sentence and sectioncontainer
		for(actualNumberBefore=0; actualNumberBefore<numberBefore; actualNumberBefore++)
		{
			// if we've come to the beginning of the SC's text, bail out
			if(whichToken-actualNumberBefore == 0) break;
			
			// if we've come to the first token of the sentence (the previous has a period), bail out
			if( t[whichToken-(actualNumberBefore+1) ].endsWith(".") ) break;
		}
		for(int i=actualNumberBefore; i>=0; i--)
		{
			// put the preceding tokens in place, including the given token
			returnableTokens.add( t[whichToken-actualNumberBefore] );
		}
		
		for(int i=0; i<=numberAfter; i++)
		{
			// if we're at the end of sentence or SC's text, bail out
			if( (whichToken+i+1) == t.length  ||  t[whichToken+i].endsWith(".") )
			{
				returnableTokens.add(t[whichToken+i]);
				break;
			}
			returnableTokens.add(t[whichToken+i]);
		}
		return returnableTokens;
	}
	
	ArrayList<String> getSentences(String text, int whichToken, int numberBefore, int numberAfter)
	{
		String t[] = text.split(" ");
		ArrayList<String> returnableSentences = new ArrayList<String>();
		
		int tokensBefore=0;
		boolean breakOut = false;
		int containingSentenceBeginning;
		
		//get out of the current sentence, record where containing sentence begins
		while( true )
		{
			// if we've come to the beginning of the SC's text, bail out
			if(whichToken-tokensBefore == 0)
			{
				breakOut = true;
				break;
			}
			
			// if we've come to the first token of the sentence (the previous has a period), bail out
			if( t[whichToken-(tokensBefore+1) ].endsWith(".") )
			{
				containingSentenceBeginning = tokensBefore;
				tokensBefore++;
				break;			
			}
			tokensBefore++;
		}
		
		// count numberBefore many sentences worth of tokens back
		int sentencesBefore;
		for(sentencesBefore=0; sentencesBefore<=numberBefore; sentencesBefore++)
		{
			while( true )
			{
				// if we've come to the beginning of the SC's text, bail out
				if(whichToken-tokensBefore == 0)
				{
					breakOut = true;
					break;
				}
				
				// if we've come to the first token of the sentence (the previous has a period), bail out
				if( t[whichToken-(tokensBefore+1) ].endsWith(".") )
				{
					tokensBefore++;
					break;			
				}
				tokensBefore++;
			}
		}
		
		// push in the sentences we counted
		for(int i=0; i<sentencesBefore; i++)
		{
			StringBuilder sb = new StringBuilder();
			while( true )
			{
				if( t[whichToken-tokensBefore].endsWith(".") )
				{
					sb.append(t[whichToken-tokensBefore]);
					returnableSentences.add(sb.toString());
					tokensBefore--;
					break;
				}
				sb.append(t[whichToken-tokensBefore]);
				tokensBefore--;
			}
		}
		
		return null;
	}
	
	
}
