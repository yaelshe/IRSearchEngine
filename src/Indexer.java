
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;

/**
 * This class performs the indexing of the corpus
 * it creates the temporary posing files and the the final posting file
 * build the Dictionary and the Cache
 */
public class Indexer
{
    private Map<String,Term>mp_terms;
    public static Map<String,TermDic> m_Dictionary=new HashMap<>();
    //private final List sortedTerms;
    public static Map<String,TermCache>m_Cache= new HashMap<>();
    private int mytxt;
    private String mypath;
    private String newLine;
    private String pathToDictioanary;
    public static Map<String,Double> docWeight;
    static int numofDocsIndex;
    BufferedWriter writerDic;
    private static int counterLine;

    /**
     * this method initialize the indexer and call the function that start to create the temporary posting files
     * @param parsedWords- the words after the pars process
     * @param i- counter to keep track of number of documents
     * @param mypath- the path to save posting file and dictionary
     * @throws IOException -
     */
    public Indexer(Map<String,Term> parsedWords,int i,String mypath,int docsNum) throws IOException {//change the i to path ....
        //this.mypath="C:\\Users\\sheinbey\\Downloads\\11\\";
        this.mypath=mypath+"\\";
        pathToDictioanary=mypath+"\\";
        //C:\Users\sheinbey\Downloads\corpus
        mp_terms=parsedWords;
        mytxt = i;
        newLine = System.getProperty("line.separator");
        tempPosting();
        numofDocsIndex=docsNum;
        docWeight=new HashMap<>();

    }

