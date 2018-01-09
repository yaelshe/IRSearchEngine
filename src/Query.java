public class Query {


    private String queryID;
    private String queryText;
    private String queryDesc;

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
