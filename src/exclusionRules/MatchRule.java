package exclusionRules;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;


public class MatchRule extends ExclusionRule {

	HashSet<String> killList;
	
	public MatchRule(String killListName)
	{
		killList  = new HashSet<String>();
		File killListFile = new File(killListName);
		try {
			Scanner scan = new Scanner(killListFile);
			while(scan.hasNextLine())
			{
				killList.add(scan.nextLine()); 
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public boolean shouldExclude(String t) 
	{
		return killList.contains(t);
	}

	@Override
	public double giveProbability() 
	{
		// TODO hmmm
		return 1;
	}


	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
