package base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTableau 
{
	private Hashtable<Integer, SectionContainer> sections;
	private ArrayList<String> text; // a full list of tokens
	private ArrayList<Integer> sentencesIndex; //index of the token on which each sentence ends
	private ArrayList<Integer> sectionsIndex; // index of the sentence on which each section ends
	//private ArrayList<Integer> informalSectionsIndex; // index of the section where informal sections end
	
	private Hashtable<Integer, Integer> sentenceContaining; // key=token#, val=sentence# containing token
	private Hashtable<Integer, Integer> sectionContaining; // key=sentence#, val=section# containing token
	
	// keep the names. The sectionsIndex can find its names from the sectionContainers (they are indexed the same)
	private ArrayList<String> informalSectionNames; 
	
	private ArrayList<NestedBundle> nestedBundles;
	private Hashtable<Integer, ArrayList<Integer> >nbIndex; //key=token val=list of NBs of which it is a part, in order
	
	TextTableau(Hashtable<Integer, SectionContainer> _sections, ArrayList<String> _informalSectionNames)
	{
		text = new ArrayList<String>();
		sentencesIndex = new ArrayList<Integer>();
		sectionsIndex = new ArrayList<Integer>();
		//informalSectionsIndex = new ArrayList<Integer>();
		informalSectionNames = _informalSectionNames;
		sentenceContaining = new Hashtable<Integer, Integer>();
		sectionContaining = new Hashtable<Integer, Integer>();
		
		nestedBundles = new ArrayList<NestedBundle>();
		nbIndex = new Hashtable<Integer, ArrayList<Integer> >();
		sections = _sections;
		makeTextTableau(sections);
	}
	
	private void makeTextTableau(Hashtable<Integer, SectionContainer> sections)
	{
		int token = 0;
		int sentence = 0;
		int section = 0;
		int informalSection = 0;
		String currentInformalSection = "";
		
		Stack<Integer> parsedNbIndices = new Stack<Integer>();
		String nestedOpener = "(\\[~~)(.+?)(\\|)";
		String nestedCloser = "(\\|)(.+?)(~~\\].*?)"; //.+? at the end for commas, etc.
		boolean pasteParenth = false;
				
		for(int i=0; i<sections.size(); i++)
		{
			SectionContainer sc = sections.get(i);
			String t[] = sc.getText().split(" ");
			for(int j=0; j<t.length; j++)
			{
				String hereForDebugging = t[j];
				if( t[j].equals("") )
				{
					continue;
				}
				if(Pattern.matches(nestedOpener, t[j]) )
				{
					Pattern openPattern = Pattern.compile(nestedOpener);
					Matcher m = openPattern.matcher(t[j] );
					m.find();
					String name = m.group(2);
					nestedBundles.add( new NestedBundle( token, name, nestedBundles.size()-1 ));
					parsedNbIndices.push( nestedBundles.size()-1 );
					continue;
				}
				if(Pattern.matches(nestedCloser, t[j]) )
				{
					int last = text.size()-1;
					NestedBundle nb = nestedBundles.get( parsedNbIndices.pop() );
					nb.setEnd( token-1 );

					//gets any hanging characters on the end of the tag
					String hangingText = t[j].replaceAll("\\|.+?~~\\]","").trim();
					
					//if the hanging character indicates that it's the end of a sentence, go through 
					//special steps to move onto next sentence
					if(hangingText.endsWith(".") || j == (t.length-1))
					{
						sentencesIndex.add(token-1);
						sentenceContaining.put(token-1, sentence);
						sectionContaining.put(sentence, section);
						sentence++;
					}
					// if there is a hanging character on the closing brace, this glues it to previous token
					text.set(last, ( text.get(last) + (hangingText)) ) ;
					continue;
				}
				// a hack to fix my accidentally adding a space before opening tags (nestedOpeners)
				if(t[j].equals("("))
				{
					pasteParenth = true;
					continue;
				}
				
				
				sentenceContaining.put(token, sentence);
				if( !parsedNbIndices.isEmpty() )
				{
					ArrayList<Integer> containingNbs = new ArrayList<Integer>();
					for(int a=0; a<parsedNbIndices.size(); a++)
					{
						containingNbs.add( parsedNbIndices.get(a) );
					}
					nbIndex.put(token, containingNbs);
				}
				
				
				// if at the end of the sentence or SectionContainer
				if(t[j].endsWith(".") || j == (t.length-1) )
				{
					text.add(t[j]);
					sentencesIndex.add(token);
					sectionContaining.put(sentence, section);
					token++;
					sentence++;
					//if(wasEnd) wasEnd = false;
					continue;
				}
				if(pasteParenth)
				{
					text.add("("+t[j]); pasteParenth=false;
				}
				else text.add(t[j]);
				token++;
			}
			// instead of nesting this in the previous for loop, just decrement to index to
			// the "last" sentence, not current
			sectionsIndex.add(sentence-1);
			
//			if( !getInformal(sc,currentInformalSection).equals(currentInformalSection) )
//			{
//				informalSectionNames.add(currentInformalSection);
//				//informalSectionsIndex.add(section);
//				
//				currentInformalSection = getInformal(sc,currentInformalSection);
//			}
			section++;
		}
		
	}
	
	private String markUpType(String token)
	{
		
		return null;
	}
	
		
	/** Returns a string comprised of the tokens bounded by the arguments.
	 * 
	 * @param start Token of the full text to begin the returned string (inclusive).
	 * @param end Token of the full text to end the returned string (inclusive).
	 */
	public String getTokens(int start, int end)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=start; i<=end; i++)
		{
			sb.append( text.get(i) );
			sb.append(" ");
		}
		return sb.toString().trim();
	}
	
	/** Returns a string comprised of the sentences bounded by the arguments. Fragile. Doesn't check if arguments
	 *  are valid
	 * 
	 * @param start Sentence in the full text to begin the returned string (inclusive).
	 * @param end Sentence of the full text to end the returned string (inclusive).
	 */
	public String getSentences(int start, int end)
	{
		int tokenStart=0;
		int tokenEnd=0;
		if(start>0)
		{
			// go to previous sentences end, and then add one to that
			tokenStart = sentencesIndex.get(start-1) + 1;
		}
		tokenEnd = sentencesIndex.get(end);
		
		return getTokens(tokenStart,tokenEnd);
	}
	
	public int getSentenceEnd(int tokenNumber)
	{
		
	}
	
	public int getContainingSentence(int tokenNumber)
	{
		Integer ret = sentenceContaining.get(tokenNumber);
		if(ret==null) return -1;
		return ret;
	}
	public int getContainingSection(int sentenceNumber)
	{
		Integer ret = sectionContaining.get(sentenceNumber);
		if(ret==null) return -1;
		return ret;
	}
	
	public ArrayList<NodeBundle> getSectionName(int sectionNumber)
	{
		return sections.get(sectionNumber).getPath();
	}
	
	public String getFormatting(int index)
	{
		ArrayList<Integer> nestedBundle = nbIndex.get(index);
		if(nestedBundle != null)
		{
			return nestedBundles.get( nestedBundle.get(0) ).toString();
		}
		return null;
	}
	
	public ArrayList<NestedBundle> getBundles(int index)
	{
		ArrayList<Integer> nestedBundle = nbIndex.get(index);
		if(nestedBundle==null)
		{
			return null;
		}
		ArrayList<NestedBundle> ret = new ArrayList<NestedBundle>();
		for(Integer i : nestedBundle )
		{
			ret.add( nestedBundles.get(i) );
		}
		return ret;
	}
	
	public ArrayList<String> allTokens()
	{
		return text;
	}
}
