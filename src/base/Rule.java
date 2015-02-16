package base;

public abstract class Rule {
	public String type;
	public abstract double giveProbability();
	public abstract String getType();
}
