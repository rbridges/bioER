package exclusionRules;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;


public class MatchRule extends ExclusionRule {

	Hashtable<String, Integer> killList;
	
	public MatchRule(String killListName)
	{
		killList  = new Hashtable<String, Integer>();
		File killListFile = new File(killListName);
		try {
			Scanner scan = new Scanner(killListFile);
			while(scan.hasNextLine())
			{
				killList.put(scan.nextLine(), 42); // facetious entry of 42. Just care about containsKey()
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public boolean shouldExclude(String t) 
	{
		return killList.containsKey(t);
	}

	@Override
	public double giveProbability() 
	{
		// TODO hmmm
		return 1;
	}

}
