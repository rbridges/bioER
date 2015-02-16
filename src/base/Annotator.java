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
					if( ((InclusionRule) r).isEnt(t[i]))
					{
						ad.addEntity( new Entity(t[i], sc, i, i) ,"regex");
					}
					
					//HARDCODED ITALICS GRABBER
					ArrayList<String> a = sc.getPath();
					if(a.get( a.size()-1 ).equals("it") )
					{
						ad.addEntity( new Entity(t[i], sc, i, i) ,"italics");
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
	
	// going to try and just write this out, design an interface later
	public void annotate(AnnotatableDocument doc)
	{
		addRules("rules/regexPatterns.txt");
		addRemovalRules("rules/killList.txt");
		
		ArrayList<Rule> regexMatcher = rules.get("rules/regexPatterns.txt");
		MatchRule removeSet = (MatchRule)removalRules.get("rules/killList.txt");
		
		Hashtable<Integer,SectionContainer> sections = doc.getSections();
		
		for(SectionContainer sc : sections.values())
		{
			if(sc.getRelevance()==0) continue;
			
			///////// italics //and bold
			ArrayList<String> path = sc.getPath();
			String last = path.get(path.size()-1);
			if( last.equals("italics") || last.equals("it") )
			{
				// the entity is the full range of stuff inside the italics
				Entity newEnt = new Entity(sc.getText(), sc, 0, (sc.getText().length()-1) );
				newEnt.addFoundBy("italics");
				doc.addEntity(newEnt, "italics");
				// can simply verify if entity is a regex match, don't need to do it at parse time
				// TODO: also no parenthesis inside italics (but <it>entity</it> <it>(alias)</it> happens)
				continue;
			}
//			if( last.equals("bold") || last.equals("b") )
//			{
//				// the entity is the full range of stuff inside the bold
//				Entity newEnt = new Entity(sc.getText(), sc, 0, (sc.getText().length()-1) );
//				newEnt.addFoundBy("bold");
//				doc.addEntity(newEnt, "bold");
//			}
			/////////// end italics and bold
			
			
			String t[] = sc.getText().split(" ");
			for(int i = 0; i < t.length; i++ )
			{
				if( removeSet.shouldExclude(t[i])) continue;
				Entity newEnt = null;
				
				boolean isRegexMatch = false;
				
				// regex match
				for(Rule r : regexMatcher)
				{
					if( ((RegexRule)r).isEnt(t[i]))
						{
							isRegexMatch = true;
							if(newEnt==null) newEnt = new Entity(t[i], sc, i, i);
							newEnt.addFoundBy("regex");
							break;
						}
				}
				if(i == (t.length-1) ) break;
				String inParenths = "\\([^) ]+?\\)"; // no spaces, one token in ()
				if(isRegexMatch && Pattern.matches(inParenths,t[i+1]) )
				{
					String noParenths = t[i+1].replace("(", "").replace(")","");
					doc.getEntManager().aliasEnts(t[i], noParenths);
				}
				
				
				if(newEnt!=null) doc.addEntity(newEnt, "regex");
				
					
				
			}
			
		}
		
		
		
		
		
		
	}
	
	
	
	
}
