package treegenerator;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import treegenerator.Utils.Utils;

import java.io.FileNotFoundException;
import java.util.*;

public class ArgumentTreeGenerator {
    //an Array to store the names of Legal Persons
    private static final ArrayList<String> SUBJECT_LIST = new ArrayList<>(
            Arrays.asList("Petitioner", "Government", "Defendant"));
    private static ArrayList<SentenceModel> sentenceModels;

    public static void main(String[] args) throws FileNotFoundException {
       // Scanner sc = new Scanner(new File("G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\src\\main\\resources\\Cases\\Lee.txt"));
        // creates a StanfordCoreNLP object, with annotators
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie,ner");
        //StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        sentenceModels =new ArrayList<>();

        //read case
        /*String filePath = new File("").getAbsolutePath();
        filePath += "/src/main/resources/case2.txt";*/


        String filePath;
        //just cmmnt the following line to change file path
        filePath="G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\src\\main\\resources\\Cases\\Lee.txt";

        sentenceSplitter(filePath);
    }

    public static void sentenceSplitter(String filePath){
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
        for (Sentence sentence : sentences1) {
            //extract the sentenceModels in the given case

            SentenceModel sentenceModel=new SentenceModel();
            sentenceModel.sentence=sentence.toString();
            sentenceModels.add(sentenceModel);

        }

        for (SentenceModel sentenceModel:sentenceModels){
            System.out.println(sentenceModel.sentence);
        }


    }



}
