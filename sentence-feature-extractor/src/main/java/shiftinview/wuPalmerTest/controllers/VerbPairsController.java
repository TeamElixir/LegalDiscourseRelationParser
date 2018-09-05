package shiftinview.wuPalmerTest.controllers;

import shiftinview.wuPalmerTest.DBCon;
import shiftinview.wuPalmerTest.models.VerbPair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class VerbPairsController {

    public static boolean insertVerbPairToDB(ArrayList<VerbPair> verbPairs) {
        Connection conn = DBCon.getConnection();

        // insert verbPairs. one by one
        for (VerbPair verbPair : verbPairs) {
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
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;

    }
}
