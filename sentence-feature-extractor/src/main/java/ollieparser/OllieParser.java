package ollieparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OllieParser {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = new File("").getAbsolutePath();
        filePath += "/src/main/resources/Ollie_text/sample.txt";

        ArrayList<OllieSentence> sentenceList = new ArrayList<>();

        Scanner sc = new Scanner(new File(filePath));

        while(sc.hasNextLine()){
            OllieSentence os = new OllieSentence();
            sentenceList.add(os);

            //the whole sentence
            os.text = sc.nextLine();

            String nextLine = sc.nextLine();

            //if there are no triples for the given sentence
            if(nextLine.contains("No extractions found.")){
                sc.nextLine();
                continue;
            }

            while(nextLine.matches("^[01]\\.[0-9]+:.*")){
                OllieTriple ot= new OllieTriple();

                ot.confidence = Double.parseDouble(nextLine.substring(0,nextLine.indexOf(":")));

                String intermediateString = nextLine.substring(nextLine.indexOf("(")+1, nextLine.length()-1);
                String[] tripleArray = intermediateString.split(";");

                ot.subject = tripleArray[0].trim();
                ot.relation = tripleArray[1].trim();
                ot.object = tripleArray[2].trim();

                os.arrayList.add(ot);

                nextLine = sc.nextLine();
                if(nextLine.length()==0){
                    break;
                }
            }


        }

        for(OllieSentence os:sentenceList){
            System.out.println(os.text);
            for(OllieTriple ot:os.arrayList){
                System.out.print(ot.confidence);
                System.out.println("  "+ot.subject+"    ----  "+ ot.relation+" ----  "+ot.object);
            }
            System.out.println();
        }


    }
}
