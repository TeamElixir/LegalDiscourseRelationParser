package shiftinview.verbsSemanticSimilarity.models;

public class SentencePair {
    public static final String TABLE_NAME = "FEATURE_ENTRY_LEGAL_SENTENCE";
    private int id;
    private Sentence sourceSentence;
    private Sentence targetSentence;
    private String relation;


    public SentencePair() {
    }

    public SentencePair(int id, Sentence sourceSentence, Sentence targetSentence, String relation) {
        this.id = id;
        this.sourceSentence = sourceSentence;
        this.targetSentence = targetSentence;
        this.relation = relation;
    }

    public int getId() {
        return id;
    }

    public Sentence getSourceSentence() {
        return sourceSentence;
    }

    public Sentence getTargetSentence() {
        return targetSentence;
    }

    public String getRelation() {
        return relation;
    }

    @Override
    public String toString() {
        return "SentencePair{" +
                "id=" + id +
                ", sourceSentence=" + sourceSentence +
                ", targetSentence=" + targetSentence +
                ", relation='" + relation + '\'' +
                '}';
    }
}
