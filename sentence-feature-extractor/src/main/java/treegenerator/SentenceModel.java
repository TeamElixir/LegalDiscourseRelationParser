package treegenerator;

import edu.stanford.nlp.ie.util.RelationTriple;

import java.util.Collection;

public class SentenceModel {

    public String sentence;
    public Collection<RelationTriple> triples;
    public  boolean citation;
    public boolean elaboration;
    public  boolean noRelation;
    public boolean shiftInView;
    public boolean argument;

}
