package shiftinview.verbsSemanticSimilarity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // db parameters
            // TODO: Change to a generic path
            String pathToDB = "/media/menuka/Data/github.com/TeamElixir/LegalDiscourseRelationParser/extractorDatabase.db";
            String url = "jdbc:sqlite:" + pathToDB;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
