

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

/**
 * This Class activate the Gui for Part B  of the search engine
 */
public class GuiPartB extends Application {

    Stage window;
    //Scene dictionaryScene, cacheScene;
    ListView<String> dictionary;
    TableView<CacheTermGui> cache;
    TextField queryFileInput;
    TextField loadInput;
    TextField docNameInput;
    TextField saveInput;
    TextField queryInput;
    String pathToLoad="";
    String pathToSave="";
    String pathToQueryFile="";
    String query="";
    String s="";
    boolean load=false;
    // String pathToPosting="C:\\Users\\yaels\\Desktop\\11";
    //String pathToCorpus="";
    ReadFile r;
    Parse P;
    Indexer indexer;
    long totalTime;
    boolean finish=false;

    /**
     * this function that launch the program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method activate the window for the Gui
     * @param primaryStage
     */
    @Override

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Welcome to our search engine! ");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        dictionary = new ListView<>();

        //DICTIONARY LOAD Label - constrains use (child, column, row)
        Label loadLabel = new Label("to load all the files necessary press:");
        GridPane.setConstraints(loadLabel, 0, 0);
        //load button
        Button loadDictionary = new Button("Load FILES");
        GridPane.setConstraints(loadDictionary, 3, 0);

        //load the created files
        loadInput = new TextField();
        loadInput.setPromptText("load path here");
        GridPane.setConstraints(loadInput, 1, 0);

        Button browseButton4 = new Button("browse");
        GridPane.setConstraints(browseButton4, 2, 0);
        browseButton4.setOnAction(e-> browserLoadQueryF());

        //query Label - constrains use (child, column, row)
        Label queryLabel = new Label("Enter query or docId:");
        GridPane.setConstraints(queryLabel, 0, 1);

        //query string Input
        queryInput = new TextField();
        queryInput.setPromptText("insert queryor docId here");
        GridPane.setConstraints(queryInput, 1, 1);

        //run query button
        Button runQuery = new Button("Run");
        GridPane.setConstraints(runQuery, 2, 1);

        //check box for expand
        CheckBox doc5sentences=new CheckBox("Get 5 for doc?");
        GridPane.setConstraints(doc5sentences, 3, 1);

