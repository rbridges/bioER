package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class OboParser 
{
	// what you want the key for the Hashtable that parse returns to be
	/*ie.
	 * [Term]
		id: NCBI_Gene:1466252
		name: rrn23S
		is_a: SO:0000252
		relationship: encoded_by NCBITaxon:3702
		relationship: part_of Chromosome:3702--
	 */
	// so that rrn23S will be the key, if "name" is selected as the LOOKUP_ATTRIBUTE
	private final String LOOKUP_ATTRIBUTE = "name"; 
	
	public Hashtable< String, Hashtable<String, String> > parse( String oboFileName )
	{
		Scanner scan = null;
		try {
			scan = new Scanner (new File(oboFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean newTerm = false;
		Hashtable<String, Hashtable<String,String> > terms = new Hashtable<String, Hashtable<String,String> >();
		Hashtable<String,String> currentTerm = null;
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String name = null;
			if(newTerm)
			{
				terms.put(name, currentTerm);
				currentTerm = new Hashtable<String,String>();
				newTerm = false;
			}
			
			if( line.equals("[TERM]") )
			{
				newTerm = true;
				continue;
			}
			
			line.replaceFirst(":", "1209&"); //1209& is just a more unique placeholder to split on
			String split[] = line.split("1209&"); // since there is no "splitFirst(":")"
			if(split[0].equals(LOOKUP_ATTRIBUTE))
			{
				name = split[1];
			}
			currentTerm.put(split[0], split[1]);
		}
		scan.close();
		return terms;
	}
}
