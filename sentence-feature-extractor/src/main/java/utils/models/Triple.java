package utils.models;

public class Triple {
	public String subject;
	public String object;
	public String relation;
	public String subjectLemma;
	public String objectLemma;
	public String relationLemma;
	public String sentence;

	@Override
	public String toString(){
		return new String("{"+subject+" : "+relation+" : "+object+"}");
	}
}
