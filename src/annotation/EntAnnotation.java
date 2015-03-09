package annotation;

/** Used to associate annotated entities with author/editor feedback
 * */
public class EntAnnotation {
	Status status;
	String note;
	
	// TODO: structured reasons for correctness, like globally recognized T/F, instance, special case
	// structured learning suggestions?
	
	public enum ActuallyWas {
		GENE,PROTEIN,INTERACTION
	}
	/** Phrase/Token is True positive, false positive, false negative*/
	public enum Status{
		TP,FP,FN
	}
	
	public EntAnnotation()
	{
		status = null;
		note = null;
	}

	
	public void mark(Status response)
	{
		status = response;
	}
	public void makeNote(String _note)
	{
		note = _note;
	}
}
