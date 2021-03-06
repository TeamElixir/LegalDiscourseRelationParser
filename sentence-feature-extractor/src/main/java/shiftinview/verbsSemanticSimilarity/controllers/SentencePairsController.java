package shiftinview.verbsSemanticSimilarity.controllers;

import shiftinview.verbsSemanticSimilarity.DBCon;
import shiftinview.verbsSemanticSimilarity.models.Relation;
import shiftinview.verbsSemanticSimilarity.models.Sentence;
import shiftinview.verbsSemanticSimilarity.models.SentencePair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SentencePairsController {
    public static ArrayList<SentencePair> getAllSentencePairs(int limit) {
        Connection conn = DBCon.getConnection();
        ArrayList<SentencePair> sentencePairs = new ArrayList<>();
        ResultSet resultSet;
        String query = "";
        if (limit > 0) {
            query = "SELECT * FROM " + SentencePair.TABLE_NAME + " LIMIT " + limit;
        } else {
            query = "SELECT * FROM " + SentencePair.TABLE_NAME;
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");

                int sourceSentenceID = resultSet.getInt("SSID");
                Sentence sourceSentence = SentencesController.getSentenceById(sourceSentenceID);

                int targetSentenceID = resultSet.getInt("TSID");
                Sentence targetSentence = SentencesController.getSentenceById(targetSentenceID);

                int relationID = resultSet.getInt("TYPE");
                String relation = Relation.getRelation(relationID);

                SentencePair sentencePair = new SentencePair(id, sourceSentence, targetSentence, relation);
                sentencePairs.add(sentencePair);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sentencePairs;
    }
}