        //Stemming
        CheckBox stemmerCheck=new CheckBox("Stemming?");
        GridPane.setConstraints(stemmerCheck, 4, 0);
        loadDictionary.setOnAction(e -> {
            try {
                loadDictionaryF(stemmerCheck.isSelected());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        })
        ;
        runQuery.setOnAction(e-> runTheQueryF(queryInput.getText(),doc5sentences.isSelected(),stemmerCheck.isSelected()));

        //run button for file of queries
        Button run2Query = new Button("Run");
        GridPane.setConstraints(run2Query, 3, 2);

        //file query Label
        Label fileQueryLabel = new Label("Enter path to query files:");
        GridPane.setConstraints(fileQueryLabel, 0, 2);

        //query file path Input
        queryFileInput = new TextField();
        queryFileInput.setPromptText("query file path here");
        GridPane.setConstraints(queryFileInput, 1, 2);

        run2Query.setOnAction(e-> runQueryFiles(stemmerCheck.isSelected(),pathToQueryFile));
        //browse query file  button
        Button queryFileBrowse = new Button("browse");
        GridPane.setConstraints(queryFileBrowse, 2, 2);
        queryFileBrowse.setOnAction(e-> browserQueryFile());//TODO ADD FUNCTION TO QUERY FILE

        //RESET
        Button resetButton = new Button("RESET");
        GridPane.setConstraints(resetButton, 1, 4);
        resetButton.setOnAction(e->resetFuncButton());
        //reset Label
        Label resetLabel = new Label("To reset the results:");
        GridPane.setConstraints(resetLabel, 0, 4);

        //save the created files
        Button saveButton = new Button("SAVE");
        GridPane.setConstraints(saveButton, 3, 7);
        Label saveLabel = new Label("To save the files:");
        GridPane.setConstraints(saveLabel, 0, 7);
        saveButton.setOnAction(e -> {
            try {
                saveFiles(pathToSave);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        //browse for save
        Button browseSaveLocation = new Button("browse");
        GridPane.setConstraints(browseSaveLocation, 2, 7);
        browseSaveLocation.setOnAction(e-> browserSaveQueryF());

        //Button browseLoadLocation = new Button("browse");
        //.setConstraints(browseLoadLocation, 2, 8);
        //browseLoadLocation.setOnAction(e-> browserLoadQueryF());

        saveInput = new TextField();
        saveInput.setPromptText("save path here");
        GridPane.setConstraints(saveInput, 1, 7);

        //Add everything to grid
        grid.getChildren().addAll(stemmerCheck,run2Query,loadDictionary,loadLabel,queryLabel,queryInput,doc5sentences, fileQueryLabel, queryFileInput,runQuery, queryFileBrowse
                ,resetButton,resetLabel,saveButton,browseButton4,saveLabel, saveInput,loadInput,browseSaveLocation);
        Scene scene = new Scene(grid, 700, 300);
        window.setScene(scene);
        window.show();
    }
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)

    public void runTheQueryF(String query, boolean isDoc,boolean withStemm)
    {
        if(load) {
            if (query != null && !query.isEmpty()) {
                System.out.println("enterted run function");
                long startTime = System.currentTimeMillis();
                Searcher s;
                if (!isDoc) {//handle query inserted
                    s = new Searcher(query, withStemm);
                    long endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;
                    System.out.println(totalTime / 1000 / 60);
                    show50resultDocs(Ranker.docsToReturn);
                    //Ranker.docsToReturn.clear();

                } else {//handle docnumber inserted return 5 most important sentences
                    //System.out.println(tt.get("FBIS3-49"));
                    String ss = null;
                    try {
                        String pats = pathToLoad + "\\";
                        if (Parse.docPosting.containsKey(query))
                            System.out.println("contains");
                        else {
                            query = " " + query + " ";
                        }
                        String pat2 = Parse.docPosting.get(query).getDirectoryPathDoc();
                        //String pat2= ;
                        System.out.println("123");
                        ss = ReadFile.readFileAsString(pat2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String sx = ss.substring(ss.indexOf(query));
                    sx = sx.substring(sx.indexOf("<TEXT>") + 7, sx.indexOf("</TEXT>"));
                    //String sx="how you to do many how to you how. how many how. i am the man who. ttdk. do you want to go. do me how. ibrahem. yousef. sarsour";
                    Searcher.createMapStopWords();
                    ParseText P = new ParseText(Searcher.stopwords, sx, query, withStemm);
                    P.ParseAll();
                    List<String> mysentence1 = P.mysentence;
                    //HashMap<String,TermDic> m_terms1=P.m_terms;//**
                    //HashMap<Integer,HashMap<String,TermDic>> m_Sentence1=P.m_Sentence;
                    //int[] S=P.SentenceLength;
                    System.out.println(P.m_Sentence.size());
                    System.out.println(P.m_terms.size());
                    //System.out.println(P.SentenceLength[3]);
                    RankerSentence R = new RankerSentence(P.m_Sentence, P.m_terms, P.SentenceLength);
                    List<Integer> l = R.TopFive();
                    HashMap<String, Integer> top5 = new HashMap<>();
                    for (int t = 0; t < l.size(); t++) {
                        top5.put(mysentence1.get(l.get(t) - 1), t + 1);
                    }
                    show5sentences(top5);
                }
            } else {
                try {
                    AlertBox.display("Missing Input for query", "Error: no query has been written!");
                } catch (Exception e) {
                    System.out.println(" i caught the problem text filed is empty");
                }
            }
        }
        else
        {
            AlertBox.display("Alert", "Error: no files and structures has been loaded!");
        }
    }
    public void browserQueryFile()
    {
        FileChooser fc=new FileChooser();
        //DirectoryChooser dc=new DirectoryChooser();
        fc.setInitialDirectory((new File("C:\\")));
        File selectedFile=fc.showOpenDialog(null);
        try {
            s = selectedFile.getAbsolutePath();
            queryFileInput.setText(s);
            pathToQueryFile = s;
        }
        catch(Exception e)
        {
        }

    }
    /**
     * this method shows the 5 sentences for a document that we found
     * @param sentences - the sentences that we found
     */
    private void show5sentences(HashMap<String,Integer> sentences)
    {
        StringBuilder sb= new StringBuilder();
        int i=1;
        for (String str: sentences.keySet())
        {
             sb=sb.append(i+". Score:"+sentences.get(str)+"\n");
             sb=sb.append(str+"\n");
             i++;
        }
        AlertBox fiveSentences= new AlertBox();
        fiveSentences.display("5 Most important sentences in ", sb.toString());
    }
    private void show50resultDocs(List< String> results)
    {
        AlertBox boxResults= new AlertBox();
        StringBuilder sbResult= new StringBuilder();
        sbResult= sbResult.append("we found "+results.size()+" relevent documents"+"\n");
        sbResult= sbResult.append("Running query time: "+totalTime+"\n");
        for (String str: results)
        {
            sbResult=sbResult.append(str+"\n");
        }
        boxResults.display("Results:",sbResult.toString());
    }

    public void saveFiles(String pathToSave) throws IOException
    {
        if(!pathToSave.equals(""))
        {
            AlertBox boxResults = new AlertBox();
            File docFile = new File(pathToSave + "\\results.txt");
            BufferedWriter writerDoc = new BufferedWriter(new FileWriter(docFile));
            if (!Searcher.allResults.isEmpty()) {
                String newLine = System.getProperty("line.separator");
                for (String str : Searcher.allResults) {
                    writerDoc.write(str);
                }
                writerDoc.close();

                boxResults.display("Alert:", "saved!");
            }
            else
                boxResults.display("Alert:", "no results to save!");
        }
        else
        {
            AlertBox boxResults = new AlertBox();
            boxResults.display("done", "no path to save inserted");
        }
    }

    /**
     * this method browse for path to load the dictionary and cache for the program?
     */
    public void browserLoadQueryF()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        try {
            s = selectedFile.getAbsolutePath();
            loadInput.setText(s);
            pathToLoad = s;
        }
        catch(Exception e)
        {
        }
    }

    /**
     * this method browse for path to save the results into a file
     */
    public void browserSaveQueryF()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        try {
            s = selectedFile.getAbsolutePath();
            saveInput.setText(s);
            pathToSave = s;
        }
        catch(Exception e)
        {
        }
    }



    public void deleteReset(String path)
    {
        //https://docs.oracle.com/javase/tutorial/essential/io/delete.html
        String directoryPath = path;
        File file = new File(directoryPath);

        try {
            //Deleting the directory recursively.
            delete(file);
            System.out.println("Directory has been deleted recursively !");
        } catch (IOException e) {
            System.out.println("Problem occurs when deleting the directory : " + directoryPath);
            e.printStackTrace();
        }

    }

    private static void delete(File file) throws IOException {

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                delete(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }

    public void finishData()
    {//present all of the Data that is needed aout the program
        AlertBox.display("Program Information",
                "time of running:"+totalTime+
                        "number of files indexed:"+
                        "size of index in Bytes:"+
                        "size of cache in Bytes:");

    }
    private void runQueryFiles(boolean stem,String pathToFile)
    {
        if(!pathToFile.equals("")) {
            long startTime = System.currentTimeMillis();
            Searcher s;
            {//handle query inserted
                try {
                    s = new Searcher(stem, pathToFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                System.out.println(totalTime / 1000 / 60);
            }
            AlertBox boxResults = new AlertBox();
            boxResults.display("Alert", "done with all the queries");
        }
        else
        {
            AlertBox boxResults = new AlertBox();
            boxResults.display("Alert", "no path to file with queries inserted");
        }
    }
    public void loadDictionaryF(boolean isStemming) throws IOException, ClassNotFoundException {
        FileInputStream fi,fi2,file3;
        if(!pathToLoad.equals("")) {
            if (isStemming) {
                fi = new FileInputStream(new File(pathToLoad + "\\StemmyDictionary.ser"));
                fi2 = new FileInputStream(new File(pathToLoad + "\\StemmyCache.ser"));
                file3 = new FileInputStream(pathToLoad + "\\StemmydocPosting.ser");
            } else {
                fi = new FileInputStream(new File(pathToLoad + "\\myDictionary.ser"));
                fi2 = new FileInputStream(new File(pathToLoad + "\\myCache.ser"));
                file3 = new FileInputStream(pathToLoad + "\\docPosting.ser");
            }

            ObjectInputStream oi = new ObjectInputStream(fi);
            ObjectInputStream zi = new ObjectInputStream(fi2);
            ObjectInputStream z2 = new ObjectInputStream(file3);
            // Read objects
            Indexer.m_Dictionary = (HashMap<String, TermDic>) oi.readObject();
            Indexer.m_Cache = (HashMap<String, TermCache>) zi.readObject();
            Parse.docPosting = (HashMap<String, Document>) z2.readObject();
            System.out.println(Parse.docPosting.size() + "size of dics");
            load=true;
            AlertBox.display("finish loading", "finish loading");
        }
        else{
                AlertBox boxResults = new AlertBox();
                boxResults.display("Alert", "no path to load inserted");
        }
    }
    private void resetFuncButton()
    {
        queryFileInput.setText("");
        loadInput.setText("");
        saveInput.setText("");
        queryInput.setText("");
        pathToLoad="";
        pathToQueryFile="";
        s="";
        if(Ranker.docsToReturn!=null&&!Ranker.docsToReturn.isEmpty())
            Ranker.docsToReturn=null;
        if(Searcher.allResults!=null&&!Searcher.allResults.isEmpty())
            Searcher.allResults=null;
        //delete the result file
        File file = new File(pathToSave + "\\results.txt");
        if(file.delete()){
            System.out.println(file.getName() + " is deleted!");
        }else {
            System.out.println("Delete operation is failed.");
        }
        if(Indexer.m_Dictionary!=null&&!Indexer.m_Dictionary.isEmpty())
            Indexer.m_Dictionary=null;
        if(Indexer.m_Cache!=null&&!Indexer.m_Cache.isEmpty())
            Indexer.m_Cache=null;
        if(Parse.docPosting!=null&&!Parse.docPosting.isEmpty())
            Parse.docPosting=null;
        pathToSave="";

    }
}