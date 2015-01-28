package base;
import inclusionRules.InclusionRule;
import inclusionRules.RegexRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import com.rits.cloning.Cloner;

import exclusionRules.ExclusionRule;



public class EntManager {
	ArrayList<InclusionRule> inclusionRules;
	ArrayList<ExclusionRule> exclusionRules;
	EntList entList;



	public EntManager()
	{
		inclusionRules = new ArrayList<InclusionRule>();
		entList = new EntList("default:EntManager");
	}
	public EntManager(Collection rules)
	{
		inclusionRules = new ArrayList<InclusionRule>();
		entList = new EntList("default:EntManager");
		
		if(rules.size() == 0) return;	
		Object r[] = rules.toArray();
		
		//ASSUMING we get a list of the same type of object, in this case, string
		if( r[0] instanceof String )
		{
			for(Object s : r)
			{
				addRule( (String)s );
			}
		}
		
		// in this case, InclusionRule
		if( r[0] instanceof InclusionRule )
		{
			for(Object ir : r)
			{
				addRule( (InclusionRule)ir );
			}
		}
	}

	
	public void addRule(String regex)
	{
		inclusionRules.add( new RegexRule( regex ) );
	}
	public void addRule(InclusionRule ir)
	{
		// https://code.google.com/p/cloning/wiki/Downloads?tm=2
		Cloner cloner = new Cloner();
		inclusionRules.add( cloner.deepClone(ir) );
	}
	
 
	public void loadup(Hashtable<Integer, SectionContainer> sections)
	{
		iterateWordsMethod(sections);
	}
	
	
	protected void iterateWordsMethod(Hashtable<Integer,SectionContainer> sections)
	{
		//for a given inclusionRule
		for(InclusionRule ir : inclusionRules)
		{
			//iterate through all sections
			for(int i = 0; i < sections.size(); i++)
			{
				
				SectionContainer sc = sections.get(i);
				String tokens[] = sc.getText().split(" ");

				//and though all words in each section
				for(int j = 0; j < tokens.length; j++)
				{
					String token = tokens[j].trim(); // trim to remove extra spaces from DFS text removal
					if( ir.isEnt(token) )
					{
						addEnt(token, sc, j );
					}
					
				}
				
				
				
				
			}
		}
	}
	
	protected void functorMethod(Hashtable<String,SectionContainer> sections)
	{
		//for a given inclusionRule
		for(InclusionRule ir : inclusionRules)
		{
			//iterate through all sections
			for(int i = 0; i < sections.size(); i++)
			{
				
				SectionContainer sc = sections.get(i);
				String tokens[] = sc.getText().split(" ");

				//and though all words in each section
				for(int j = 0; j < tokens.length; j++)
				{
					String token = tokens[j].trim(); // trim to remove extra spaces from DFS text removal
					if( ir.isEnt(token) )
					{
						addEnt(token, sc, j );
					}
					
				}

				
			}
		}
	}
	
	private void addEnt(String token, SectionContainer sc, int position)
	{
		entList.addEnt( new Entity(token, sc, position) ); // didn't see the point in adding another level of indirection with just "addEnt()"
	}
	
	
	public void addEnt(Entity ent)
	{
		entList.addEnt(ent);
	}
	
	public EntList getEntList() {
		return entList;
	}
}
