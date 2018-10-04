package shiftinview.verbsSemanticSimilarity.controllers;

import shiftinview.verbsSemanticSimilarity.DBCon;
import shiftinview.verbsSemanticSimilarity.models.VerbPair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VerbPairsController {

    public static boolean insertVerbPairToDB(VerbPair verbPair) {
        Connection conn = DBCon.getConnection();

        String query = "INSERT INTO " + VerbPair.TABLE_NAME + "(SENTENCE_PAIR_ID, SOURCE_VERB, TARGET_VERB)" +
                " VALUES(?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, verbPair.getSentencePairID());
            ps.setString(2, verbPair.getSourceVerb());
            ps.setString(3, verbPair.getTargetVerb());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static ArrayList<VerbPair> getAllVerbPairs() {
        Connection conn = DBCon.getConnection();
        ArrayList<VerbPair> verbPairs = new ArrayList<>();
        ResultSet resultSet;
        String query = "SELECT * FROM " + VerbPair.TABLE_NAME;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int sentencePairID = resultSet.getInt("SENTENCE_PAIR_ID");
                String sourceVerb = resultSet.getString("SOURCE_VERB");
                String targetVerb = resultSet.getString("TARGET_VERB");

                VerbPair vp = new VerbPair(id, sentencePairID, sourceVerb, targetVerb);

                verbPairs.add(vp);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return verbPairs;
    }
}
