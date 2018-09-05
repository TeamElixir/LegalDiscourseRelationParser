package shiftinview.wuPalmerTest.models;

public class Sentence {

    public static final String TABLE_NAME = "LEGAL_SENTENCE";

    private int id;

    private String sentence;

    private String file;

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
}
