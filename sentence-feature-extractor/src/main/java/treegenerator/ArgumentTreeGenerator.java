package treegenerator;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import treegenerator.Utils.Utils;
import utils.NLPUtils;

import java.io.FileNotFoundException;
import java.util.*;

public class ArgumentTreeGenerator {
    //an Array to store the names of Legal Persons
    private static final ArrayList<String> SUBJECT_LIST = new ArrayList<>(
            Arrays.asList("petitioner", "government", "defendant"));
    private static ArrayList<SentenceModel> sentenceModels;
    private static ArrayList<SentenceModel> subjectSentenceModel=new ArrayList<>();
    private static ArrayList<String> caseSubjects;


    public static void main(String[] args) throws FileNotFoundException {
       // Scanner sc = new Scanner(new File("G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\src\\main\\resources\\Cases\\Lee.txt"));
        // creates a StanfordCoreNLP object, with annotators
        Case legalCase = new Case();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie,ner");
        //StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        NLPUtils nlpUtils = new NLPUtils(props);
        sentenceModels =new ArrayList<>();
        caseSubjects=new ArrayList<>();

        //read case
        /*String filePath = new File("").getAbsolutePath();
        filePath += "/src/main/resources/case2.txt";*/


        String filePath;
        //just cmmnt the following line to change file path
        filePath="G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\src\\main\\resources\\Cases\\Lee.txt";

        sentenceModels=sentenceSplitter(filePath,sentenceModels);

        sentenceModels=preProcess(sentenceModels);

        sentenceModels=extractTriples(sentenceModels,nlpUtils);

        sentenceModels=assignSubjects(sentenceModels);


        for (SentenceModel sentenceModel:sentenceModels){
            /*System.out.println("_____________________");
            System.out.println("sentence");
            System.out.println(sentenceModel.sentence);
            System.out.println("subjects");*/
            if(sentenceModel.legalSubjects.size()>0){
                for(String subject:sentenceModel.subjects){
                    System.out.println(subject);
                }
            }
        }





        for (SentenceModel sentenceModel:subjectSentenceModel){
            System.out.println("subject "+ sentenceModel.sentence);
        }


        for (SentenceModel sentenceModel:sentenceModels){
            if(sentenceModel.legalSubjects.size()>0){
                System.out.println("------------------------");
                System.out.println("__________________________");
                for (String subject : sentenceModel.legalSubjects){
                    System.out.println(sentenceModel.sentence);
                    System.out.println(subject);
                }

            }else {
                System.out.println(sentenceModel.sentence);
            }
        }




    }

    public static ArrayList<SentenceModel> sentenceSplitter(String filePath, ArrayList<SentenceModel> sentenceModels){
        String textRaw = Utils.readFile(filePath);

        String[] splitted = textRaw.split("\n");

        StringBuilder inputBuilder = new StringBuilder();
        for (int i = 2; i < splitted.length; i++) {
            inputBuilder.append(splitted[i]);
        }

        // this the text after the Held: paragraph
        String text = inputBuilder.toString();

        // Held: paragraph
        String held = splitted[0].split("Held: ")[1];

        System.out.println(held);


        Document doc = new Document(text);
        List<Sentence> sentences1 = doc.sentences();
        int count=1;
        for (Sentence sentence : sentences1) {
            //extract the sentenceModels in the given case

            SentenceModel sentenceModel=new SentenceModel();
            sentenceModel.ID=count;
            sentenceModel.sentence=sentence.toString();
            sentenceModel.processedSentence=sentence.toString();
            sentenceModels.add(sentenceModel);
            count++;

        }


        return sentenceModels;


    }

    public static ArrayList<SentenceModel> preProcess(ArrayList<SentenceModel> sentenceModels){
        for (SentenceModel sentenceModel:sentenceModels){

            //replace words that misleads triple extraction
            String ss = sentenceModel.processedSentence.replaceAll("(that,|that|'s)", "");
            sentenceModel.processedSentence=ss;


        }
        return sentenceModels;
    }

    public static ArrayList<SentenceModel> extractTriples(ArrayList<SentenceModel> sentenceModels,NLPUtils nlpUtils){
        for(SentenceModel sentenceModel:sentenceModels) {
            String sentenceExtracting = sentenceModel.processedSentence;

            // run all Annotators on this text
            Annotation annotatedSentence = nlpUtils.annotate(sentenceExtracting);
            ;

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = annotatedSentence.get(CoreAnnotations.SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {


                //extracting triples from the sentence
                Collection<RelationTriple> triples =
                        sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

                for (RelationTriple triple : triples) {
                    sentenceModel.triples.add(triple);
                }
            }
        }
        return sentenceModels;


    }

    public static ArrayList<SentenceModel> assignSubjects(ArrayList<SentenceModel> sentenceModels){

        for (SentenceModel sentenceModel:sentenceModels){
            String subject;
            for(RelationTriple triple:sentenceModel.triples){
                subject=triple.subjectLemmaGloss();
                if(!sentenceModel.subjects.contains(subject.toLowerCase())){
                    sentenceModel.subjects.add(subject.toLowerCase());
                    if(SUBJECT_LIST.contains(subject.toLowerCase())){
                        sentenceModel.legalSubjects.add(subject.toLowerCase());
                        if(!caseSubjects.contains(subject.toLowerCase())){
                            caseSubjects.add(subject);
                            SentenceModel sentenceModelSubject = new SentenceModel();
                            sentenceModelSubject.sentence=subject.toLowerCase();
                            subjectSentenceModel.add(sentenceModelSubject);
                        }
                    }

                }
            }
        }
        return sentenceModels;

    }





}
