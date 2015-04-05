package base;

import inclusionRules.CategoryKeywordMatcher;
import inclusionRules.InclusionRule;
import inclusionRules.RegexRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import exclusionRules.MatchRule;

public class Annotator {

	Hashtable< String,ArrayList<Rule> > rules; 
	Hashtable< String, Rule > removalRules;
	Hashtable< String, Rule > metaRules;
	
	public Annotator()
	{
		rules = new Hashtable< String, ArrayList<Rule> >();
		removalRules = new Hashtable< String, Rule >();
		metaRules = new Hashtable< String,Rule >();
	}

	//USED IN OLD DEMOS
	public void annotate(AnnotatableDocument aDoc, String fileName)
	{
		if(!rules.containsKey(fileName) )
		{
			addRules(fileName);
		}
		rollThrough_booleanRule(aDoc, fileName);
	}
	
	public void metaData(AnnotatableDocument aDoc, String fileName)
	{
		if(!metaRules.containsKey(fileName))
		{
			metaRules.put(fileName, new CategoryKeywordMatcher(fileName));
		}
		
		rollThrough_metaRule(aDoc, fileName);
	}
	
	private void rollThrough_metaRule(AnnotatableDocument aDoc, String ruleKey) 
	{
		Hashtable<Integer, SectionContainer> sections = aDoc.getSections();
		
		CategoryKeywordMatcher fetchedRule = (CategoryKeywordMatcher)metaRules.get(ruleKey);

			for(SectionContainer sc : sections.values())
			{
				String t[] = sc.getText().split(" ");
				for(int i = 0; i < t.length; i++ )
				{
					metaData(aDoc, fetchedRule, t[i]);
				}	
			}
		
		
	}

	//TODO This is unnecessary
	private void metaData(AnnotatableDocument aDoc, CategoryKeywordMatcher ckm, String token)
	{

			String category = ckm.getKeywordMap().get(token);
			if(category == null)
			{
				return;
			}
			aDoc.addMetaData(category);
		
	
	}

	// currently only adds regex rules
	public void addRules(String fileName) 
	{
		File f = new File(fileName);
		Scanner scan = null;
		
		//////////
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////
		
		ArrayList<Rule> fileRules = new ArrayList<Rule>();
		
		// could add a markup in the file to get a different add methodology
		while(scan.hasNext())
		{
			String regex = scan.next();
			fileRules.add( new RegexRule(regex) );
		}
		
		rules.put(fileName, fileRules);
		
	}
	
	// currently only adds regex rules
	private void addRemovalRules(String fileName) 
	{
		File f = new File(fileName);
		Scanner scan = null;
		
		//////////
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////
		
		
		removalRules.put(fileName, new MatchRule(fileName) );
		
	}
	
	//NOT USED IN CURRENT CODE
	private void rollThrough_booleanRule(AnnotatableDocument ad, String ruleKey)
	{
		Hashtable<Integer, SectionContainer> sections = ad.getSections();
		
		ArrayList<Rule> fetchedRules = rules.get(ruleKey);
		for(Rule r : fetchedRules)
		{
			for(SectionContainer sc : sections.values())
			{
				String t[] = sc.getText().split(" ");
				for(int i = 0; i < t.length; i++ )
				{
					String cleaned = t[i].replace(",", "").replace(".", "");
					if( ((InclusionRule) r).isEnt(cleaned))
					{
						ad.addEntity( new Entity(cleaned, sc, i, i) ,"regex");
					}
					
					//HARDCODED ITALICS GRABBER
					ArrayList<NodeBundle> a = sc.getPath();
					if(a.get( a.size()-1 ).getName().equals("it") )
					{
						ad.addEntity( new Entity(cleaned, sc, i, i) ,"italics");
					}
					////
					
				}	
			}
		}
		
	}
	
	
	public void remove(AnnotatableDocument aDoc, String fileName)
	{
		if(!rules.containsKey(fileName) )
		{
			addRemovalRules(fileName);
		}
		
		remover(aDoc,fileName);
	}
	
	private void remover(AnnotatableDocument ad, String ruleKey)
	{
		Hashtable<Integer, SectionContainer> sections = ad.getSections();
		
		Rule fetchedRule = removalRules.get(ruleKey);

			for(SectionContainer sc : sections.values())
			{
				String t[] = sc.getText().split(" ");
				for(int i = 0; i < t.length; i++ )
				{
					if( ((MatchRule)fetchedRule).shouldExclude(t[i]))
					{
						ad.removeEntity(t[i]);
					}
				}	
			}
		
		
	}
	
	
	
	
	////////// procedural annotation
	
