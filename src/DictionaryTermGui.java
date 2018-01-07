

/**
 * This class is for words that belongs to the Dictionary and we want to portray them in the gui
 */
public class DictionaryTermGui {
    private String term;
    private String amount;

    /**
     * this constructor for the term in the Dictionary
     * @param name- string term
     * @param quantity - number of appearances of word in all of the corpus
     */
    public DictionaryTermGui(String name, String quantity){
        term=name;
        amount=quantity;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm()
    {
        return term;
    }
}
