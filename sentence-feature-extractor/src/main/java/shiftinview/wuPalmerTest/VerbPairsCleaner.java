package shiftinview.wuPalmerTest;

import shiftinview.wuPalmerTest.controllers.VerbPairsController;
import shiftinview.wuPalmerTest.models.VerbPair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class VerbPairsCleaner {
    public static void main(String[] args) {
        ArrayList<VerbPair> allVerbPairs = VerbPairsController.getAllVerbPairs();
//        for (int i = 0; i < allVerbPairs.size(); i++) {
//            allVerbPairs.set(i, allVerbPairs.get(i).swapSourceTarget());
//        }

        ArrayList<VerbPair> distinctVerbPairs = new ArrayList<>();

        Outer:
        for (VerbPair verbPair : allVerbPairs) {
            for (VerbPair verbPair2 : distinctVerbPairs) {
                if (verbPair.equals(verbPair2)) {
                    continue Outer;
                }
            }
            distinctVerbPairs.add(verbPair);
        }

        System.out.println(allVerbPairs.size());
        System.out.println(distinctVerbPairs.size());

        writeToCsv(distinctVerbPairs);
    }

    private static void writeToCsv(ArrayList<VerbPair> verbPairs) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("test.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ID");
        sb.append(',');
        sb.append("SentencePairID");
        sb.append(',');
        sb.append("SourceVerb");
        sb.append(',');
        sb.append("TargetVerb");
        sb.append('\n');

        for (VerbPair verbPair : verbPairs) {
            sb.append(verbPair.getId());
            sb.append(',');
            sb.append(verbPair.getSentencePairID());
            sb.append(',');
            sb.append(verbPair.getSourceVerb());
            sb.append(',');
            sb.append(verbPair.getTargetVerb());
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }
}
