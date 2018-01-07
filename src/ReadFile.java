
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * This class reaches the corpus file and take all the document form the folders
 * get the stop words list
 * and use iteration to read t the main memory only 40 folders at a time(out of 1815).
 */
public class ReadFile
{
    Map<String,Document> documents ;
    Map<String,String> stopword ;
    String mainPath;
    List<String> filesPaths;
    List<String> allMatchesofdoc;
    int sizofmydictionary;
    static int counDocs=0;
    int nextFile;
    int indexOfPathFolder;

    /**
     * This is the constructor which make the stop word list
     * and extract from the corpus file all of the path to folder s inside of it.
     * @param path- the path to the corpus
     */
    public ReadFile(String path)
    {
        //create the HashMap for the stopwords from the array;
        indexOfPathFolder=0;
        String pathofstopword=path+"\\stop_words.txt";
        String []stops=(readStopword(pathofstopword));
        stopword = new HashMap<>();// why save stop?
        for(int i=0;i<stops.length;i++)
        {
            stopword.put(stops[i],"");
        }
        this.mainPath = path;
        this.filesPaths = new ArrayList<>();
        this.sizofmydictionary=0;
        this.nextFile=0;
        try (Stream<Path> paths = Files.walk(Paths.get(path+"\\corpus"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path1 -> filesPaths.add(path1.toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * this method take the paths of the folders and extract from each folder the , file and the text inside of it
     * and send the file to be splitted to documents
     */
    public void breakToFiles()
    {
        List<String> mydocuments=new ArrayList<String>();
        /**this.filesPaths.forEach(s -> {
         try {
         //out.println(readFileAsString(s));
         mydocuments.add(readFileAsString(s));
         //out.println(documents[0]);
         } catch (IOException e) {
         e.printStackTrace();
         }
         });*/
        //mydocuments = new ArrayList<String>(); moved up to 1 line
        documents = new HashMap<>();
        sizofmydictionary=documents.size();
        allMatchesofdoc = new ArrayList<String>();
        for (int i = nextFile;i <this.filesPaths.size()&& i < nextFile+40 ;i++)
        {
            try {
                mydocuments.add(readFileAsString(this.filesPaths.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        nextFile=nextFile+40;
        mydocuments.forEach(s -> {
            breakToDocs(s,filesPaths.get(indexOfPathFolder));
        });
        mydocuments.clear();
    }

    /**
     * this method get the string of the hole file and divide to the docs inside by the tags "<DOC></DOC>"
     * and save all the documents and the text inside of the <TEXT> </TEXT> tags inside of them to "documents"
     * @param stringfile- the text in the file from the folder
     */
    private  void breakToDocs(String stringfile,String myPath)
    {
        //allMatchesofdoc.clear();
        allMatchesofdoc = new ArrayList<String>();
        String regex = "<DOC>(?s)(.+?)</DOC>";
        //String regex1 = "<TEXT>(?s)(.+?)</TEXT>";
        Matcher m = Pattern.compile(regex).matcher(stringfile);
        while (m.find()) {
            allMatchesofdoc.add(m.group(1));
        }
        int i=0;
        for( i=0;i<allMatchesofdoc.size();i++)
        {
            // mytext=new
            if (allMatchesofdoc.get(i).contains("<TEXT>")){
                int first = allMatchesofdoc.get(i).indexOf("<TEXT>");
                int last = allMatchesofdoc.get(i).indexOf("</TEXT>");
                String s =allMatchesofdoc.get(i).substring(first+6,last);
                int k = s.length();
                StringBuilder mytext = new StringBuilder(k);
                mytext.append(s);
                if(mytext.length()>0) {
                    first = allMatchesofdoc.get(i).indexOf("<DOCNO>");
                    last = allMatchesofdoc.get(i).indexOf("</DOCNO>");
                    String mydocno = allMatchesofdoc.get(i).substring(first+7,last);
                    Document S = new Document(mydocno,mytext.toString(), 0, "",myPath);
                    documents.put(mydocno, S);
                }

            }
            sizofmydictionary=documents.size();
        }
        counDocs=counDocs+i;
        // System.out.println(counDocs);
        allMatchesofdoc.clear();
    }

    /**
     * this method takes a path to a file and returnn the text that is inside.
     * @param filePath- tha path to the file
     * @return string for all of the text in the file
     * @throws IOException
     */
    private String readFileAsString(String filePath) throws IOException
    {//pull all the text from the file

        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * this method gets path to the stop word text file and return a string array with all of the words
     * @param S the path to the stop word file
     * @return the array of stop words as strings
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
        }
        String []stopwords=everything.split("\\s+");
        return  stopwords;
    }

}