package utils.models;

import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceWords;

public class NoRelation {
    public boolean Transition = false;
    public boolean Pronoun = false;
    public int sourceLength = 0;
    public int targetLength = 0;
    public double SemanticSimilarityPrev=0.0;
    public double SemanticSimilarityNew=0.0;
    public double overlapRatio = 0.0;

    public void setLengths(String sourceSentence, String targetSentence){
        SentenceProps wordProps = new SentenceWords(sourceSentence, targetSentence);
        sourceLength=wordProps.getSeq_sentence1().size();
        targetLength=wordProps.getSeq_sentence2().size();
    }
}
