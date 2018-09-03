package shiftinview.wuPalmerTest.models;

public class Relation {
    private static final String[] relations = {
            "No Relation",
            "Identity",
            "Equivalence (Paraphrase)",
            "Translation",
            "Subusmption",
            "Contradiction",
            "Historical Background",
            "Citation",
            "Modality",
            "Attribution",
            "Summary",
            "Follow-up",
            "Indirect speech",
            "Elaboration (Refinement)",
            "Fulfillment",
            "Description",
            "Reader Profile",
            "Change of Perspective",
            "Overlap (partial equivalence)"

    };

    public static String getRelation(int relation) {
        return relations[relation];
    }
}
