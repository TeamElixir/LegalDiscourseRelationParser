package shiftinview.wuPalmerTest.models;

public class VerbPairWithAllScores extends AnnotatedVerbPair {
    public static final String TABLE_NAME = "VERB_PAIR_WITH_ALL_SCORES";
    private double hirstStOnge, jiangConrath, leacockChodorow, lesk, lin, path, resnik, wuPalmer;

    public VerbPairWithAllScores(AnnotatedVerbPair avp, double[] allScores) {
        super(avp.getId(), avp.getSentencePairID(), avp.getSourceVerb(), avp.getTargetVerb(), avp.getAnnotation());
        this.hirstStOnge = allScores[0];
        this.jiangConrath = allScores[1];
        this.leacockChodorow = allScores[2];
        this.lesk = allScores[3];
        this.lin = allScores[4];
        this.path = allScores[5];
        this.resnik = allScores[6];
        this.wuPalmer = allScores[7];
    }

    public double getHirstStOnge() {
        return hirstStOnge;
    }

    public double getJiangConrath() {
        return jiangConrath;
    }

    public double getLeacockChodorow() {
        return leacockChodorow;
    }

    public double getLesk() {
        return lesk;
    }

    public double getLin() {
        return lin;
    }

    public double getPath() {
        return path;
    }

    public double getResnik() {
        return resnik;
    }

    public double getWuPalmer() {
        return wuPalmer;
    }

    @Override
    public String toString() {
        return super.toString() + "\nVerbPairWithAllScores{" +
                "hirstStOnge=" + hirstStOnge +
                ", jiangConrath=" + jiangConrath +
                ", leacockChodorow=" + leacockChodorow +
                ", lesk=" + lesk +
                ", lin=" + lin +
                ", path=" + path +
                ", resnik=" + resnik +
                ", wuPalmer=" + wuPalmer +
                '}';
    }
}
