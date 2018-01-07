

import java.io.Serializable;

/**
 * This class creates an object for a Term to save in the Cache from the corpus' documents
 */
public class TermCache implements Serializable {
    String term;
    String favDocs;
    int pointer;

    /**
     * the constructor
     * @param term- the strings's word
     * @param favDocs the 2 documents and number of appearnces maximum
     * @param pointer a pointer to the term in the posting
     */
    public TermCache(String term, String favDocs, int pointer) {
        this.term = term;
        this.favDocs = favDocs;
        this.pointer = pointer;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getFavDocs() {
        return favDocs;
    }

    public void setFavDocs(String favDocs) {
        this.favDocs = favDocs;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }


}
