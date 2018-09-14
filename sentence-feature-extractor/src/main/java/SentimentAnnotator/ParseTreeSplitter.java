package SentimentAnnotator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCostAndGradient;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParseTreeSplitter {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,depparse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String sentence = "The Government contends that Lee cannot show prejudice from accepting a plea where his only hope at trial was that something unexpected and unpredictable might occur that would lead to acquittal.";
        Annotation ann = new Annotation(sentence);
        pipeline.annotate(ann);

        try {
            CustomizedSentimentAnnotator.addSentimentLayerToCoreNLPSentiment(
                    "DeviatedSentimentWords/non_positive_mini.csv",
                    "DeviatedSentimentWords/non_negative_mini.csv",
                    "DeviatedSentimentWords/non_neutral_mini.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(processParseTree(parseTree(ann),pipeline));
    }

    public static String parseTree(Annotation ann){
        List<CoreMap> sentences = ann.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            return tree.toString();
        }
        return null;
    }

    public static String processParseTree(String text, StanfordCoreNLP pipeline){
        //to split from the pattern SBAR IN
        String[] phraseList = text.split("\\(SBAR \\(IN [a-z]+\\)");

        int count = 0;
        for(String phrase : phraseList){
            phrase = phrase.replaceAll("\\(","").replaceAll("\\)","").replaceAll("[A-Z]+ ","").replaceAll(" [\\.]"," ").trim() +".";
            phraseList[count] = phrase;
            intermediate_execution(phrase,pipeline);
            System.out.println(phrase);
            count += 1;
        }

        System.out.println(phraseList[1]);
        return null;
    }

    public static void intermediate_execution(String text, StanfordCoreNLP pipeline){
        Annotation ann = new Annotation(text);
        pipeline.annotate(ann);

        CustomizedSentimentAnnotator.createPosTagMapForSentence(ann);

        System.out.println(findSubject(ann));
    }

    //outputs subject for a given sentence part
    public static String findSubject(Annotation ann){
        ArrayList<String> subjectList = new ArrayList<>();
        List<CoreMap> sentences = ann.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sent : sentences) {
            SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
            SentimentClassification(sent);

            for (TypedDependency td : sg.typedDependencies()) {
                if (td.reln().toString().equals("nsubj") || td.reln().equals("nsubjpass")) {
                    return td.dep().originalText();
                }
            }
        }

        return null;
    }

    public static void SentimentClassification(CoreMap coreMapSentence){
        final Tree tree = coreMapSentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        final SimpleMatrix sm = RNNCoreAnnotations.getPredictions(tree);
        final String sentiment = coreMapSentence.get(SentimentCoreAnnotations.SentimentClass.class);
        System.out.println("sentence:  " + coreMapSentence);
        System.out.println("sentiment: " + sentiment);

        System.out.println(sm.toString().split("\n")[2]);
    }
}
