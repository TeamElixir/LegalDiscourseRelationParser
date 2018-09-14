package SentimentAnnotator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParseTreeSplitter {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,depparse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String sentence = "The Government contends that Lee cannot show prejudice from accepting a plea where his only hope at trial was that something unexpected and unpredictable might occur that would lead to acquittal.";
        Annotation ann = new Annotation(sentence);
        pipeline.annotate(ann);

        System.out.println(processParseTree(parseTree(ann)));
    }

    public static String parseTree(Annotation ann){
        List<CoreMap> sentences = ann.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            return tree.toString();
        }
        return null;
    }

    public static String processParseTree(String text){
        //to split from the pattern SBAR IN
        String[] phraseList = text.split("\\(SBAR \\(IN [a-z]+\\)");

        int count = 0;
        for(String phrase : phraseList){
            phrase = phrase.replaceAll("\\(","").replaceAll("\\)","").replaceAll("[A-Z]+ ","").trim();
            phraseList[count] = phrase;
            count += 1;
        }

        System.out.println(phraseList[1]);
        return null;
    }



    //outputs subject for a given sentence part
    public static String findSubject(Annotation ann){
        ArrayList<String> subjectList = new ArrayList<>();
        List<CoreMap> sentences = ann.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sent : sentences) {
            SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

            for (TypedDependency td : sg.typedDependencies()) {
                if (td.reln().toString().equals("nsubj") || td.reln().equals("nsubjpass")) {
                    return td.dep().toString();
                }
            }
        }

        return null;
    }
}
