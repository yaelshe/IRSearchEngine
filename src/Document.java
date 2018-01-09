
import java.io.Serializable;
import java.util.*;
import java.io.BufferedReader;
import java.lang.*;
import java.io.InputStreamReader;

/**
 * This class creates an object for a Document from the corpus
 */
public class Document implements Serializable
{
    public String text;
    public int max_tf;//number of appearances most frequent term
    private int docLength;
    public String mostCommWord;
    public final String id ;
    public double docWeight;
    public String directoryPathDoc;//Todo need to add through readFile
    private static final long serialVersionUID = -4985806624272702150L;


    /**
     * this is the constructor that initialize the fields for the document object.
     * @param id- name of document
     * @param text- text of the document
     * @param max_tf- number of appearances most ferquent term
     * @param mostCommWord- the most common string in the text
     * @param pathDoc - the path to the document = the name of the folder it is in
     */
    public Document(String id,String text, int max_tf, String mostCommWord,String pathDoc) {
        this.text = text;
        this.max_tf = max_tf;
        this.docLength =0;
        this.mostCommWord = mostCommWord;
        this.id=id;
        docWeight=0;
        directoryPathDoc=pathDoc;
    }

    public String getDirectoryPathDoc() {
        return directoryPathDoc;
    }

    public void setDirectoryPathDoc(String directoryPathDoc) {
        this.directoryPathDoc = directoryPathDoc;
    }

    public void setDocLength(int docLength) {
        this.docLength = docLength;
    }

    public double getDocWeight() {
        return docWeight;
    }

    public void setDocWeight(double docWeight) {
        this.docWeight = this.docWeight+docWeight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMax_tf() {
        return max_tf;
    }

    public void setMax_tf(int max_tf) {
        this.max_tf = max_tf;
    }

    public int getDocLength() {
        return docLength;
    }

    public String getMostCommWord() {
        return mostCommWord;
    }

    public void setMostCommWord(String mostCommWord) {
        this.mostCommWord = mostCommWord;
    }

    public String getId() {
        return id;
    }
}
