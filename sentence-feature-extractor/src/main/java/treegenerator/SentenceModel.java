package treegenerator;

import edu.stanford.nlp.ie.util.RelationTriple;

import java.util.ArrayList;
import java.util.Collection;

public class SentenceModel {

    public int ID;
    public String sentence;
    public String processedSentence;
    public Collection<RelationTriple> triples=new ArrayList<>();
    public ArrayList<String> subjects = new ArrayList<>();
    public ArrayList<String> legalSubjects = new ArrayList<>();
    public  boolean citation;
    public boolean elaboration;
    public  boolean noRelation;
    public boolean shiftInView;
    public boolean argument;
    public  boolean nodeStart=false;
    public int parentID;


}
