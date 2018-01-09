
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.StrictMath.log;
import static java.lang.StrictMath.sqrt;

public class Ranker {
    private String loadDocPosting="";//path to loadDocPosting
    private String loadDoclengths="";//path to loadDocPosting
    //public static HashMap<String,Document> docPosting;
    private HashMap<String,Term> rankQueryTerms;
    public static String pathToPosting="C:\\Users\\sheinbey\\Downloads\\finalPosting.txt";//todo add path to posting
    public static final int N=472525;//468370
    public static int avgDoc=70;
    public static final double k=1.4;
    public static final double b=0.75;
    public HashMap<String,Double> docsTermQuery;// save all the documents that are relevant to the terms
                                                        //in the query and the rank for each after computing it
    public static LinkedList<String> docsToReturn;

    public Ranker(HashMap<String,Term> queryTerms) {
        System.out.println(Parse.docPosting.size()+ "size of docposting");
        System.out.println(Indexer.m_Dictionary.size()+ "size of dictionary");
        System.out.println(Indexer.m_Cache.size()+ "size of cache");
        docsToReturn= new LinkedList<>();

       /* try {
            loadFiles(loadDocPosting);// load the docPosting
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        updateInfoQuery(queryTerms);

        docsTermQuery= new HashMap<>();
        breakToDocsOnlyQuery();
        rankAllDocument();
        try {
            returnDocs();//todo need to send back
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public LinkedList<String> returnDocs() throws IOException {
        if(!docsTermQuery.isEmpty())
        {
            PriorityQueue<Map.Entry<String,Double>> pq = new PriorityQueue<>((o1, o2) ->Double.compare(o2.getValue(), o1.getValue()));
            for (Map.Entry<String,Double> d:docsTermQuery.entrySet()){
                pq.add(d);
            }
            for(int m=0;m<50&&m<pq.size();m++){
                docsToReturn.add(pq.poll().getKey());
                //pq.poll();
            }
            /*List<String> sortedTerms=new ArrayList(docsTermQuery.values());
            Collections.sort(sortedTerms);
            for(int m=0;m<50;m++)
            {
                docsToReturn.put(sortedTerms.get(sortedTerms.size()-1-m),docsTermQuery.get(sortedTerms.get(sortedTerms.size()-1-m)));
            }
            */
            System.out.println(docsToReturn);
            File docFile=new File("D:\\results.txt");
            BufferedWriter writerDoc= new BufferedWriter(new FileWriter(docFile));
            String newLine = System.getProperty("line.separator");
            for(String docId: docsToReturn)
            {
                docId=docId.replaceAll(" ","");
                System.out.println("*"+docId+"*");
                String towrite="351 0 "+docId+" 1 1 mt"+newLine;
                writerDoc.write(towrite);
            }
            writerDoc.close();
            return docsToReturn;
        }
        System.out.println("no docs to return");
        return null;
    }
    private void updateInfoQuery(HashMap<String,Term> words)
    {
        rankQueryTerms=new HashMap<String,Term>();
        for( String str: words.keySet())
        {
            TermDic t= Indexer.m_Dictionary.get(str);
            int pointer=t.getPointer();
            int df=t.getNumOfDocs();
            String line=getLineFromPostingFile(pointer);
            Term term= new Term(str,df,pointer,line);
            rankQueryTerms.put(str,term);
        }
    }
   private void rankAllDocument()
   {
       for(String docId: docsTermQuery.keySet())
       {
           docsTermQuery.put(docId,cosSim(docId)+computeBM25total(1,1,docId));//Todo add more to the cosSim formula change 1 to k and b
       }
   }
    private double cosSim(String doc)
    {
        double docWeight=sqrt(Parse.docPosting.get(doc).getDocWeight());//TODO *SQRT Wiq
        double mone= sumWijMone(doc);//*weight of terms in query; todo
        if (docWeight!=0)
            return mone/docWeight;
        else
        {
            System.out.println("mehcane is zero Ranker cosSim function");
            return 0;
        }
    }
    public void loadFiles(String path) throws IOException, ClassNotFoundException {
        FileInputStream fi;
        try {
            fi = new FileInputStream(new File(path + "\\docPosting.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            Parse.docPosting=((HashMap<String,Document>) oi.readObject()) ;
        }
        catch(Exception e)
        {
            System.out.println("not load ranker row 31");
        }


    }
    public static String getLineFromPostingFile(int lineNumber){
        try (Stream<String> lines = Files.lines(Paths.get(pathToPosting))) {
            return lines.skip(lineNumber).findFirst().get();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * this method compute the wij in mone for word in query and specific document
     * @param term string with the term
     * @param doc docID
     * @return
     */
    private double wijQueryWordDoc(String term,String doc)
    {
        double ans=0;
        double df= rankQueryTerms.get(term).getNumOfDocIDF();
        //breakToDocsOnlyQuery(line);
        double idf=Math.log(((double)N/df)) / Math.log(2);
        double lengthDoc= Parse.docPosting.get(doc).getDocLength();
        ans=(getFijFromLine(rankQueryTerms.get(term).getPostingline(),doc)/lengthDoc)*(idf);
        return ans;
    }

    /**
     * this method initialize the list of documents that are relevant to the terms from the query
     * docsTermQuery save all the docID for all of the documents
     */
    private void breakToDocsOnlyQuery()
    {
        for(String str: rankQueryTerms.keySet())
        {
            docsTermQuery.putAll(breakToDocsId(rankQueryTerms.get(str).getPostingline()));
        }
        //docsTermQuery.containsKey()
    }

    /**
     *this method recieve line from posting for a term and returns a list of all the docs the term is in
     * @param line- line from positng of a term
     * @return list of all the docs the term is in
     */
    private HashMap <String,Double>breakToDocsId(String line)
    {
        HashMap <String,Double> allMatchesofdoc ;
        String regex = "\\{(?s)(.+?)\\:";
        //TODO make the pattern static and compiled once
        allMatchesofdoc = new HashMap <String,Double>();
        Matcher m = Pattern.compile(regex).matcher(line);
        while (m.find()) {
            allMatchesofdoc.put(m.group(1),0.0);
        }
        return allMatchesofdoc;
    }

    /**
     * this method calculate the sum of all wij for words in the Query and document
     * @param doc
     * @return
     */
    private double sumWijMone(String doc)
    {
        double sumWij=0;
        for (String term: rankQueryTerms.keySet()) {
            sumWij=+wijQueryWordDoc(term,doc);
        }
        return sumWij;
    }

    /**
     * this method return the line form file
     * start the counet of line from 0 !!!!!!!!!!!!!
     * @param lineNumber -line number in file (pointer)
     * @param pathToFile - the string path to specific file
     * @return the line from the file of the row by the lineNumber
     */
    public static String getLineFromFile(int lineNumber,String pathToFile)
    {//https://stackoverflow.com/questions/2312756/how-to-read-a-specific-line-using-the-specific-line-number-from-a-file-in-java/38229581
        String lineIWant="";
        FileInputStream fs = null;
        try {
            fs = new FileInputStream("someFile.txt");
            //TODO NEED TO CHANGE IN BRACKETS TO pathToFile
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                if (lineNumber == 0) {
                     lineIWant = br.readLine();
                } else {
                    for (int i = 0; i < lineNumber; ++i)
                        br.readLine();
                     lineIWant = br.readLine();
                }
                br.close();
            }
            catch (IOException ei) {
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lineIWant;
    }

    /**
     * this method get from the posting line the frequency of the term in the doc
     * @param line the posting line of the term
     * @param doc the document that we looking for
     * @return the frequency of the term in the doc
     */
    private double getFijFromLine(String line,String doc)
    {
        double d=0;
        int in=line.indexOf(doc+":");
        String sx=line.substring((line.indexOf(doc+":")+doc.length()+1),line.indexOf('}',line.indexOf(doc+":")));
        try {
            d = Double.parseDouble(sx);
        }
        catch(Exception e){
            return 0;
        }
        return d;
    }
    private double computeIDFbm25(double df)
    {
        double ans=0;
        ans= (double)N-df+0.5;
        ans=ans/(df+0.5);
        ans= log(ans);//TODO CHECK IN WHICH LOG NEED TO BE DONE
        return ans;
    }
    private double computebm25SecondPart(double freqTerm, int docLength)
    {
        double ans=0;
        ans=freqTerm*(k+1);
        ans=ans/(freqTerm+k*(1-b+(b*(avgDoc/(double)docLength))));
        return ans;
    }
    private double computeBM25total(double k,double b,String docId)
    {
        double answer=0;
        for(String term:rankQueryTerms.keySet())
        {
            answer=answer+(computeIDFbm25(rankQueryTerms.get(term).getNumOfDocIDF()))*
                    (computebm25SecondPart(getFijFromLine(rankQueryTerms.get(term).getPostingline(),docId),Parse.docPosting.get(docId).getDocLength()));
        }
        return answer;
    }
}