	@Deprecated
	public void annotate(AnnotatableDocument doc)
	{
//		
//		addRules("rules/regexPatterns.txt");
//		addRemovalRules("rules/killList.txt");
//		
//		ArrayList<Rule> regexMatcher = rules.get("rules/regexPatterns.txt");
//		MatchRule removeSet = (MatchRule)removalRules.get("rules/killList.txt");
//		
//		// lazy me adding a scope as to not redeclare variables of same name below
//	{	
//		
//		//// New section to add () aliasing
//	
//		String text = doc.getFullText();
//		String t[] = doc.getFullText()./*replace("  ", " ").*/split(" ",-1); //replace("<italics>", "").replace("</italics>", "").
//		boolean isRegexMatch = false;
//		String inParenths = "\\([^) ]+?\\)"; // no spaces, one token in ()
//		
//		for(int i = 0; i < t.length; i++  )
//		{
//			if(i == (t.length-1) ) break;
//
//			String cleaned = t[i].replace(",", "").replace(".", "");
//			for(Rule r : regexMatcher)
//			{
//				if( ((RegexRule)r).isEnt(cleaned))
//					{
//						isRegexMatch = true; 
//						break;
//					}
//			}
//			
//			if(i == t.length-1) break;
//			if(isRegexMatch && !t[i].contains(".") && Pattern.matches(inParenths,t[i+1]) )
//			{
//				String noParenths = t[i+1].replace("(", "").replace(")","");
//				doc.getEntManager().aliasEnts(cleaned, noParenths);
//			}
//			
//			if(i == t.length-3) continue;
//			//last parenth "contains" because of periods, etc.
//			if(isRegexMatch && !t[i].contains(".") && t[i+1].equals("(") && t[i+3].contains(")") )
//			{
//				String one = t[i];
//				String two = t[i+2];
//				doc.getEntManager().aliasEnts(cleaned, t[i+2].replace(",", "").replace(".", ""));
//			}
//			
//			isRegexMatch = false;
//		}
//	}
//		
//		
//	
//		
//		Hashtable<Integer,SectionContainer> sections = doc.getSections();
//		
//		for(SectionContainer sc : sections.values())
//		{
//			if(sc.getRelevance()==0) continue;
//			
//			///////// italics //and bold
//			ArrayList<NodeBundle> path = sc.getPath();
//			NodeBundle last = path.get(path.size()-1);
//			if( last.getName().equals("italics") || last.getName().equals("it") )
//			{
//				// the entity is the full range of stuff inside the italics
//				Entity newEnt = new Entity(sc.getText(), sc, 0, (sc.getText().length()-1) );
//				newEnt.addFoundBy("italics");
//				doc.addEntity(newEnt, "italics");
//				// can simply verify if entity is a regex match, don't need to do it at parse time
//				// TODO: also no parenthesis inside italics (but <it>entity</it> <it>(alias)</it> happens)
//				continue;
//			}
////			if( last.equals("bold") || last.equals("b") )
////			{
////				// the entity is the full range of stuff inside the bold
////				Entity newEnt = new Entity(sc.getText(), sc, 0, (sc.getText().length()-1) );
////				newEnt.addFoundBy("bold");
////				doc.addEntity(newEnt, "bold");
////			}
//			/////////// end italics and bold
//			
//			
//			String t[] = sc.getText().split(" ");
//			for(int i = 0; i < t.length; i++ )
//			{
//				String cleaned = t[i].replace(".", "").replace(",", "");
//				if( removeSet.shouldExclude(cleaned)) continue;
//				Entity newEnt = null;
//				
//				boolean isRegexMatch = false;
//				
//				// regex match
//				for(Rule r : regexMatcher)
//				{
//					if( ((RegexRule)r).isEnt(cleaned))
//						{
//							isRegexMatch = true;
//							if(newEnt==null) newEnt = new Entity(cleaned, sc, i, i);
//							newEnt.addFoundBy("regex");
//							break;
//						}
//				}
//		
//				// parenthesis aliasing was here
//				
//				
//				if(newEnt!=null) doc.addEntity(newEnt, "regex");
//				
//					
//				
//			}
//			
//		}
//		
//		
//		
//
//		
	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void textTableauAnnotate(AnnotatableDocument doc)
	{
		
		addRules("rules/regexPatterns.txt");
		addRemovalRules("rules/killList.txt");
		
		ArrayList<Rule> regexMatcher = rules.get("rules/regexPatterns.txt");
		MatchRule removeSet = (MatchRule)removalRules.get("rules/killList.txt");

		
		ArrayList<String> t = doc.getTextTableau().allTokens();
		boolean isRegexMatch;
		
		// co-reference ents and their abbreviations as indicated by "LONGGENENAME (LGN) ..."
		parenthsAliasing(doc, regexMatcher,t);
	
		
		Hashtable<Integer,SectionContainer> sections = doc.getSections();
		
		int i = 0;
		TextTableau tt = doc.getTextTableau();
		ArrayList<String> text = tt.allTokens();
	
	whileLoop:
		while(true)
		{
			if(i>=text.size()) break;
			
			String s = text.get(i);
			String cleaned = s.replace(".", "").replace(",", "").replace("(", "")
					.replace(")","");
			ArrayList<NestedBundle> bundles = tt.getBundles(i);
			Entity newEnt = null;
			

				for(Rule r : regexMatcher)
				{
					if( ((RegexRule)r).isEnt(cleaned) && !removeSet.shouldExclude(cleaned) )
						{
							isRegexMatch = true;
							int sectionNumber = tt.getContainingSection( tt.getContainingSentence(i) );
							if(newEnt==null) newEnt = new Entity(cleaned, sectionNumber, i, i);
							newEnt.addFoundBy("regex");
							newEnt.setIsA("gene");
							doc.addEntity(newEnt);
							
							i++;
							continue whileLoop;
						}
				}

			if(bundles == null) 
			{
				i++;
				continue whileLoop;
			}
				
			//this could be cleaned up with an "isIn("italic")" method instead
			for(NestedBundle nb : bundles)
			{
				int start = nb.getStart();
				int end = nb.getEnd();
				if(nb.toString().equals("italic"))
				{

					// one word
					if(start==end)
					{
						int sectionNumber = tt.getContainingSection( tt.getContainingSentence(i) );
						if(newEnt==null) newEnt = new Entity(cleaned, sectionNumber, i, i);
						newEnt.addFoundBy("italic");
						newEnt.setIsA("gene");
						doc.addEntity(newEnt);
						
						i++;
						continue whileLoop;
					}
					//two words, checking for species regex
					if(end-start == 1)
					{
						//starts with capital, has non captials in first word, second word is anything
						String speciesRegex = "^[A-Z][^A-Z]*? .+"; 
						String pair = tt.getTokens(start, end);
						
						// add a species entity
						if(Pattern.matches(speciesRegex, pair))
						{
							int sectionNumber = tt.getContainingSection( tt.getContainingSentence(i) );
							if(newEnt==null) newEnt = new Entity(tt.getTokens(start, end), 
									sectionNumber, start, end);
							newEnt.addFoundBy("italic");
							newEnt.addFoundBy("regex");
							newEnt.setIsA("species");
							doc.addEntity(newEnt);
							
							i = nb.getEnd()+1;
							continue whileLoop;
						}
					}
				}
			}
			i++;
			continue whileLoop;
		}
		
		
		

		
	}

	private void parenthsAliasing(AnnotatableDocument doc, 
			ArrayList<Rule> regexMatcher, ArrayList<String> t) {
		//// New section to add () aliasing
	
		boolean isRegexMatch = false;
		String inParenths = "\\([^) ]+?\\)"; // no spaces, one token in ()
		
		for(int i = 0; i < t.size(); i++  )
		{
			if(i == (t.size()-1) ) break;

			String cleaned = t.get(i).replace(",", "").replace(".", "");
			for(Rule r : regexMatcher)
			{
				if( ((RegexRule)r).isEnt(cleaned))
					{
						isRegexMatch = true; 
						break;
					}
			}
			
			if(i == t.size()-1) break;
			if(isRegexMatch && !t.get(i).contains(".") && Pattern.matches(inParenths,t.get(i+1)) )
			{
				String noParenths = t.get(i+1).replace("(", "").replace(")","");
				doc.getEntManager().aliasEnts(cleaned, noParenths);
			}
			
			if(i == t.size()-3) continue;
			//last parenth "contains" because of periods, etc.
			if(isRegexMatch && !t.get(i).contains(".") && t.get(i+1).equals("(") && t.get(i+3).contains(")") )
			{
				String one = t.get(i);
				String two = t.get(i+2);
				doc.getEntManager().aliasEnts(cleaned, t.get(i+2).replace(",", "").replace(".", ""));
			}
			
			isRegexMatch = false;
		}
	}

	
	
	
	
}
