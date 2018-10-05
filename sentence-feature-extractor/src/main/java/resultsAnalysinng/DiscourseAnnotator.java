package resultsAnalysinng;

import scalaz.Category;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DiscourseAnnotator {

    public static HashMap<Integer, AnnotatedPair> map = new HashMap<Integer, AnnotatedPair>();

    public static void main(String[] args) throws SQLException {

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

        for (HashMap.Entry<Integer,AnnotatedPair> entry:map.entrySet()){

            System.out.println(entry.getValue().pairId);
            System.out.println("no of users:" + entry.getValue().annotatedUsers);
        }




    }
}
