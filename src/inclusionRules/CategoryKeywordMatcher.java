package inclusionRules;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;


public class CategoryKeywordMatcher extends InclusionRule {
	
	Hashtable<String,String> keywordCategories;
	
	// yes, ugly. Just stores a lookup of keywords into keywordCategories
	public CategoryKeywordMatcher(String file)
	{
		File keywords = new File(file);
		Scanner scan = null;
		keywordCategories = new Hashtable<String,String>();
		
		try {
			scan = new Scanner(keywords);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String currentCategory = "unset";
		while(scan.hasNextLine())
		{
			
			String line = scan.nextLine();
			if(line.equalsIgnoreCase("research areas") || 
					line.equalsIgnoreCase("research organisms") ||
					line.equalsIgnoreCase("research methods") )
			{
				currentCategory = line.toLowerCase();
				continue;
			}
			
			String tokens[] = line.split(" - "); 
			if(tokens.length == 1)
			{
				String slashSeparated[] = tokens[0].split("/");
				for(String s : slashSeparated){
					//put an entry as a key to the LHS category:broad category (like "research methods")
					keywordCategories.put(s, tokens[0]+";"+currentCategory);
				}
				continue;
			}
			else if(tokens.length == 2)
			{
				String slashSeparated[] = tokens[0].split("/");
				for(String s : slashSeparated){
					keywordCategories.put(s, tokens[0]);
				}
				continue;
			}
			else
			{
				System.out.println("[CategoryKeywordMatcher] errr: " + tokens);
				System.exit(0);
			}
		}
	}
	
	@Override
	public boolean isEnt(String t) {
		
		
		return false;
	}

	@Override
	public double giveProbability() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public Hashtable<String,String> getKeywordMap()
	{
		return keywordCategories;
	}

	
	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	public String getType()
	{
		return type;
	}
	

}
