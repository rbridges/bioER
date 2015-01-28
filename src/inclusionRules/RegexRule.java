package inclusionRules;

public class RegexRule extends InclusionRule {
	
	String regex;
	
	public RegexRule(String _regex)
	{
		regex = _regex;
	}
	
	@Override
	public boolean isEnt(String t) {
		return t.matches(regex);
	}

	@Override
	public double giveProbability() {
		// TODO Future
		return 1;
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}

}
