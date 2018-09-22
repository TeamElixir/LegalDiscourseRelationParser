package shiftinview.wuPalmerTest.controllers;

import shiftinview.wuPalmerTest.DBCon;
import shiftinview.wuPalmerTest.models.VerbPairWithAllScores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
