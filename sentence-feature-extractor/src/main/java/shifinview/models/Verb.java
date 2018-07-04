package shifinview.models;

public class Verb {
    private String relation;
    private String depWord;
    private String depLemma;
    private String depTag;
    private String govWord;
    private String govLemma;
    private String govTag;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDepWord() {
        return depWord;
    }

    public void setDepWord(String depWord) {
        this.depWord = depWord;
    }

    public String getDepLemma() {
        return depLemma;
    }

    public void setDepLemma(String depLemma) {
        this.depLemma = depLemma;
    }

    public String getDepTag() {
        return depTag;
    }

    public void setDepTag(String depTag) {
        this.depTag = depTag;
    }

    public String getGovWord() {
        return govWord;
    }

    public void setGovWord(String govWord) {
        this.govWord = govWord;
    }

    public String getGovLemma() {
        return govLemma;
    }

    public void setGovLemma(String govLemma) {
        this.govLemma = govLemma;
    }

    public String getGovTag() {
        return govTag;
    }

    public void setGovTag(String govTag) {
        this.govTag = govTag;
    }
}
