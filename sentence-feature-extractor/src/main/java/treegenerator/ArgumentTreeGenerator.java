package treegenerator;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import treegenerator.Utils.Utils;
import utils.NLPUtils;


import java.io.IOException;
import java.util.*;

public class ArgumentTreeGenerator {
    //an Array to store the names of Legal Persons
    private static final ArrayList<String> SUBJECT_LIST = new ArrayList<>(
            Arrays.asList("petitioner", "government", "defendant"));
    private static ArrayList<SentenceModel> sentenceModels;
    private static ArrayList<SentenceModel> subjectSentenceModels =new ArrayList<>();
    private static SentenceModel heldSentenceModel=new SentenceModel();
    private static ArrayList<NodeModel> nodeModels;
    private static ArrayList<String> caseSubjects;
    private static DiscourseAPINew discourseAPINew= new DiscourseAPINew();
    private static ArrayList<Node> nodes = new ArrayList<>();

    private static ArrayList<SentenceModel> allSentenceModels=new ArrayList<>();


    public static void main(String[] args) throws IOException {
       // Scanner sc = new Scanner(new File("G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\src\\main\\resources\\Cases\\Lee.txt"));
        // creates a StanfordCoreNLP object, with annotators

        Case legalCase = new Case();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie,ner");
        //StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        props.setProperty("coref.algorithm", "statistical");
        NLPUtils nlpUtils = new NLPUtils(props, "http://104.248.226.230", 9000);
       // NLPUtils nlpUtils = new NLPUtils(props);
        sentenceModels =new ArrayList<>();
        caseSubjects=new ArrayList<>();
        nodeModels=new ArrayList<>();

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


        InitializeIds();


        for (SentenceModel sentenceModel: subjectSentenceModels){
            System.out.println("subject "+ sentenceModel.sentence);
        }

        int nodeCount=0;
        for (SentenceModel sentenceModel:sentenceModels){

            if(sentenceModel.legalSubjects.size()>0){
                System.out.println("------------------------");
                System.out.println("__________________________");
                for (String subject : sentenceModel.legalSubjects){

                    System.out.println("sentence: "+sentenceModel.sentence);
                    System.out.println(subject);
                    System.out.println("id :" + sentenceModel.ID);

                    for(SentenceModel sentenceModelSubject: subjectSentenceModels){
                        if(sentenceModelSubject.sentence.toLowerCase().equals(subject.toLowerCase())){
                            sentenceModel.parentID=sentenceModelSubject.ID;
                        }
                    }
                    NodeModel nodeModel=new NodeModel(subject);
                    System.out.println("bbb");
                    nodeCount=nodeCount+1;
                    nodeModel.id=1;
                    nodeModel.sentences.add(sentenceModel);
                    nodeModels.add(nodeModel);

                }

            }else {
                System.out.println("lll "+nodeCount );
                if(nodeCount<1){

                }else {
                    System.out.println("sentence: "+sentenceModel.sentence);
                    System.out.println("Id: "+sentenceModel.ID);
                    int nodesSize=nodeModels.size();
                    NodeModel nodeModelAnpend = nodeModels.get(nodesSize-1);
                    ArrayList<SentenceModel> nodeSentences=nodeModelAnpend.sentences;
                    int sentencesSize= nodeSentences.size();
                    int i=sentencesSize;
                    while(i>0){
                        if(sentencesSize==1){
                            int parentID=nodeSentences.get(0).ID;
                            sentenceModel.parentID=parentID;
                            nodeSentences.add(sentenceModel);
                            break;
                        }else{
                            int parentIndex = sentencesSize-1;
                            SentenceModel parentSentenceModel = nodeSentences.get(parentIndex);
                            String parentSentence = parentSentenceModel.sentence;

                            int rType=discourseAPINew.getDiscourseType(sentenceModel.sentence,parentSentence,nlpUtils);
                            if(rType==4){
                                sentenceModel.citation=true;
                                sentenceModel.parentID=parentSentenceModel.ID;
                                nodeSentences.add(sentenceModel);
                                break;
                            }else if(rType==2||rType==3||rType==5){
                                sentenceModel.parentID=parentSentenceModel.ID;
                                nodeSentences.add(sentenceModel);
                                break;
                            }else {
                                sentencesSize--;
                            }

                        }
                    }
                }

            }
        }

        allSentenceModels.add(heldSentenceModel);
        for(SentenceModel sentenceModelSubject:sentenceModels){
            allSentenceModels.add(sentenceModelSubject);
        }
        for (SentenceModel sentenceModel:sentenceModels){
            System.out.println("______________________________");
            System.out.println("ID : "+sentenceModel.ID);
            System.out.println("Parent: "+sentenceModel.parentID);
            System.out.println("Sentence: "+sentenceModel.sentence);
            allSentenceModels.add(sentenceModel);
        }

        for(SentenceModel sentenceModel4:allSentenceModels){
            String id = Integer.toString(sentenceModel4.ID);
            String parentId = Integer.toString(sentenceModel4.parentID);
            String text = sentenceModel4.sentence;
            Node node=new Node(id,parentId,text);
            if(sentenceModel4.citation){
                node.setType("Red");

            }
            nodes.add(node);
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
        heldSentenceModel.sentence=held;


        System.out.println(held);


        Document doc = new Document(text);
        List<Sentence> sentences1 = doc.sentences();
        int count=1;
        for (Sentence sentence : sentences1) {
            //extract the sentenceModels in the given case

            SentenceModel sentenceModel=new SentenceModel();
            //sentenceModel.ID=count;
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
                            subjectSentenceModels.add(sentenceModelSubject);
                        }
                    }

                }
            }
        }
        return sentenceModels;

    }

    public static void InitializeIds(){
        int count =0;
        heldSentenceModel.ID=count;
        heldSentenceModel.parentID=-1;
        count++;
        for(SentenceModel sentenceModel: subjectSentenceModels){
            count++;
            sentenceModel.ID=count;
            sentenceModel.parentID=heldSentenceModel.ID;
        }
        for(SentenceModel sentenceModel:sentenceModels){
            count++;
            sentenceModel.ID=count;
        }

    }





}
