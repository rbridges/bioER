package inclusionRules;

import base.Rule;

public abstract class InclusionRule extends Rule {
	
	public abstract boolean isEnt(String t);
	
	//eventually get rid of isEnt in favor of "process"?
	public abstract void process();

}
