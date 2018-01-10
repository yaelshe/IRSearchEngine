/**
 * This class is for saving the query indormmation such as name description and the query itself
 */
public class Query
{
    private String queryID;
    private String queryText;
    private String queryDesc;

    /**
     * The constructor of the Query class
     * @param queryID - number of qurty
     * @param queryText -the query in a string
     * @param queryDesc - the description in a string
     */
    public Query(String queryID, String queryText, String queryDesc) {
        this.queryID = queryID;
        this.queryText = queryText;
        this.queryDesc = queryDesc;
    }

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryDesc() {
        return queryDesc;
    }

    public void setQueryDesc(String queryDesc) {
        this.queryDesc = queryDesc;
    }
}
