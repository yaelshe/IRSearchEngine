/**
        * This class is for words that belongs to the cache and we want to portray them in the gui
        */
public class CacheTermGui {
    String term;
    String docsApper;


    /**
     * this constructor for the term in the cache
     * @param term the word
     * @param favsdocss the 2 documents that the word apperas the most in them and the number of apperances in each one
     *                  {doc345- 34, doc346- 65} for example
     */
    public CacheTermGui(String term,String favsdocss) {

        this.term = term;
        this.docsApper=favsdocss;
    }

    /**
     * get method for the term as a String
     * @return the String of the word
     */
    public String getTerm() {
        return term;
    }

    /**
     *set method to alter the string od the term
     * @param term- the string to change the term to
     */
    public void setTerm(String term) {
        this.term = term;
    }
}
