
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    String pathToDocFile="D:\\DOCS.txt";
    public static Map<String,TermDic> m_Dictionary;
    int sizeofQuery;
    private ArrayList<String> Querys;
    private Pattern queryCut;//<title><desc>
    //TODO need to change the path to a file inside the project

    /**
     * this is the constructor get the query and perform parse on it to insert to queryTerms
     * @param query- the string received from the user
     * @param stemming to perform stemming or not
     * @param expand to perform expansion to the query or not
     * @param isDoc to perform doc analyze or not
     */
    public Searcher(String query,boolean stemming) {
        createMapStopWords();
        p= new Parse(stopwords, stemming);//stemming instead of true
        p.parseDoc(query,true);
        queryTerms=  new HashMap<>(p.m_terms);
        sizeofQuery=queryTerms.size();
        rank = new Ranker(queryTerms);
    }
    public Searcher(String query,boolean stemming,String pathToQueryFile) {
        createMapStopWords();
        p= new Parse(stopwords, stemming);//stemming instead of true
        p.parseDoc(query,true);
        queryTerms=  new HashMap<>(p.m_terms);
        sizeofQuery=queryTerms.size();
        Querys= new ArrayList<>();

        if(pathToQueryFile!=null)
        {
            queryCut=Pattern.compile("<title>(?s)(.+?)<desc>");
            // in case we get a file of more then one query
            try {
                breakToQuerysFile(pathToQueryFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!Querys.isEmpty())
            {
                for(String que: Querys)
                {
                    p.parseDoc(que,true);
                    queryTerms=  new HashMap<>(p.m_terms);
                    sizeofQuery=queryTerms.size();
                    rank = new Ranker(queryTerms);
                    //rank.printResults();
                }
            }
        }
    }
    private void Search(HashMap<String,Term> queryWords)
    {

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
     * this
     */
    private void createMapStopWords()
    {
        String pathofstopword="C:\\Users\\sheinbey\\Downloads\\stop_words.txt";
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
    private  String [] readStopword(String S){
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
    private ArrayList<String> getQuerysFromText(String text)
    {
        ArrayList <String> allMatchesofQuery ;
        //String regex = "<title>(?s)(.+?)<desc>";
        allMatchesofQuery = new ArrayList <>();
        Matcher m = queryCut.matcher(text);
        while (m.find()) {
            allMatchesofQuery.add(m.group(1).toString().trim());
        }
        return allMatchesofQuery;
    }

}
