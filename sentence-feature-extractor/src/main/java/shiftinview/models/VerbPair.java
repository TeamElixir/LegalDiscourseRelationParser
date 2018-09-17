package shiftinview.models;

public class VerbPair {
    private String sourceVerb;
    private String targetVerb;
    private Boolean sourceVerbNegated=false;
    private Boolean targetVerbNegated=false;

    public String getSourceVerb() {
        return sourceVerb;
    }

    public void setSourceVerb(String sourceVerb) {
        this.sourceVerb = sourceVerb;
    }

    public String getTargetVerb() {
        return targetVerb;
    }

    public void setTargetVerb(String targetVerb) {
        this.targetVerb = targetVerb;
    }

    public Boolean getSourceVerbNegated() {
        return sourceVerbNegated;
    }

    public void setSourceVerbNegated(Boolean sourceVerbNegated) {
        this.sourceVerbNegated = sourceVerbNegated;
    }

    public Boolean getTargetVerbNegated() {
        return targetVerbNegated;
    }

    public void setTargetVerbNegated(Boolean targetVerbNegated) {
        this.targetVerbNegated = targetVerbNegated;
    }
}
