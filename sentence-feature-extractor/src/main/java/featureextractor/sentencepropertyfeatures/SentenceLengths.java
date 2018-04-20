package featureextractor.sentencepropertyfeatures;

import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceWords;

public class SentenceLengths {

    private SentenceProps sentenceWords;

    public SentenceLengths(String sourceSentence, String targetSentence){
        sentenceWords = new SentenceWords(sourceSentence,targetSentence);
    }

    public double getLengthScore(){
        double sourceSentenceLength = (double) sentenceWords.getSeq_sentence1().size();
        double targetSentenceLength = (double) sentenceWords.getSeq_sentence2().size();
        double score;
        double difference = sourceSentenceLength - targetSentenceLength;
        double differenceValue = (difference/(sourceSentenceLength*2));

        score = 0.50 + differenceValue;

        return score;
    }

}
