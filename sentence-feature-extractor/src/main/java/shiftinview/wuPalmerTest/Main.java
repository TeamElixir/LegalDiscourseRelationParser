package shiftinview.wuPalmerTest;

import edu.cmu.lti.jawjaw.pobj.POS;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import shiftinview.wuPalmerTest.controllers.AnnotatedVerbPairsController;
import shiftinview.wuPalmerTest.controllers.SentencePairsController;
import shiftinview.wuPalmerTest.controllers.VerbPairsController;
import shiftinview.wuPalmerTest.controllers.VerbPairsWithAllScoresController;
import shiftinview.wuPalmerTest.models.*;
import utils.NLPUtils;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs = AnnotatedVerbPairsController.getAllAnnotatedVerbPairs();
        SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();

        System.out.println(allAnnotatedVerbPairs.size());

        ArrayList<VerbPairWithAllScores> verbPairsWithAllScores = new ArrayList<>();

        for (AnnotatedVerbPair avp : allAnnotatedVerbPairs) {
            double[] allScores = similarity.getAllWordSimilarityScores(avp.getSourceVerb(), POS.v,
                    avp.getTargetVerb(), POS.v);

            VerbPairWithAllScores vpwal = new VerbPairWithAllScores(avp, allScores);
            boolean inserted = VerbPairsWithAllScoresController.insertVerbPairToDB(vpwal);
            if(!inserted) {
                System.out.println("Not Inserted!");
                System.out.println(vpwal);
            }
//            System.out.println(vpwals);
//            System.out.println();
//            verbPairsWithAllScores.add(vpwals);
        }

//        System.out.println(verbPairsWithAllScores.size());

    }

    private static void indWuPalmerThreshold() {
        ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs = AnnotatedVerbPairsController.getAllAnnotatedVerbPairs();
        SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();

        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.75);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.80);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.85);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.86);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.87);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.88);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.89);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.90);
        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.95);
    }

    private static void checkWuPalmerAccuracy(ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs,
                                              SemanticSentenceSimilarity similarity, double minScore) {
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
