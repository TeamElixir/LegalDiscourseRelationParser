package shiftinview.wuPalmerTest.models;

public class AnnotatedVerbPair extends VerbPair {
    public static final String TABLE_NAME = "ANNOTATED_VERB_PAIR";
    private int annotation;

    public AnnotatedVerbPair(int id, int sentencePairID, String sourceVerb, String targetVerb, int annotation) {
        super(id, sentencePairID, sourceVerb, targetVerb);
        this.annotation = annotation;
    }

    public int getAnnotation() {
        return annotation;
    }
}
