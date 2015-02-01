package base;

import inclusionRules.CategoryKeywordMatcher;
import inclusionRules.InclusionRule;
import inclusionRules.RegexRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

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
	private void addRules(String fileName) 
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
						ad.getEntList().add( new Entity(t[i], sc, i) );
					}
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
	
	
	
	
}
