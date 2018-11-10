package shiftinview.verbsSemanticSimilarity.controllers;

import shiftinview.verbsSemanticSimilarity.Constants;
import shiftinview.verbsSemanticSimilarity.DBCon;
import shiftinview.verbsSemanticSimilarity.models.AnnotatedVerbPair;
import shiftinview.verbsSemanticSimilarity.models.VerbPairWithAllScores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VerbPairsWithAllScoresController {
    public static boolean insertVerbPairToDB(VerbPairWithAllScores verbPair) {
        Connection conn = DBCon.getConnection();

        String query = "INSERT INTO " + VerbPairWithAllScores.TABLE_NAME + "(ID, SENTENCE_PAIR_ID, SOURCE_VERB, " +
                "TARGET_VERB, HIRST_ST_ONGE, JIAN_CONRATH, LEACOCK_CHODOROW, LESK, LIN, _PATH, RESNIK, WU_PALMER)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, verbPair.getId());
            ps.setInt(2, verbPair.getSentencePairID());
            ps.setString(3, verbPair.getSourceVerb());
            ps.setString(4, verbPair.getTargetVerb());
            ps.setDouble(5, verbPair.getHirstStOnge());
            ps.setDouble(6, verbPair.getJiangConrath());
            ps.setDouble(7, verbPair.getLeacockChodorow());
            ps.setDouble(8, verbPair.getLesk());
            ps.setDouble(9, verbPair.getLin());
            ps.setDouble(10, verbPair.getPath());
            ps.setDouble(11, verbPair.getResnik());
            ps.setDouble(12, verbPair.getWuPalmer());

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

    public static ArrayList<VerbPairWithAllScores> getAllAnnotatedVerbPairs() {
        Connection conn = DBCon.getConnection();
        ArrayList<VerbPairWithAllScores> verbPairsWithScores = new ArrayList<>();
        ResultSet resultSet;
        String query = "SELECT * FROM " + VerbPairWithAllScores.TABLE_NAME;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int sentencePairID = resultSet.getInt("SENTENCE_PAIR_ID");
                String sourceVerb = resultSet.getString("SOURCE_VERB");
                String targetVerb = resultSet.getString("TARGET_VERB");
                int annotation = AnnotatedVerbPairsController.getAnnotationOfVerbPair(id);
                System.out.printf("%d: %d", id, annotation);
                double[] allScores = {
                        resultSet.getDouble(Constants.HIRST_ST_ONGE),
                        resultSet.getDouble(Constants.JIAN_CONRATH),
                        resultSet.getDouble(Constants.LEACOCK_CHODOROW),
                        resultSet.getDouble(Constants.LESK),
                        resultSet.getDouble(Constants.LIN),
                        resultSet.getDouble(Constants.PATH),
                        resultSet.getDouble(Constants.RESNIK),
                        resultSet.getDouble(Constants.WU_PALMER),
                };

                AnnotatedVerbPair avp = new AnnotatedVerbPair(id, sentencePairID, sourceVerb, targetVerb, annotation);

                VerbPairWithAllScores vp = new VerbPairWithAllScores(avp, allScores);
                verbPairsWithScores.add(vp);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return verbPairsWithScores;
    }
}
