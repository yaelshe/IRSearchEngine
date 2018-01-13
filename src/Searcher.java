
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class perfroms the search query in the corpus process
 */
public class Searcher {


    Parse p;
    HashMap<String,Term> queryTerms;
    Ranker rank;
    public static Map<String,String> stopwords;
    int sizeofQuery;
    private ArrayList<Query> Querys;
    private Pattern quertcutFirst;
    public static ArrayList<String> allResults;
    public static ArrayList<String> docsToDisplay;
    public static String pathToPosting;

    //TODO need to change the path to a file inside the project

    /**
     * this is the constructor get the query and perform parse on it to insert to queryTerms
     * @param query- the string received from the user
     * @param stemming to perform stemming or not

     */
    public Searcher(String query,boolean stemming) {
        createMapStopWords();
        allResults=new ArrayList<>();
        if(!stemming)
            pathToPosting=GuiPartB.pathToLoad+"\\finalPosting.txt";
        else
            pathToPosting=GuiPartB.pathToLoad+"\\finalPostingWithStem.txt";
        p= new Parse(stopwords, stemming);//stemming instead of true
        p.parseDoc(query,true);
        queryTerms=  new HashMap<>(p.m_terms);
        sizeofQuery=queryTerms.size();
        rank = new Ranker(queryTerms);
        String newLine = System.getProperty("line.separator");
        for(String docId: rank.docsToReturn) {
            docId = docId.replaceAll(" ", "");
            String towrite =222 + " 0 " + docId + " 1 1 mt" + newLine;
            allResults.add(towrite);
        }
    }
    public Searcher(boolean stemming,String pathToQueryFile) throws IOException {
        createMapStopWords();
        if(!stemming)
            pathToPosting=GuiPartB.pathToLoad+"\\finalPosting.txt";
        else
            pathToPosting=GuiPartB.pathToLoad+"\\finalPostingWithStem.txt";
        docsToDisplay=new ArrayList<>();
        allResults=new ArrayList<>();
        p= new Parse(stopwords, stemming);
        Querys= new ArrayList<>();
        quertcutFirst=Pattern.compile("<num>(?s)(.+?)Narrative:");
        String newLine = System.getProperty("line.separator");
        try {
            breakToQuerysFile(pathToQueryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!Querys.isEmpty())
        {
            for(Query que: Querys)
            {
                p.m_terms.clear();
                p.parseDoc(que.getQueryText(),true);
                queryTerms=  new HashMap<>(p.m_terms);
                sizeofQuery=queryTerms.size();
                //rank.docsToReturn.clear();
                rank = new Ranker(queryTerms);
                docsToDisplay.add("*****Query number: "+que.getQueryID()+"******");
                docsToDisplay.add("***We found "+rank.docsToReturn.size()+ " relevant documents***");
                for(String docId: rank.docsToReturn) {
                    docId = docId.replaceAll(" ", "");
                    String towrite = que.getQueryID() + " 0 " + docId + " 1 1 mt" + newLine;
                    docsToDisplay.add(docId);
                    allResults.add(towrite);
                }
            }
        }
    }
    /**
     * this method compute the weight of each term in the query
     * @return the weight of the term in the query
     */
    private double computeWiQ(String term)
    {

        return 0.0;
    }

    /**
     * this method compute the weight of the term in a document
     * @return
     */
    private double computeWiD(String term, String doc)
    {

        return 0.0;
    }

    /**
     * this method initiate the process to build the stopwords list
     */
    public static void createMapStopWords()
    {
        String pathofstopword=GuiPartB.pathToLoad+"\\stop_words.txt";
        //TODO CHANGE PATH TO STOPWORDS
        String []stops=(readStopword(pathofstopword));
        stopwords = new HashMap<>();
        for(int i=0;i<stops.length;i++)
        {
            stopwords.put(stops[i],"");
        }
    }

    /**
     * this method return an array of the stop word from file path S
     * @param S - the path to the stop word file
     * @return - array of stop word
     */
    private  static String [] readStopword(String S){
        //make a string Array from all the StopWords
        String everything="";
        try {
            try(BufferedReader br = new BufferedReader(new FileReader(S))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("path to stopwords not good");
        }
        String []stopwords=everything.split("\\s+");
        return  stopwords;
    }

    /**
     * This method receve path to file with queries and read all of the text as a string send to another function and intiaize
     * Querys which is an array for all of the queries from the file
     * @param path - path to queries file
     * @throws IOException
     */
    private void breakToQuerysFile(String path) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        char[] buf = new char[1024];
        int numRead=0;
        try {
            while((numRead=reader.read(buf)) != -1){
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
        Querys=getQuerysFromText(fileData.toString());
    }

    /**
     * this method takes from the text file of queries the queies itself and their number
     * @param text- the text of the file with the queries
     * @return - array of queries
     */
    private ArrayList<Query> getQuerysFromText(String text)
    {
        ArrayList <String> allMatchesofQuery ;
        ArrayList<Query> a=new ArrayList<>();
        String queryiD,query,queryDesc;
        //String regex = "<title>(?s)(.+?)<desc>";
        allMatchesofQuery = new ArrayList <>();
        Matcher m = quertcutFirst.matcher(text);
        while (m.find()) {
            allMatchesofQuery.add(m.group(1));
        }
        for(String str: allMatchesofQuery)
        {
             queryiD=str.substring(str.indexOf("Number:")+8,str.indexOf("<title>")).replaceAll("\n","").trim();
             query=str.substring(str.indexOf("<title>")+8,str.indexOf("<desc>"));
             queryDesc=str.substring(str.indexOf("Description:")+12,str.indexOf("<narr>"));
             Query q= new Query(queryiD,query,queryDesc);
             a.add(q);
        }
        return a;
    }

}
