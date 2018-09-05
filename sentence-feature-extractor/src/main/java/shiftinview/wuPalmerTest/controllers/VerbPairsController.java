package shiftinview.wuPalmerTest.controllers;

import shiftinview.wuPalmerTest.DBCon;
import shiftinview.wuPalmerTest.models.VerbPair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
