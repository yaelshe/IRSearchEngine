import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankerSentence {
    public HashMap<String,TermDic> AllTerms;//**
    public HashMap<Integer,HashMap<String,TermDic>> AllSentence;
    public int [] SentenceLength;
    public double[] SentenceWeight;
    private int SenId;
    private double weight;
    private int N;
    private int Fij=0;
    private int Dj=0;
    private int DFi=0;
    private double TFij=0;
    private double IDF=0;
    /**
     * this method compute the wij in mone for word in query and specific document
     * @param allterms the terms in all text
     * @param allsentence map for maps for the term in the sentence
     * @return
     */
    public RankerSentence(HashMap allsentence,HashMap allterms,int[] lengthofsentence) {
        this.AllSentence=new HashMap<>(allsentence);
        this.AllTerms=new HashMap<>(allterms);
        this.SentenceLength=lengthofsentence;
        this.SentenceWeight=new double[lengthofsentence.length];
        this.weight=0;
        this.N=allsentence.size();
        RankTheSentence();
    }

    private void RankTheSentence() {
        for (HashMap sen: AllSentence.values())
        {

            HashMap <String,TermDic> termsen=sen;
            for (TermDic term: termsen.values())
            {
                SenId=term.pointer;
                Dj=SentenceLength[SenId-1];
                Fij=term.apperances;
                DFi=AllTerms.get(term.name).numOfDocs;
                IDF=Math.log((((double)N)/DFi)) / Math.log(2);
                TFij=((double)Fij)/Dj;
                this.weight=this.weight+(IDF*TFij);
            }
            SentenceWeight[SenId-1]=weight;
            weight=0;
        }

    }

    public List TopFive(){
        List<Integer> top5= new ArrayList<Integer>();

        for(int j=0;j<5&&j<SentenceWeight.length;j++) {
            double max=0;
            int idmax=-1;
            for (int i = 0; i < SentenceWeight.length; i++) {
                if (!top5.contains(i + 1)) {
                    if (SentenceWeight[i] > max) {
                        max = SentenceWeight[i];
                        idmax = i + 1;
                    }
                }
            }
            top5.add(idmax);
        }
        return top5;

    }


}