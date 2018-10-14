package resultsAnalysinng;

import scalaz.Category;
import shiftinview.resultsAnalyzer.resultShiftInView;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DiscourseAnnotator {

    public static HashMap<Integer, AnnotatedPair> map = new HashMap<Integer, AnnotatedPair>();
    public static ArrayList verbMethod;

    public static void main(String[] args) throws SQLException {

        //verbMethod= resultShiftInView.getShiftInView();
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "123456");


        Connection connection = DriverManager.getConnection("jdbc:mysql://142.93.244.96:3306/discourse_annotator", props);
        Statement statement = connection.createStatement();

        String sql1 = "Select pair_id, user_id, relation from pair_user_annotations ";
        ResultSet resultSet1 = statement.executeQuery(sql1);

        while (resultSet1.next()){
            int pairId = resultSet1.getInt("pair_id");
            if (!map.containsKey(pairId)) {

                AnnotatedPair annotatedPair = new AnnotatedPair();
                annotatedPair.pairId = pairId;
                // annotatedPair.svmRelation=resultSet1.getInt("relation");
                annotatedPair.userId1 = resultSet1.getInt("user_id");
                annotatedPair.userRelation1 = resultSet1.getInt("relation");
                annotatedPair.annotatedUsers=1;
                map.put(pairId,annotatedPair);
            }else{
                AnnotatedPair annotatedPair = map.get(pairId);
                annotatedPair.userId2 = resultSet1.getInt("user_id");
                annotatedPair.userRelation2 = resultSet1.getInt("relation");
                annotatedPair.annotatedUsers=2;
                map.put(pairId,annotatedPair);
            }
        }

        int count=0;
        for (HashMap.Entry<Integer,AnnotatedPair> entry:map.entrySet()){
            AnnotatedPair ap=entry.getValue();
            if(ap.userRelation1==5 || ap.userRelation2==5){

                    String sql2 = "Select source_sntc_id, target_sntc_id from sentence_pairs_from_algorithm where id ="+ap.pairId;
                    ResultSet resultSet2 = statement.executeQuery(sql2);
                    System.out.println(ap.pairId);
                    int sourceId=-1;
                    int targetId=-2;
                    while (resultSet2.next()) {
                       // System.out.println("aa");
                        sourceId = resultSet2.getInt("source_sntc_id");
                        targetId = resultSet2.getInt("target_sntc_id");
                    }
                    String sql3 = "Select sentence from sentences where id ="+targetId;
                    ResultSet resultSetTarget = statement.executeQuery(sql3);

                while (resultSetTarget.next()){
                    System.out.println("Target : "+resultSetTarget.getString("sentence"));
                }

                    String sql4 = "Select sentence from sentences where id ="+sourceId;
                    ResultSet resultSetSource = statement.executeQuery(sql4);



                    while (resultSetSource.next()){
                        System.out.println("Source : "+resultSetSource.getString("sentence"));
                    }

                    count++;


                //System.out.println("no of users:" + entry.getValue().annotatedUsers);
            }
        }
        System.out.println(count);




    }
}
