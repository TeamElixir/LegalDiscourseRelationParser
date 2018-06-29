package shifinview;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import utils.NLPUtils;

import java.util.Properties;

public class ConstituentParser {
    public void runConstituentParser(Annotation annotation, NLPUtils nlpUtils){

        nlpUtils.constituentParse(annotation);
    }
}