    /**
     * this method creates the temporary posting files
     * @throws IOException -
     */
    public void tempPosting() throws IOException
    {
        //NEED TO CHANGE BACK TO GET MYPART !!!!!!!!!!!!!!!!!!!!!!!
        //File logFile=new File("C:\\Users\\yaels\\Desktop\\11\\ibr.txt");
        File logFile=new File(mypath+mytxt+".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
        //System.out.println(logFile.getCanonicalPath());
        //int i = 1;
        System.out.println(logFile.getAbsolutePath());
        List <String>sortedTerms=new ArrayList(mp_terms.keySet());
        Collections.sort(sortedTerms);
        for( String s: sortedTerms)
        {
            if(mp_terms.get(s).getTotalApperance()<2)
                continue;
            if(m_Dictionary.containsKey(s))
            {
                //TermDic t = m_Dictionary.get(s);
                m_Dictionary.get(s).setApperances((mp_terms.get(s).getTotalApperance()));
                m_Dictionary.get(s).setNumOfDocs(mp_terms.get(s).getNumOfDocIDF());
                // t.setApperances((mp_terms.get(s)).getValue().getTotalApperance());
                //m_Dictionary.get(termo.getKey()).setNumOfDocs(termo.getValue().getNumOfDocIDF());
            }
            else
                m_Dictionary.put(s, new TermDic(s,mp_terms.get(s).getTotalApperance(),
                        0,mp_terms.get(s).getNumOfDocIDF()));
            String value =mp_terms.get(s).toString();//get the string that describe the term
            try {
                writer.write (value+newLine);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        sortedTerms.clear();
        mp_terms.clear();
        System.gc();
        writer.close();
    }

    /**
     * this method start the merging of the temporary posting files merge insertion algorithm between each 2 files
     * @throws IOException
     */
    public  void  mergeAllFile() throws IOException {
        File logFile=new File(mypath+"00.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
        File directory = new File(mypath+"1");
        directory.mkdirs();
        String path3=directory.getCanonicalPath();
        for (int j=0;j<=45;j=j+2){
            String s =mergeTwoFile(mypath+j+".txt",mypath+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        String thepath = directory.getAbsolutePath();
        directory=new File(mypath+"\\4");
        directory.mkdirs();
        path3=mypath+"4";
        for (int j=0;j<22;j=j+2){
            String s =mergeTwoFile(thepath+"\\"+j+".txt",thepath+"\\"+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        String t=mergeTwoFile(thepath+"\\"+22+".txt",logFile.getAbsolutePath(),path3+"\\11");
        thepath = directory.getAbsolutePath();
        directory=new File(mypath+"\\5");
        directory.mkdirs();
        path3=mypath+"5";
        for (int j=0;j<=11;j=j+2){
            String s =mergeTwoFile(thepath+"\\"+j+".txt",thepath+"\\"+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        thepath = directory.getAbsolutePath();
        directory=new File(mypath+"\\6");
        directory.mkdirs();
        path3=mypath+"6";
        for (int j=0;j<=5;j=j+2){
            String s =mergeTwoFile(thepath+"\\"+j+".txt",thepath+"\\"+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        thepath = directory.getAbsolutePath();
        directory=new File(mypath+"\\7");
        directory.mkdirs();
        path3=mypath+"7";
        for (int j=0;j<2;j=j+2){
            String s =mergeTwoFile(thepath+"\\"+j+".txt",thepath+"\\"+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        t=mergeTwoFile(thepath+"\\"+2+".txt",logFile.getAbsolutePath(),path3+"\\1");
        thepath = directory.getAbsolutePath();
        directory=new File(mypath+"\\8");
        directory.mkdirs();
        path3=mypath+"8";
        for (int j=0;j<=1;j=j+2){
            mergeTwoFileLast(thepath+"\\"+j+".txt",thepath+"\\"+(j+1)+".txt",path3+"\\"+(j/2));
            System.out.println(j);
        }
        deleteTheTempPosting();
    }

    /**
     * this method to delete the old posting files
     */
    private void deleteTheTempPosting()
    {
        File file= new File(mypath+"00.txt");
        if(file.exists())
            try {
                file.delete();
            }catch(Exception e){
                System.out.println("didn't delete file indexer row 174");
            }
        for(int i=0;i<=45;i++){
            File file1= new File(mypath+i+".txt");
            if(file1.exists())
                try {
                    file1.delete();
                }catch(Exception e){
                    System.out.println("didn't delete file indexer row 174");
                }
        }
        try {
            deleteDirectory(mypath+"/"+1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("didn't succeseded deleting directories");
        }
        for(int j=4;j<8;j++)
        {
            try {
                deleteDirectory(mypath+"/"+j);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void deleteDirectory(String filePath) throws IOException {
        try {
            File file  = new File(filePath);
            if(file.isDirectory()){
                String[] childFiles = file.list();
                if(childFiles == null) {
                    //Directory is empty. Proceed for deletion
                    file.delete();
                }
                else {
                    //Directory has other files.
                    //Need to delete them first
                    for (String childFilePath :  childFiles) {
                        //recursive delete the files
                        deleteDirectory(childFilePath);
                    }
                }

            }
            else {
                //it is a simple file. Proceed for deletion
                file.delete();
            }
        }

        catch (Exception e)
        {
            System.out.println("files froblem with delteing");
        }
    }
    /**
     * this method merge insertion algorithm between the *last* 2  temporary posting files and creates the cache and update the pointer in the dictionary
     * @param path1 -
     * @param path2 -
     * @param path3 -
     *
     * @return -
     * @throws IOException -
     *///TODO how i save diffrent postion for non stemming???
    public void mergeTwoFileLast(String path1,String path2,String path3) throws IOException{
        //the merge for the last tow temporary posting files
        //create the dictionary and cache
        List <TermDic>sortedTerms=new ArrayList(m_Dictionary.values());
        Collections.sort(sortedTerms);
        int sizezush=sortedTerms.size();
        for(int m=0;m<10000;m++)
        {
            m_Cache.put(sortedTerms.get(sortedTerms.size()-1-m).getName(),
                    new TermCache(sortedTerms.get(sortedTerms.size()-1-m).getName(),
                            "",0));
        }
        File logFileDic=new File(pathToDictioanary+"dictionary"+".txt");
        writerDic = new BufferedWriter(new FileWriter(logFileDic));
        counterLine=0;
        File finalFile=new File(path3+".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(finalFile));
        BufferedReader br = new BufferedReader(new FileReader(path1));
        BufferedReader bs = new BufferedReader(new FileReader(path2));
        System.out.println(finalFile.getAbsolutePath());
        System.out.println(path1);
        System.out.println(path2);
        boolean write1 = false;
        boolean write2 = false;
        boolean read1=true;
        boolean read2=true;
        String line = br.readLine();
        String line2 = bs.readLine();
        String s1 = line.substring(0, line.indexOf("#") - 1);
        String s2 = "";
        if (line2!=null) {
            s2 = line2.substring(0, line2.indexOf("#") - 1);
        }
        while (line != null && line2 != null &&!line.equals("null") &&!line2.equals("null")) {
            if (line != null&&!line.equals("null")){
                //System.out.println(line);
                s1 = line.substring(0, line.indexOf("#") - 1);
                //System.out.println(s1);
            }
            if (line2 != null&& !line2.equals("null")){
                // System.out.println(line2);
                //System.out.println(line2.indexOf("#") - 1);
                s2 = line2.substring(0, line2.indexOf("#") - 1);

            }
            if (s1.equals(s2))
            {//if it's the same term

                String docs = line.substring(line.indexOf("&"), line.indexOf("[")) + line2.substring(line2.indexOf("&") + 1, line2.indexOf("["));
                String NUM1=line.substring(line.indexOf("#") + 1, line.indexOf("&") - 1);
                NUM1=NUM1.replaceAll(" ","");
                //
                String NUM11=line.substring(line.indexOf("[") + 1, line.indexOf("]") );
                NUM11=NUM11.replaceAll(" ","");
                //
                String NUM2=line2.substring(line2.indexOf("#") + 1, line2.indexOf("&") - 1);
                NUM2=NUM2.replaceAll(" ","");
                //
                String NUM22=line2.substring(line2.indexOf("[") + 1, line2.indexOf("]") );
                NUM22=NUM22.replaceAll(" ","");
                //
                int number = Integer.parseInt(NUM1) + Integer.parseInt(NUM2);
                int number2 = Integer.parseInt(NUM11) + Integer.parseInt(NUM22);
                line = s1 + " " + "#" + " " + number + " " + docs+"["+number2+"]";
                writer.write(line + System.getProperty("line.separator"));
                insertDicAndCache(s1,line);
                getDocsForTerm(number,line);
                counterLine++;
                write1 = true;
                write2 = true;
                //writer.close();
            } else {
                if (s1.compareTo(s2) < 0) {
                    //System.out.println(s1);
                    writer.write(line + System.getProperty("line.separator"));
                    int appearances=getAppearances(line);
                    insertDicAndCache(s1,line);
                    getDocsForTerm(appearances,line);
                    counterLine++;
                    write1 = true;
                    write2 = false;
                    //writer.close();
                } else {
                    //System.out.println(s2);
                    writer.write(line2 + System.getProperty("line.separator"));
                    //if (!m_Dictionary.containsKey(s2))
                    // System.out.println(s2);
                    int appearances2=getAppearances(line2);
                    insertDicAndCache(s2,line2);
                    getDocsForTerm(appearances2,line2);
                    counterLine++;
                    write1 = false;
                    write2 = true;
                    //writer.close();
                }
            }
            if (write1)
                line = br.readLine();
            if (write2)
                line2 = bs.readLine();
        }
        if (line != null) {
            while (line != null) {
                //System.out.println(1);
                if (!read1&&write1)
                {
                    line = br.readLine();
                    write1=false;
                }
                if (!write1)
                {
                    // System.out.println(line+"this line sucks 280 indexer");
                    writer.write(line + System.getProperty("line.separator"));
                    //int appercances=getAapperances(line);
                    if(line!=null&&!line.equals("null")) {
                        String sx = line.substring(0, line.indexOf("#") - 1);
                        int appearances3=getAppearances(line);
                        insertDicAndCache(sx,line);
                        getDocsForTerm(appearances3,line);
                    }
                    counterLine++;
                    write1=true;
                }
                read1 =false;
            }
        } else
        {
            while (line2 != null) {
                //System.out.println(2);
                if (!read2&&write2)
                {
                    line2 = bs.readLine();
                    write2=false;
                }
                if (!write2)
                {
                    writer.write(line2 + System.getProperty("line.separator"));
                    if(line2!=null&&!line2.equals("null")) {
                        String s = line2.substring(0, line2.indexOf("#") - 1);
                        int appearances4=getAppearances(line2);
                        insertDicAndCache(s,line2);
                        getDocsForTerm(appearances4,line2);
                    }
                    counterLine++;
                    write2=true;
                }
                read2=false;
            }
        }
        writerDic.close();
        writer.close();
        //System.out.println("closed");
    }

    /**
     * this method take 2 text files and mergethem with insertion algorithm between 2 temporary posting files into a new 3rd file
     * @param path1 -
     * @param path2 -
     * @param path3 -
     * @return -
     * @throws IOException -
     */
    public String mergeTwoFile(String path1,String path2,String path3) throws IOException {
        File logFile=new File(path3+".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
        BufferedReader br = new BufferedReader(new FileReader(path1));
        BufferedReader bs = new BufferedReader(new FileReader(path2));
        System.out.println(logFile.getAbsolutePath());
        System.out.println(path1);
        System.out.println(path2);
        boolean write1 = false;
        boolean write2 = false;
        boolean read1=true;
        boolean read2=true;
        String line = br.readLine();
        String line2 = bs.readLine();
        String s1 = line.substring(0, line.indexOf("#") - 1);
        String s2 = "";
        if (line2!=null) {
            s2 = line2.substring(0, line2.indexOf("#") - 1);
        }

        while (line != null && line2 != null &&!line.equals("null") &&!line2.equals("null")) {
            /**try {
             if (write1) {
             line = br.readLine();
             //System.out.println(line);*/
            if (line != null&&!line.equals("null")){
                //System.out.println(line);
                s1 = line.substring(0, line.indexOf("#") - 1);
                //System.out.println(s1);
            }
            /**}
             } catch (IOException e) {
             e.printStackTrace();
             }
             try {
             //try(BufferedReader br = new BufferedReader(new FileReader(path2))) {
             if (write2) {
             line2 = bs.readLine();
             //System.out.println(line2);*/
            if (line2 != null&& !line2.equals("null")){
                // System.out.println(line2);
                //System.out.println(line2.indexOf("#") - 1);
                s2 = line2.substring(0, line2.indexOf("#") - 1);
            }
            /**}
             } catch (IOException e) {
             e.printStackTrace();
             }*/
            if (s1.equals(s2)) {
                String docs = line.substring(line.indexOf("&"), line.indexOf("[")) + line2.substring(line2.indexOf("&") + 1, line2.indexOf("["));
                String NUM1=line.substring(line.indexOf("#") + 1, line.indexOf("&") - 1);
                NUM1=NUM1.replaceAll(" ","");
                //
                String NUM11=line.substring(line.indexOf("[") + 1, line.indexOf("]") );
                NUM11=NUM11.replaceAll(" ","");
                //
                String NUM2=line2.substring(line2.indexOf("#") + 1, line2.indexOf("&") - 1);
                NUM2=NUM2.replaceAll(" ","");
                //
                String NUM22=line2.substring(line2.indexOf("[") + 1, line2.indexOf("]") );
                NUM22=NUM22.replaceAll(" ","");
                //
                int number = Integer.parseInt(NUM1) + Integer.parseInt(NUM2);
                int number2 = Integer.parseInt(NUM11) + Integer.parseInt(NUM22);
                line = s1 + " " + "#" + " " + number + " " + docs+"["+number2+"]";
                writer.write(line + System.getProperty("line.separator"));
                write1 = true;
                write2 = true;
                //writer.close();
            } else {
                if (s1.compareTo(s2) < 0) {
                    //System.out.println(s1);
                    writer.write(line + System.getProperty("line.separator"));
                    write1 = true;
                    write2 = false;
                    //writer.close();
                } else {
                    //System.out.println(s2);
                    writer.write(line2 + System.getProperty("line.separator"));
                    write1 = false;
                    write2 = true;
                    //writer.close();
                }
            }
            if (write1)
                line = br.readLine();
            if (write2)
                line2 = bs.readLine();
        }
        if (line != null) {
            while (line != null) {
                //System.out.println(1);
                if (!read1&&write1)
                {
                    line = br.readLine();
                    write1=false;
                }
                if (!write1)
                {
                    writer.write(line + System.getProperty("line.separator"));
                    write1=true;
                }
                read1 =false;
            }
        } else
        {
            while (line2 != null) {
                //System.out.println(2);
                if (!read2&&write2)
                {
                    line2 = bs.readLine();
                    write2=false;
                }
                if (!write2)
                {
                    writer.write(line2 + System.getProperty("line.separator"));
                    write2=true;
                }
                read2=false;
            }
        }
        writer.close();
        System.out.println("closed");

        return "";
    }
    private int getAppearances(String line)
    {
        int first=line.indexOf("[");
        int last=line.indexOf("]");
        int appearances=0;
        try {
            appearances = Integer.parseInt((line.substring(first+1, last)).trim());
        }
        catch(NumberFormatException nfe) {
            System.out.println("dont have int appernces print in indexr last merge");
        }
        return appearances;
    }
    /**
     * this method find the 2 documents that a term from the cache appeared in the most
     * @param docs
     * @return a string that indicate the 2 documents that the term appeared in and the number of appearances
     */
    public String findTheDocs(String docs){
        List<String> allMatchesofdoc =new ArrayList<>(breakToDocsFrequ(docs));
        String docid1="";
        String docid2="";
        int max1=0;
        int max2=0;
        for (int i =0; i<allMatchesofdoc.size();i++){
            String s = allMatchesofdoc.get(i);
            int x = Integer.parseInt(s.substring(s.indexOf(":")+1));
            if (x>max2){
                if (x>max1){
                    max2=max1;
                    max1=x;
                    docid2=docid1;
                    docid1=allMatchesofdoc.get(i);
                    continue;
                }
                max2=x;
                docid2=allMatchesofdoc.get(i);
            }
            //System.out.println(allMatchesofdoc.get(i));
        }
        String s = "["+docid1+" "+docid2+"]";
        return s;
    }

    /**
     * this method recieve the df of the term and the line from the posting and update the weight for each doc in the docs list of the term
     * @param df - df of term
     * @param line - line from posting of term
     */
    private void getDocsForTerm(int df,String line)
    {
        String docID, freqTerm;
        List<String> allMatchesofdoc =new ArrayList<>(breakToDocsFrequ(line));

        for (String tempDoc : allMatchesofdoc) {
            // FBIS3-42818 :2
            docID=tempDoc.substring(0,tempDoc.indexOf(':'));
            freqTerm=tempDoc.substring(tempDoc.indexOf(':')+1,tempDoc.length());
            updateWeightDoc(df,docID,freqTerm);
            //System.out.println(tempDoc);
        }

    }

    /**
     * this method takes only the list of document from the posting line of the term
     * brealk it to strings of FBIS3-42818 :2 and put them in an array of string
     * @param line - the line from the posting file for the term
     * @return array list of FBIS3-42818 :2 for each doc in the list for the term
     */
    private ArrayList<String> breakToDocsFrequ(String line)
    {
        ArrayList<String> allMatchesofdoc ;
        String regex = "\\{(?s)(.+?)\\}";
        //TODO make the pattern static and compiled once
        allMatchesofdoc = new ArrayList<String>();
        Matcher m = Pattern.compile(regex).matcher(line);
        while (m.find()) {
            //AbstractList<String> allMatchesofdoc;
            allMatchesofdoc.add(m.group(1));
        }
        return allMatchesofdoc;
    }

    /**
     * This method update the weight of the doc in the docWeight Map
     * @param df - df of the term
     * @param docWith - docId
     * @param TermFrequency- frequency of the term in this doc
     */
    private void updateWeightDoc(int df,String docWith,String TermFrequency)
    {
        double idf=computeIdf(df);
        double itf=computeItf(docWith,TermFrequency);
        double weight=Math.pow(idf*itf,2);
        Parse.allDocs.get(docWith).setDocWeight(weight);
    }

    /**
     * this method calculate the tf for the with length of the doc from the parse Map with docCounterWords
     * @param docWith - doc Id
     * @param termFrequency - term frequency in this document
     * @return the tf of the word in the document
     */
    private double computeItf(String docWith,String termFrequency)
    {
        double t=Double.parseDouble(termFrequency);
        return t/(Parse.docPosting.get(docWith));
    }

    /**
     * compute the idf
     * @param df- df of the term
     * @return - the idf for document
     */
    private double computeIdf(int df)
    {
        return Math.log((Parse.countDoc/df)) / Math.log(2);
    }

    /**
     * this method recive a line from posting of a term and the term as string and insert to dictionary
     * and to the cache if necessary
     * @param s - the term
     * @param line- the posting line of the term
     */
    private void insertDicAndCache(String s,String line)
    {
        if (m_Dictionary.containsKey(s)) {
            m_Dictionary.get(s).setPointer(counterLine);
            try {
                writerDic.write("name:"+m_Dictionary.get(s).getName()+
                        "_numberofdocs:"+m_Dictionary.get(s).getNumOfDocs()+
                        "_total,apperances:"+m_Dictionary.get(s).getApperances()+
                        "_pointer:"+m_Dictionary.get(s).getPointer()+newLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//TODO change the pointer from the dictionary to cache if the word is in the cache
        if (m_Cache.containsKey(s)) {
            m_Cache.get(s).setPointer(counterLine);
            m_Cache.get(s).setFavDocs(findTheDocs(line));
        }

    }
}