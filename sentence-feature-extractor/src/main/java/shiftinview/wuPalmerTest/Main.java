package shiftinview.wuPalmerTest;

import shiftinview.wuPalmerTest.controllers.SentencePairsController;
import shiftinview.wuPalmerTest.models.SentencePair;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<SentencePair> allSentencePairs = SentencePairsController.getAllSentencePairs();
    }
}
