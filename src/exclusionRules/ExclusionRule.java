package exclusionRules;

import base.Rule;

public abstract class ExclusionRule extends Rule{
	
	public abstract boolean shouldExclude(String t);
	
	
	
}
