package shiftinview.resultsAnalyzer;

import shiftinview.models.ShiftInViewPair;
import utils.SQLiteUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class resultShiftInView {

    public static ArrayList<Integer> verbPairs = new ArrayList<>();
    public static void main(String[] args) throws SQLException {

       getShiftInView();

    }

    public static ArrayList<Integer> getShiftInView(){
        try {
            ArrayList<ShiftInViewPair> pairs = ShiftInViewPair.getAll();

            int count = 0;

            // executes sql and fills up the array list
            SQLiteUtils sqLiteUtils = new SQLiteUtils();



            for (ShiftInViewPair pair:pairs) {

                //String sql = "SELECT SENTENCE FROM LEGAL_SENTENCE where ID ="+pair.getTargetSentence()+";";
                if(pair.getVerbShift()==1) {
                    String sql = "SELECT SSID,TSID from FEATURE_ENTRY_LEGAL_SENTENCE where ID =" + pair.getRelationshipId() + ";";
                    ResultSet resultSet = sqLiteUtils.executeQuery(sql);
                    while (resultSet.next()) {
                        System.out.println(/*"Pair ID : "*/ + pair.getRelationshipId());
                        verbPairs.add(pair.getRelationshipId());
                       /* System.out.println("SSID :" + resultSet.getString("SSID"));
                        System.out.println("TSOD :" + resultSet.getString("TSID"));*/
                        pair.setSourceSentenceID(Integer.parseInt(resultSet.getString("SSID")));
                        pair.setTargetSentenceID(Integer.parseInt(resultSet.getString("TSID")));
                        count++;
//                        sentences.add(resultSet.getString("SENTENCE"));
                    }

                    String sqlTarget = "SELECT * from LEGAL_SENTENCE where ID =" + pair.getTargetSentenceID() + ";";
                    ResultSet resultSet2 = sqLiteUtils.executeQuery(sqlTarget);
                    System.out.println("Target Sentence : "+ resultSet2.getString("SENTENCE") );
                    String sqlSource = "SELECT * from LEGAL_SENTENCE where ID =" + pair.getSourceSentenceID() + ";";
                    ResultSet resultSet3 = sqLiteUtils.executeQuery(sqlSource);
                    System.out.println("Source Sentence : "+ resultSet3.getString("SENTENCE") );
                }
            }

            System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return verbPairs;
    }

}
