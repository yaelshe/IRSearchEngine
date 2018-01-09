

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    //private static Regex r=new Regex((new char[]{'!','?','#','$',',','&','=','*','+','<','>','^','(',')','{','}',
    //'[',']','\"',';',':','|',}));
    //(,$#!&=?*<>^(){}\":;+|\\[\\]]);
    public static HashMap<String,String> stopwordsMain;

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        String line="paribass # 4 &{FT922-10596:111} {FT923-3497:1} {FT923-14404:32} {FT923-10502:1} [4]";
        String line2="paribass # 4 &{FT922-105961:111} {FT923-3497:1} {FT923-14404:32} {FT923-10502:1} [4]";

        String str="<num> Number: 351 \n" +
                "<title> Falkland petroleum exploration \n" +
                "\n" +
                "<desc> Description: \n" +
                "What information is available on petroleum exploration in \n" +
                "the South Atlantic near the Falkland Islands?\n" +
                "\n" +
                "<narr> Narrative: \n" +
                "Any document discussing petroleum exploration in the\n" +
                "South Atlantic near the Falkland Islands is considered\n" +
                "relevant.  Documents di";
        String queryiD=str.substring(str.indexOf("Number:")+8,str.indexOf("<title>"));
        String query=str.substring(str.indexOf("<title>")+8,str.indexOf("<desc>"));
        String queryDesc=str.substring(str.indexOf("Description:")+12,str.indexOf("<narr>"));
        System.out.println(queryiD.trim() + "  id");
        System.out.println(query.trim()+ "  the query");
        System.out.println(queryDesc.trim()+ " the desc");
        //ArrayList <String> arr= breakToDocsFrequ(line);
        String s="";
        HashMap<String,Integer> sentences= new HashMap<>();

        /*ArrayList<String> querys=new ArrayList<String> (getQuerysFromText(s));
        String sq=querys.get(2);
        System.out.println(querys);
        createMapStopWords();
        Parse p= new Parse(stopwordsMain,false);
        p.parseDoc(sq,true);
        HashMap queryTerms=  new HashMap<>(p.m_terms);
        System.out.println(p.m_terms.keySet().toString());
        int sizeofQuery=queryTerms.size();
        System.out.println(sizeofQuery+ " :size of query");
        //s=s.trim();
        */



        //hasdocs.putAll(breakToDocsId(line2));

        /*for (String tempDoc : arr) {
            // { FBIS3-42818 :2}
            //String docID=tempDoc.substring(0,tempDoc.indexOf(':'));
            //System.out.println(docID+ " docId");

            //String freqTerm=tempDoc.substring(tempDoc.indexOf(':')+1,tempDoc.length());
            //System.out.println(freqTerm+" term freq");
            System.out.println(tempDoc);
            //updateWeightDoc(df,docID,freqTerm);
            //System.out.println(tempDoc);
       }

        //String s=line;
        //String docName="FT923-14404";
        //String sx=s.substring((s.indexOf(docName+":")+docName.length()+1),s.indexOf('}',s.indexOf(docName+":")));
        //System.out.println(sx);
       // double d=Double.parseDouble(sx);
        //System.out.println(d+"number 1");
        //String sosh=getLineFromFile(1,"C:\\Users\\sheinbey\\Downloads\\stop_words.txt");
        //System.out.println(sosh);
        System.out.println(Math.log(2));
        System.out.println(Math.log((2))/ Math.log(2));




       /* ReadFile r= new ReadFile("C:\\Users\\sheinbey\\Downloads\\");
        Indexer indexer=null;
        //C:\Users\sheinbey\Downloads\corpus
        int i = 0;
        while (r.nextFile<r.filesPaths.size())
        {
            System.out.println(i);
            Runtime instance=Runtime.getRuntime();
            System.out.println((instance.totalMemory())/(1024*1024)+"fd");
            r.breakToFiles();
            System.out.println((instance.totalMemory())/(1024*1024)+"fdd");
            Parse P= new Parse(r.stopword,r.documents,true);
            P.ParseAll();

            //indexer=new Indexer(P.m_terms,0,pathToPosting);
            try {
                 indexer =new Indexer(P.m_terms,i,"",P.d);//changed to i
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("here dosnt");
            }
            i++;
            //indexer=new Indexer();//add the m_terms and the path for posting files
        }
        try {
            indexer.mergeAllFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000/60);
        // String []strArray=s1.split("\\s+");
        //for(int i=0; i<strArray.length;i++ )
        //     {
        //strArray[i] = strArray[i].substring(0, strArray[i].length() - 1);
        //        System.out.println(strArray[i]);
        //    }

        //List<String> ph=capitalTerm(strArray[0],strArray[1],strArray[2],strArray[3]);
        //System.out.println(ph.toString());
        //System.out.println(s);
        /**for(int g=0; g<strArray.length;g++)
         {
         System.out.println("old "+strArray[g]);
         strArray[g]=removeExtra(strArray[g]);
         System.out.println("new "+strArray[g]);
         }*/
        //}
        //  if(!(n.equals(n.toLowerCase())))
        //    capitalTerm(n);
        //else
        //  System.out.println(n.toLowerCase());
        //System.out.println(n.toLowerCase());
        /*if(n.endsWith("\'nt")) {
            System.out.println(n.substring(0, n.indexOf('\'')));
            System.out.println(n.substring(0,n.indexOf('\''))+"nt");
        }*/
        //term.add(n.n.substring(0,n.indexOf('\'')));
        //terms.add(n.substring(0,n.indexOf('\''))+"nt");
        //String n= ".13";
        //if(isNumeric(n))
        //    System.out.print("is number");
        //else
        //    System.out.print("not");
        //System.out.println(str.charAt(str.length()));
        //System.out.println(str.charAt(str.length() - 1));
        //if ((str.charAt(str.length() - 1) == '\"'))
        //    System.out.println("have slash");
        //else
        //    System.out.println("no have");
        //System.out.println(str.length);
    }
    private static void show5sentences(HashMap<String,Integer> sentences)
    {
        StringBuilder sb= new StringBuilder();
        int i=1;
        for (String str: sentences.keySet())
        {
            sb=sb.append(i+". Score:"+sentences.get(str)+"\n");
            sb=sb.append(str+"\n");
            i++;

        }
        //AlertBox fiveSentences= new AlertBox();
        //fiveSentences.display("5 Most important senteces in ", sb.toString());
        System.out.println(sb.toString());
    }
    private static ArrayList<String> getQuerysFromText(String text)
    {
        ArrayList <String> allMatchesofQuery ;
        String regex = "<title>(?s)(.+?)<desc>";
        //TODO make the pattern static and compiled once
        allMatchesofQuery = new ArrayList <String>();
        Matcher m = Pattern.compile(regex).matcher(text);
        while (m.find()) {
            allMatchesofQuery.add(m.group(1).toString().trim());
        }
        return allMatchesofQuery;
    }
    private static HashMap <String,String>breakToDocsId(String line)
    {
        HashMap <String,String> allMatchesofdoc ;
        String regex = "\\{(?s)(.+?)\\:";
        //TODO make the pattern static and compiled once
        allMatchesofdoc = new HashMap <String,String>();
        Matcher m = Pattern.compile(regex).matcher(line);
        while (m.find()) {
            allMatchesofdoc.put(m.group(1),"");
        }
        return allMatchesofdoc;
    }
    public static String getLineFromFile(int lineNumber,String pathToFile)
    {//https://stackoverflow.com/questions/2312756/how-to-read-a-specific-line-using-the-specific-line-number-from-a-file-in-java/38229581
        String lineIWant="";
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(pathToFile);
            //TODO NEED TO CHANGE IN BRACKETS TO pathToFile
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                    for (int i = 0; i < lineNumber; ++i)
                        br.readLine();
                    lineIWant = br.readLine();
                br.close();
            }
            catch (IOException ei) {
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return lineIWant;
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
    private static ArrayList<String> breakToDocsFrequ(String line)
    {
        ArrayList<String> allMatchesofdoc ;
        String regex = "\\{(?s)(.+?)\\:";
        //TODO make the pattern static and compiled once
        allMatchesofdoc = new ArrayList<String>();
        Matcher m = Pattern.compile(regex).matcher(line);
        while (m.find()) {
            //AbstractList<String> allMatchesofdoc;
            allMatchesofdoc.add(m.group(1));
            //System.out.println(m.group((1))+"this item");
        }
        return allMatchesofdoc;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public static String removeExtra(String str)
    {
        //str=str.replaceAll("[,$#!&?*(){}\":;+|\\[\\] \\s\\\\ ]","");
        str=str.replaceAll("[,$#!&?*()<>^{}\\\":;+|\\[\\]\\s\\\\]","");
        StringBuilder sdot= new StringBuilder();
        if (str.length()>0) {
            char last = str.charAt(str.length() - 1);
            char first = str.charAt(0);
            if (first == '<' || first == '\'' || first == '^' || first == '.'||first == '-')
                str = str.substring(1, str.length() - 1);
            if(str.length()>0) {
                if (last == '.' || last == '\'' || last == '^' || last == '-' || last == '>')
                    str = str.substring(0, str.length() - 1);
            }
            if (str.contains(".")) {
                int dot = str.indexOf('.');
                if (dot != str.lastIndexOf('.')) {
                    sdot.append(str.substring(0, dot + 1));
                    str = str.substring(dot + 1, str.length());
                    str = str.replaceAll("\\.", "");
                    sdot.append(str);
                    str = sdot.toString();
                }
            }
        }
        return str;
    }
    public static void breaktoMakaf(String termsDoc)
    {
        if(termsDoc.contains("-"))
        {
            int makaf=termsDoc.indexOf("-");
            String part1=termsDoc.substring(0,makaf);
            //addToTerm(part1);
            System.out.println(part1);
            String part2=termsDoc.substring(makaf+1,termsDoc.length());
            //System.out.println((part2.substring(part2.indexOf('-')+1,part2.length())).length());
            if ((part2.contains("-"))&&((part2.substring(part2.indexOf('-')+1,part2.length())).length()>0))
            {
                String part3=part2.substring(0,part2.indexOf('-'));
                String part4=part2.substring(part2.indexOf('-')+1,part2.length());

                //addToTerm(part3);
                System.out.println(part3);
                System.out.println(part4);
                //addToTerm(part4);
            }
            else {
                //addToTerm(part2);
            }
            //addToTerm(part2);
            //add part1, part 2 part 1 &part 2 together and with - to terms

        }
    }
    private static void createMapStopWords()
    {
        String pathofstopword="D:\\stop_words.txt";
        //TODO CHANGE PATH TO STOPWORDS
        String []stops=(readStopword(pathofstopword));
        stopwordsMain = new HashMap<>();
        for(int i=0;i<stops.length;i++)
        {
            stopwordsMain.put(stops[i],"");
        }
    }

    /**
     * this method return an array of the stop word from file path S
     * @param S - the path to the stop word file
     * @return - array of stop word
     */
    private static String [] readStopword(String S){
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

}
