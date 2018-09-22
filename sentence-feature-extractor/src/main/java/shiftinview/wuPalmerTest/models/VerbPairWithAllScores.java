package shiftinview.wuPalmerTest.models;

public class VerbPairWithAllScores extends AnnotatedVerbPair {
    public static final String TABLE_NAME = "VERB_PAIR_WITH_ALL_SCORES";
    private double hirstStOnge, jiangConrath, leacockChodorow, lesk, lin, path, resnik, wuPalmer;

    public VerbPairWithAllScores(AnnotatedVerbPair avp,
                                 // arranged in the alphabetical order. do not change.
                                 double hirstStOnge,
                                 double jiangConrath,
                                 double leacockChodorow,
                                 double lesk,
                                 double lin,
                                 double path,
                                 double resnik,
                                 double wuPalmer
    ) {
        super(avp.getId(), avp.getSentencePairID(), avp.getSourceVerb(), avp.getTargetVerb(), avp.getAnnotation());
        this.hirstStOnge = hirstStOnge;
        this.jiangConrath = jiangConrath;
        this.leacockChodorow = leacockChodorow;
        this.lesk = lesk;
        this.lin = lin;
        this.path = path;
        this.resnik = resnik;
        this.wuPalmer = wuPalmer;
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
        String superToString = super.toString();
        return superToString + "\nVerbPairWithAllScores{" +
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
