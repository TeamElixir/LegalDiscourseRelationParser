package treegenerator;

import java.util.ArrayList;

public class NodeModel {
    public String subject;
    public int id;
    public ArrayList<SentenceModel> sentences=new ArrayList<>();
    NodeModel(String subject){
        this.subject=subject;
    }
}
