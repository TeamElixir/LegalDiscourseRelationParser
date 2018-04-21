import models.FeatureEntryDB;

import java.sql.SQLException;
import java.util.ArrayList;

public class DiscourseModel {
    int numberOfFeatures = 16;
    public static void main(String[] args) {
        FeatureEntryDB featureEntryDB = new FeatureEntryDB();
        try {
            ArrayList<FeatureEntryDB> featureEntryDBS = featureEntryDB.getAll();

            System.out.println("readFeatureEntryDBs");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
