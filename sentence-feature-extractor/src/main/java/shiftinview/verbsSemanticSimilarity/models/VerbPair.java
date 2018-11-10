package shiftinview.verbsSemanticSimilarity.models;

public class VerbPair {
    public static final String TABLE_NAME = "VERB_PAIR";
    private int id;
    private int sentencePairID;
    private String sourceVerb;
    private String targetVerb;

    public VerbPair(int id, int sentencePairID, String sourceVerb, String targetVerb) {
        this.id = id;
        this.sentencePairID = sentencePairID;
        this.sourceVerb = sourceVerb;
        this.targetVerb = targetVerb;
    }

    public VerbPair(int sentencePairID, String sourceVerb, String targetVerb) {
        this.sentencePairID = sentencePairID;
        this.sourceVerb = sourceVerb;
        this.targetVerb = targetVerb;
    }

    public int getId() {
        return id;
    }

    public int getSentencePairID() {
        return sentencePairID;
    }

    public String getSourceVerb() {
        return sourceVerb;
    }

    public String getTargetVerb() {
        return targetVerb;
    }

    public VerbPair swapSourceTarget() {
        if (this.sourceVerb.charAt(0) > this.targetVerb.charAt(0)) {
            String temp = this.sourceVerb;
            this.sourceVerb = this.targetVerb;
            this.targetVerb = temp;
        }
        return this;
    }

    @Override
    public String toString() {
        return "VerbPair{" +
                "id=" + id +
                ", sentencePairID=" + sentencePairID +
                ", sourceVerb='" + sourceVerb + '\'' +
                ", targetVerb='" + targetVerb + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // or reversed
        return (this.sourceVerb.equalsIgnoreCase(((VerbPair) obj).sourceVerb) &&
                this.targetVerb.equalsIgnoreCase(((VerbPair) obj).targetVerb)) ||
                // or reversed
                (this.sourceVerb.equalsIgnoreCase(((VerbPair) obj).targetVerb) &&
                        this.targetVerb.equalsIgnoreCase(((VerbPair) obj).sourceVerb));
    }
}
