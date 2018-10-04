package shiftinview.verbsSemanticSimilarity.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import shiftinview.models.ShiftInViewPair;
import utils.SQLiteUtils;

public class Sentence {

    public static final String TABLE_NAME = "LEGAL_SENTENCE";

    private int id;

    private String sentence;

    private String file;

	private static SQLiteUtils sqLiteUtils;

	static {
		try {
			sqLiteUtils = new SQLiteUtils();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public Sentence(String sentence) {
        this.sentence = sentence;
    }

    public Sentence(int id, String file, String sentence) {
        this.id = id;
        this.sentence = sentence;
        this.file = file;
    }

    public Sentence(String file, String sentence) {
        this.sentence = sentence;
        this.file = file;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + id +
                ", sentence='" + sentence + '\'' +
                ", file='" + file + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getSentence() {
        return sentence;
    }

    public String getFile() {
        return file;
    }

    public static Sentence getSentenceDB(int id) throws Exception{
	    String sql = "SELECT * FROM " + Sentence.TABLE_NAME + " WHERE ID=" + id + ";";
//	    System.out.println(sql);
	    ResultSet resultSet = sqLiteUtils.executeQuery(sql);
	    Sentence sentence = null;

	    if (resultSet.isClosed()) {
		    return null;
	    }

	    ArrayList<ShiftInViewPair> pairs = new ArrayList<>();
	    ShiftInViewPair pair;
	    while (resultSet.next()) {
		    String file = resultSet.getString("CASE_FILE");
		    String rawSentence = resultSet.getString("SENTENCE");

		    sentence = new Sentence(id, file, rawSentence);
	    }

	    return sentence;
    }
}
