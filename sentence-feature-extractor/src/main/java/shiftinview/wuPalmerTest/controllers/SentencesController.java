package shiftinview.wuPalmerTest.controllers;

import shiftinview.wuPalmerTest.DBCon;
import shiftinview.wuPalmerTest.models.Sentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SentencesController {
    public static Sentence getSentenceById(int id) {
        Connection conn = DBCon.getConnection();
        ResultSet resultSet;
        String query = "SELECT * FROM " + Sentence.TABLE_NAME + " WHERE ID=" + id;
        Sentence s = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String file = resultSet.getString("CASE_FILE");
                String rawSentence = resultSet.getString("SENTENCE");

                s = new Sentence(id, file, rawSentence);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static ArrayList<Sentence> getAllSentences() {
        Connection conn = DBCon.getConnection();
        ArrayList<Sentence> sentences = new ArrayList<>();
        ResultSet resultSet;
        String query = "SELECT * FROM " + Sentence.TABLE_NAME;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String file = resultSet.getString("CASE_FILE");
                String rawSentence = resultSet.getString("SENTENCE");

                Sentence s = new Sentence(id, file, rawSentence);

                sentences.add(s);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sentences;
    }
}
