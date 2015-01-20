
public class RegexRule extends InclusionRule {
	
	String regex;
	
	RegexRule(String _regex)
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

}
