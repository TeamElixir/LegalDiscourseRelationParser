package SentimentAnnotator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCostAndGradient;
import edu.stanford.nlp.util.CoreMap;
import utils.NLPUtils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

public class SentimentDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,depparse,sentiment");
        NLPUtils nlpUtils = new NLPUtils(props);

        //String filePath = "/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/";
        String filePath = "/home/viraj/FYP/";

        try {
            CustomizedSentimentAnnotator.addSentimentLayerToCoreNLPSentiment(
                    filePath + "DeviatedSentimentWords/non_positive_mini.csv",
                    filePath + "DeviatedSentimentWords/non_negative_mini.csv",
                    filePath + "DeviatedSentimentWords/non_neutral_mini.csv");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String sentence= "Sam is charged with a crime";

        System.out.println(calculateSentiment(nlpUtils,sentence));
    }

    public static String calculateSentiment(NLPUtils nlpUtils, String text){
        SentimentCostAndGradient.createPosTagMap();

        Annotation ann = nlpUtils.annotate(text);

        //to create the Pos tag map
        CustomizedSentimentAnnotator.createPosTagMapForSentence(ann);

        //this line is required, after creating POS tag map needs to annotate again
        ann = nlpUtils.annotate(text);

        List<CoreMap> sentences = ann.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sent : sentences) {
            return ParseTreeSplitter.SentimentClassification(sent);
        }
        return null;
    }
}
