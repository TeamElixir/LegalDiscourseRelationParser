package shifinview;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import utils.NLPUtils;

import java.util.ArrayList;
import java.util.Properties;

public class ShifInViewAnalyzer {
    public static void main(String[] args) {
        String targetSentence = "Although he has lived in this country for most of his life, Lee is not a United States" +
                " citizen, and he feared that a criminal conviction might affect his status as a lawful " +
                "permanent resident.";
        String sourceSentence = "His attorney assured him there was nothing to worry about--the Government would not " +
                "deport him if he pleaded guilty.\n";

        ArrayList<String> coreferencedSentences = new ArrayList<>();

        // set up pipeline properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        // use faster shift reduce parser
        props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        props.setProperty("parse.maxlen", "100");
        // set up Stanford CoreNLP pipeline
       // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        NLPUtils nlpUtils =  new NLPUtils(props);
        // build annotation for a review
        Annotation annotation = nlpUtils.annotate(targetSentence);




       /* Coreferencer coreferencer = new Coreferencer();
        coreferencedSentences=Coreferencer.getCoreferencedSentences(sourceSentence,targetSentence,nlpUtils);
        System.out.println(coreferencedSentences.get(0));
        System.out.println(coreferencedSentences.get(1));*/
       ConstituentParser constituentParser = new ConstituentParser();
       constituentParser.runConstituentParser(annotation,nlpUtils);

    }
}
