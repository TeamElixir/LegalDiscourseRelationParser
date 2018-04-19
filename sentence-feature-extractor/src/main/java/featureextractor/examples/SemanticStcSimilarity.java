package featureextractor.examples;

import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import utils.NLPUtils;

public class SemanticStcSimilarity {
    static NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");
    static String sentence1 = "Gathika and John play cricket";
    static String sentence2 = "Sharukh is dancing with Kajol";

    public static void main(String[] args) {


        Annotation annotation1 =nlpUtils.annotate(sentence1);
        Annotation annotation2 = nlpUtils.annotate(sentence2);

        SemanticSentenceSimilarity semanticSentenceSimilarity = new SemanticSentenceSimilarity(annotation1,
                annotation2,nlpUtils);
        semanticSentenceSimilarity.checkSimilarity();
        double finalScore = semanticSentenceSimilarity.getAverageScore();
        System.out.println("finalScore : "+finalScore);
    }
}
