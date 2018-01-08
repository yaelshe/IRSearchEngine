

import java.util.Map;
import java.util.*;

/**
 * This class  creates an object for a Term from the corpus' documents
 */
public class Term
{
    public String _term;
    public int numOfDocIDF;//amount of docs the term appear in
    public Map<String,Integer> docs;//list for the numbers of the
    // documents the word is in and number of appearances in each
    private int totalApperance;
    public int pointer;
    public String postingline;

    /**
     * this constructor builds the term object
     * @param term - the word
     * @param docs- a Map to save all of the documents the term appeared in and an Integer to count the number of times in each document
     */
    public Term( String term, Map<String, Integer> docs) {
        _term=term;
        this.docs = new HashMap<>(docs);
        this.numOfDocIDF =1;
        totalApperance=1;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public String getPostingline() {
        return postingline;
    }

    public void setPostingline(String postingline) {
        this.postingline = postingline;
    }

    public int getPointer() {

        return pointer;
    }

    public Term(String str, int df, int pointer,String line)
    {
        _term=str;
        numOfDocIDF=df;
        this.pointer=pointer;
        this.postingline=line;
    }

    /**
     * a toString method to show all of the Term details in a String
     * @return the string of the term.
     * for example: term #numberofDocs &docname-number docname-number....
     */
    @Override
    public String toString()
    {//term #numberofDocs &docname-number docname-number....
        String termStr="";
        termStr=this._term+" ";
        termStr=termStr+"#"+this.getnumOfDocIDFString()+" ";
        termStr=termStr+"&"+this.get_docs();
        termStr=termStr+"["+this.totalApperance+"]";
        return termStr;
    }

    public String get_term() {
        return _term;
    }

    /**
     * this method return a string with the number of docs the word appeared in
     * @return
     */
    public String getnumOfDocIDFString()
    {
        // String num="";
        //List sortedKeys=new ArrayList(docs.values());
        //Collections.sort(sortedKeys);
        String num=Integer.toString(numOfDocIDF);
        return num;
    }

    public int getNumOfDocIDF() {
        return numOfDocIDF;
    }

    public int getTotalApperance() {
        return totalApperance;
    }

    /**
     * this method takes the Map that saves the documents the term appeared in and put in  as string
     * @return the string ofd  documents id and number of appearances of the word
     */
    public String get_docs(){
        String str="";
        for (String docnum: docs.keySet())
        {
            String key =docnum;
            String value =docs.get(docnum).toString();
            str=str+"{"+key + ":" + value+"} ";
            //System.out.print(key + "-" + value+" ");
        }

        return str;
    }

    public void setTotalApperance(int totalApperance) {
        this.totalApperance = this.totalApperance+totalApperance;
    }
    public void setNumOfDocIDF(int num){
        this.numOfDocIDF=this.numOfDocIDF+num;
    }
    public void addToDocs(String docnum)
    {
        if(docs.containsKey(docnum))
        {
            //docs.put(currDoc, m_terms.get(str).docs.get(currDoc) + 1);
        }
        else{

        }
    }
}
