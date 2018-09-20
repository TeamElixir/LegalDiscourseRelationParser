package resultsAnalysinng;

import utils.SQLiteUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class resultShiftInView {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT SSID,TSID, ID FROM FEATURE_ENTRY_LEGAL_SENTENCE where ID in (SELECT RELATIONSHIP_ID FROM SHIFT_IN_VIEW where LIN_SHIFT = 2);";
        int count = 0;

        // executes sql and fills up the array list
        SQLiteUtils sqLiteUtils = new SQLiteUtils();
        ResultSet resultSet = sqLiteUtils.executeQuery(sql);
        ArrayList<String> sentences = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println("IDs :" + resultSet.getString("ID"));
            count++;
            //sentences.add(resultSet.getString("SENTENCE"));
        }
        System.out.println(count);

    }

}
