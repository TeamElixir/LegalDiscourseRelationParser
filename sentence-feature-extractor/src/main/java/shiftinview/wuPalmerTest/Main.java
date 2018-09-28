package shiftinview.wuPalmerTest;

import edu.cmu.lti.jawjaw.pobj.POS;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import shiftinview.wuPalmerTest.controllers.AnnotatedVerbPairsController;
import shiftinview.wuPalmerTest.controllers.SentencePairsController;
import shiftinview.wuPalmerTest.controllers.VerbPairsController;
import shiftinview.wuPalmerTest.controllers.VerbPairsWithAllScoresController;
import shiftinview.wuPalmerTest.models.*;
import utils.NLPUtils;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        checkAccuracyOfMeasures();
    }

    private static void checkAccuracyOfMeasures() {
        ArrayList<VerbPairWithAllScores> verbPairsWithAllScores = VerbPairsWithAllScoresController.getAllAnnotatedVerbPairs();

        double[] values = {0.75, 0.80, 0.85, 0.86, 0.87, 0.88, 0.89, 0.90, 0.95};
        for (double value : values) {
            checkSimilarityMeasureAccuracy(Constants.JIAN_CONRATH, verbPairsWithAllScores, value);
        }

        for (double value : values) {
            checkSimilarityMeasureAccuracy(Constants.LIN, verbPairsWithAllScores, value);
        }

        for (double value : values) {
            checkSimilarityMeasureAccuracy(Constants.WU_PALMER, verbPairsWithAllScores, value);
        }
    }

    private static void insertVerbPairsWithAllScoresToDB() {
        ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs = AnnotatedVerbPairsController.getAllAnnotatedVerbPairs();
        SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();

        System.out.println(allAnnotatedVerbPairs.size());

        for (AnnotatedVerbPair avp : allAnnotatedVerbPairs) {
            double[] allScores = similarity.getAllWordSimilarityScores(avp.getSourceVerb(), POS.v,
                    avp.getTargetVerb(), POS.v);

            VerbPairWithAllScores vpwal = new VerbPairWithAllScores(avp, allScores);
            boolean inserted = VerbPairsWithAllScoresController.insertVerbPairToDB(vpwal);
            if (!inserted) {
                System.out.println("Not Inserted!");
                System.out.println(vpwal);
            }
        }
    }

    private static void checkWuPalmerThreshold() {
        System.out.println("Wu-Palmer");
        ArrayList<AnnotatedVerbPair> allAnnotatedVerbPairs = AnnotatedVerbPairsController.getAllAnnotatedVerbPairs();
        SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();

        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.75);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.80);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.85);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.86);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.87);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.88);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.89);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.90);
//        checkWuPalmerAccuracy(allAnnotatedVerbPairs, similarity, 0.95);
    }

    private static void checkSimilarityMeasureAccuracy(String measure, ArrayList<VerbPairWithAllScores> verbPairs, double minScore) {
        int total = 0;
        int precisionCount = 0;
        int recallCount = 0;

        switch (measure) {
            case Constants.JIAN_CONRATH: {
                System.out.println(Constants.JIAN_CONRATH + " > " + minScore);
                System.out.println("===========================");
                for (VerbPairWithAllScores verbPair : verbPairs) {
                    if (verbPair.getJiangConrath() > minScore) {
                        total++;
                        if (verbPair.getAnnotation() == 1) {
                            precisionCount++;
                        }
                    } else if (verbPair.getAnnotation() == 1) {
                        recallCount++;
                    }
                }
            }
            break;
            case Constants.LIN: {
                System.out.println(Constants.LIN + " > " + minScore);
                System.out.println("===========================");
                for (VerbPairWithAllScores verbPair : verbPairs) {
                    if (verbPair.getLin() > minScore) {
                        total++;
                        if (verbPair.getAnnotation() == 1) {
                            precisionCount++;
                        }
                    } else if (verbPair.getAnnotation() == 1) {
                        recallCount++;
                    }
                }
            }
            break;

            case Constants.WU_PALMER: {
                System.out.println(Constants.WU_PALMER + " > " + minScore);
                System.out.println("===========================");
                for (VerbPairWithAllScores verbPair : verbPairs) {
                    if (verbPair.getWuPalmer() > minScore) {
                        total++;
                        if (verbPair.getAnnotation() == 1) {
                            precisionCount++;
                        }
                    } else if (verbPair.getAnnotation() == 1) {
                        recallCount++;
                    }
                }
            }
            break;

            default: {
                System.out.println("Enter a valid similarity measure");
            }
        }

        System.out.println("Total: " + total);
        System.out.println("PrecisionCount: " + precisionCount);
        System.out.println("RecallCount: " + recallCount);

        double precision = calculatePrecision(precisionCount, total);
        double recall = calculateRecall(precisionCount, recallCount);
        double fMeasure = getFMeasure(precision, recall);

        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F-Measure: " + fMeasure);
        System.out.println();

        writeResultsToCSV(measure, minScore, precision, recall, fMeasure);

    }

    private static void writeResultsToCSV(String measure, double minScore, double precision, double recall, double fMeasure) {
        try {
            File f = new File("results.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            StringBuilder sb = new StringBuilder();
            sb.append(measure);
            sb.append(",");
            sb.append(minScore);
            sb.append(",");
            sb.append(precision);
            sb.append(",");
            sb.append(recall);
            sb.append(",");
            sb.append(fMeasure);
            sb.append("\n");

            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    // identified as similar by the measure
                    // and, actually similar pair of verbs
                    precisionCount++;
                }
            } else if (verbPair.getAnnotation() == 1) {
                // not identified as similar by the measure
                // but, actually similar pair of verbs
                recallCount++;
            }
        }

        printAccuracyStats(minScore, total, precisionCount, recallCount);
    }

    private static void printAccuracyStats(double minScore, int total, int precisionCount, int recallCount) {
        System.out.println("Total: " + total);
        System.out.println("PrecisionCount: " + precisionCount);
        System.out.println("RecallCount: " + recallCount);

        double precision = calculatePrecision(precisionCount, total);
        double recall = calculateRecall(precisionCount, recallCount);
        double fMeasure = getFMeasure(precision, recall);

        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F-Measure: " + fMeasure);
        System.out.println();
    }


    private static double calculatePrecision(int precisionCount, int total) {
        return (double) precisionCount / (double) total;
    }

    private static double calculateRecall(int precisionCount, int recallCount) {
        return (double) precisionCount / ((double) precisionCount + (double) recallCount);
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
