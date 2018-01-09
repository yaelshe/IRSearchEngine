
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
    String pathToDocFile="D:\\DOCS.txt";
    public static Map<String,TermDic> m_Dictionary;
    int sizeofQuery;
    private ArrayList<Query> Querys;
    private Pattern queryCut;//<title><desc>
    private Pattern quertcutFirst;
    BufferedWriter writerDocQuerys;
    File docFile;

    //TODO need to change the path to a file inside the project

    /**
     * this is the constructor get the query and perform parse on it to insert to queryTerms
     * @param query- the string received from the user
     * @param stemming to perform stemming or not

     */
    public Searcher(String query,boolean stemming) {
        createMapStopWords();
        p= new Parse(stopwords, stemming);//stemming instead of true
        p.parseDoc(query,true);
        queryTerms=  new HashMap<>(p.m_terms);
        sizeofQuery=queryTerms.size();
        rank = new Ranker(queryTerms);
    }
    public Searcher(boolean stemming,String pathToQueryFile) throws IOException {
        createMapStopWords();
        p= new Parse(stopwords, stemming);
        Querys= new ArrayList<>();
        queryTerms=  new HashMap<>(p.m_terms);
        sizeofQuery=queryTerms.size();
        queryCut=Pattern.compile("<title>(?s)(.+?)<desc>");
        quertcutFirst=Pattern.compile("<num>(?s)(.+?)Narrative:");
         docFile=new File("D:\\results.txt");
        writerDocQuerys= new BufferedWriter(new FileWriter(docFile));
        // in case we get a file of more then one query
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
                rank = new Ranker(queryTerms);
                try {
                    writeToFile(que.getQueryID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //rank.printResults();
            }
            try {
                writerDocQuerys.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeToFile(String id) throws IOException {

        String newLine = System.getProperty("line.separator");
        for(String docId: rank.docsToReturn)
        {
            docId=docId.replaceAll(" ","");
            String towrite=id+" 0 "+docId+" 1 1 mt"+newLine;
            writerDocQuerys.write(towrite);
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
     * this
     */
    public static void createMapStopWords()
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
             queryiD=str.substring(str.indexOf("Number:")+8,str.indexOf("<title>"));
             query=str.substring(str.indexOf("<title>")+8,str.indexOf("<desc>"));
             queryDesc=str.substring(str.indexOf("Description:")+12,str.indexOf("<narr>"));
             Query q= new Query(queryiD,query,queryDesc);
             a.add(q);
        }
        return a;
    }

}
