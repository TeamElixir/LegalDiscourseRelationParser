package shiftinview.wuPalmerTest;

import edu.cmu.lti.jawjaw.pobj.POS;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import shiftinview.wuPalmerTest.controllers.AnnotatedVerbPairsController;
import shiftinview.wuPalmerTest.controllers.SentencePairsController;
import shiftinview.wuPalmerTest.controllers.VerbPairsController;
import shiftinview.wuPalmerTest.models.AnnotatedVerbPair;
import shiftinview.wuPalmerTest.models.Sentence;
import shiftinview.wuPalmerTest.models.SentencePair;
import shiftinview.wuPalmerTest.models.VerbPair;
import utils.NLPUtils;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs = AnnotatedVerbPairsController.getAllAnnotatedVerbPairs();
        SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();

        calculateMetrics(allAnnotatedVerbPairs, similarity, 0.75);
        calculateMetrics(allAnnotatedVerbPairs, similarity, 0.80);
        calculateMetrics(allAnnotatedVerbPairs, similarity, 0.85);
        calculateMetrics(allAnnotatedVerbPairs, similarity, 0.90);
        calculateMetrics(allAnnotatedVerbPairs, similarity, 0.95);
    }

    private static void calculateMetrics(ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs, SemanticSentenceSimilarity similarity, double minScore) {
        int total = 0;
        int precisionCount = 0;
        int recallCount = 0;

        for (AnnotatedVerbPair verbPair : allAnnotatedVerbPairs) {
            String sourceVerb = verbPair.getSourceVerb();
            String targetVerb = verbPair.getTargetVerb();
            double wordSimilarity = similarity.wordSimilarity(sourceVerb, POS.v, targetVerb, POS.v);
            if (wordSimilarity > minScore) {
                total++;
                if (verbPair.getAnnotation() == 1) {
                    precisionCount++;
                }
            } else if (verbPair.getAnnotation() == 1) {
                recallCount++;
            }
        }

        System.out.println("> " + minScore);
        System.out.println("================");
        System.out.println("Total: " + total);
        System.out.println("PrecisionCount: " + precisionCount);
        System.out.println("RecallCount: " + recallCount);

        double precision = (double) precisionCount / (double) total;
        double recall = (double) precisionCount / ((double) precisionCount + (double) recallCount);
        double fMeasure = getFMeasure(precision, recall);

        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F-Measure: " + fMeasure);
        System.out.println();
    }

    private static void insertVerbPairsWithSpecificScoreToDB() {
        ArrayList<SentencePair> allSentencePairs = SentencePairsController.getAllSentencePairs(-1);

        NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos,lemma");
        for (SentencePair sentencePair : allSentencePairs) {
            System.out.println("SentencePair: " + sentencePair.getId());
            ArrayList<String> sourceVerbs = getSourceVerbs(sentencePair.getSourceSentence(), nlpUtils);
            ArrayList<String> targetVerbs = getTargetVerbs(sentencePair.getTargetSentence(), nlpUtils);

            SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();
            for (String sVerb : sourceVerbs) {
                for (String tVerb : targetVerbs) {
                    double wordSimilarity = similarity.wordSimilarity(sVerb, POS.v, tVerb, POS.v);
                    if (wordSimilarity > 0.75) {
                        VerbPair verbPair = new VerbPair(sentencePair.getId(), sVerb, tVerb);
                        boolean inserted = VerbPairsController.insertVerbPairToDB(verbPair);
                        if (!inserted) {
                            System.out.println("Insertion failed: " + sentencePair.getId());
                        }
                    }
                }
            }
        }
    }

    private static ArrayList<String> getSourceVerbs(Sentence sourceSentence, NLPUtils nlpUtils) {
        return nlpUtils.getLemmaVerbsWithOutBe(nlpUtils.annotate(sourceSentence.getSentence()));
    }

    private static ArrayList<String> getTargetVerbs(Sentence targetSentence, NLPUtils nlpUtils) {
        return nlpUtils.getLemmaVerbsWithOutBe(nlpUtils.annotate(targetSentence.getSentence()));
    }

    public static double getFMeasure(double precision, double recall) {
        return 2 * precision * recall / (precision + recall);
    }
}
