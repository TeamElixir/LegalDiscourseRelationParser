package shiftinview.wuPalmerTest.models;

public class VerbPair {
    public static final String TABLE_NAME = "VERB_PAIR";
    private int sentencePairID;
    private String sourceVerb;
    private String targetVerb;

    public VerbPair(int sentencePairID, String sourceVerb, String targetVerb) {
        this.sentencePairID = sentencePairID;
        this.sourceVerb = sourceVerb;
        this.targetVerb = targetVerb;
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

    @Override
    public String toString() {
        return "VerbPair{" +
                "sentencePairID=" + sentencePairID +
                ", sVerb='" + sourceVerb + '\'' +
                ", tVerb='" + targetVerb + '\'' +
                '}';
    }
}
