package shiftinview.verbsSemanticSimilarity.controllers;

import shiftinview.verbsSemanticSimilarity.DBCon;
import shiftinview.verbsSemanticSimilarity.models.AnnotatedVerbPair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnnotatedVerbPairsController {

    static int getAnnotationOfVerbPair(int id) {
        Connection conn = DBCon.getConnection();
        String query = "SELECT ANNOTATION FROM " + AnnotatedVerbPair.TABLE_NAME + " WHERE id=" + id;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            int annotation = rs.getInt("ANNOTATION");
            return annotation;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ArrayList<AnnotatedVerbPair> getAllAnnotatedVerbPairs() {
        Connection conn = DBCon.getConnection();
        ArrayList<AnnotatedVerbPair> annotatedVerbPairs = new ArrayList<>();
        ResultSet resultSet;
        String query = "SELECT * FROM " + AnnotatedVerbPair.TABLE_NAME;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int sentencePairID = resultSet.getInt("SENTENCE_PAIR_ID");
                String sourceVerb = resultSet.getString("SOURCE_VERB");
                String targetVerb = resultSet.getString("TARGET_VERB");
                int annotation = resultSet.getInt("ANNOTATION");

                AnnotatedVerbPair vp = new AnnotatedVerbPair(id, sentencePairID, sourceVerb, targetVerb, annotation);

                annotatedVerbPairs.add(vp);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return annotatedVerbPairs;
    }
}
