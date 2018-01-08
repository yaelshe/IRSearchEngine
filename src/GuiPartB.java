

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    String s="";
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
        GridPane.setConstraints(loadDictionary, 1, 0);
        loadDictionary.setOnAction(e-> loadDictionaryF());

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
        GridPane.setConstraints(stemmerCheck, 2, 0);

        //TODO - WRITE THE RUN FUNCTION
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

        //browse query file  button
        Button queryFileBrowse = new Button("browse");
        GridPane.setConstraints(queryFileBrowse, 2, 2);
        queryFileBrowse.setOnAction(e-> browseQueryFileF());
/*
        //doc query Label
        Label docQueryLabel = new Label("Enter doc name to query files:");
        GridPane.setConstraints(docQueryLabel, 0, 3);

        //query file path Input
        docNameInput = new TextField();
        docNameInput.setPromptText("doc name here");
        GridPane.setConstraints(docNameInput, 1, 3);

        //browse query file  button
        Button docBrowse = new Button("browse");
        GridPane.setConstraints(docBrowse, 2, 3);
        docBrowse.setOnAction(e-> browseDocF());
        */
/**
 //Start
 Button startButton = new Button("START");
 GridPane.setConstraints(startButton, 1, 3);
 startButton.setOnAction(e -> {
 try {
 StartButton(postingInput.getText(), corpusInput.getText(), stemmerCheck.isSelected());
 } catch (IOException e1) {
 e1.printStackTrace();
 }
 });
 startButton.disableProperty().bind(Bindings.createBooleanBinding( () -> !((postingInput.getText()!=null && corpusInput.getText()!=null)),
 postingInput.textProperty(), corpusInput.textProperty()));
 */

        //RESET
        Button resetButton = new Button("RESET");
        GridPane.setConstraints(resetButton, 1, 4);
        //reset Label
        Label resetLabel = new Label("To reset the results:");
        GridPane.setConstraints(resetLabel, 0, 4);

        //save the created files
        Button saveButton = new Button("SAVE");
        GridPane.setConstraints(saveButton, 2, 7);
        Label saveLabel = new Label("To save the files:");
        GridPane.setConstraints(saveLabel, 0, 7);
        saveButton.setOnAction(e -> {
            try {
                saveFiles();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button browseSaveLocation = new Button("browse");
        GridPane.setConstraints(browseSaveLocation, 2, 7);
        browseSaveLocation.setOnAction(e-> browserSaveQueryF());

        Button browseLoadLocation = new Button("browse");
        GridPane.setConstraints(browseLoadLocation, 2, 8);
        browseLoadLocation.setOnAction(e-> browserLoadQueryF());


        //load the created files
        Button loadButton = new Button("LOAD");
        GridPane.setConstraints(loadButton, 2, 8);
        Label loadLabel2 = new Label("To load the files:");
        GridPane.setConstraints(loadLabel2, 0, 8);
        loadButton.setOnAction(e -> {
            try {
                loadFiles(stemmerCheck.isSelected());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        loadInput = new TextField();
        loadInput.setPromptText("load path here");
        GridPane.setConstraints(loadInput, 1, 8);

        saveInput = new TextField();
        saveInput.setPromptText("save path here");
        GridPane.setConstraints(saveInput, 1, 7);

        //Add everything to grid
        grid.getChildren().addAll(stemmerCheck,run2Query,loadDictionary,loadLabel,queryLabel,queryInput,doc5sentences, fileQueryLabel, queryFileInput,runQuery, queryFileBrowse
                ,resetButton,resetLabel,saveButton,saveLabel,loadButton,loadLabel2,
                saveInput,loadInput);

        Scene scene = new Scene(grid, 700, 300);
        window.setScene(scene);
        window.show();
    }
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)

    public void runTheQueryF(String query, boolean isDoc,boolean withStemm) {
        if (query != null && !query.isEmpty())
        {
            long startTime = System.currentTimeMillis();
            Searcher s;
            if (!isDoc) {//handle query inserted
                 s = new Searcher(query, withStemm, "");

                long endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                System.out.println(totalTime / 1000 / 60);
                show50resultDocs(Ranker.docsToReturn);
                Ranker.docsToReturn.clear();


            } else {//handle docnumber inserted return 5 most important sentences


            }




        }
        else
        {
            try {
                AlertBox.display("Missing Input for query", "Error: no query has been written!");
            } catch (Exception e) {
                System.out.println(" i caught the problem text filed is empty");
            }
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
        fiveSentences.display("5 Most important senteces in ", sb.toString());
    }
    private void show50resultDocs(HashMap< String, Double> results)
    {
        AlertBox boxResults= new AlertBox();
        StringBuilder sbResult= new StringBuilder();
        sbResult= sbResult.append("we found "+results.size()+"relevent documents"+"\n");
        sbResult= sbResult.append("Running query time: "+totalTime+"\n");
        for (String str : results.keySet())
        {
            sbResult=sbResult.append(str+"\n");
        }
        boxResults.display("Results:",sbResult.toString());
    }
    /** public void StartButton (String s1, String s2, boolean box1) throws IOException
     {
     Platform.runLater(new Runnable() {
    @Override
    public void run() {
    long startTime = System.currentTimeMillis();
    if(s1.length()>0&&s2.length()>0)
    {//the fields are filled

    pathToCorpus=s1;
    pathToPosting=s2;
    //change secene to alert and back to the main window to let write again
    //RadioButton chk = (RadioButton)box1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
    if(box1) {
    System.out.println("i checked yes");
    doStemming=true;
    }
    else {
    doStemming=false;
    System.out.println("i checked no");
    }
    }
    else{// the fields are missing
    //change scene to alert and back to the main window to let write again
    AlertBox.display("Missing Input", "Error: no paths had been written!");
    }
    r= new ReadFile("C:\\");
    int i = 0;
    while (r.nextFile<r.filesPaths.size())
    {
    System.out.println(i);
    Runtime instance=Runtime.getRuntime();
    System.out.println((instance.totalMemory())/(1024*1024)+"fd");
    r.breakToFiles();
    System.out.println((instance.totalMemory())/(1024*1024)+"fdd");
    P = new Parser(r.stopword,r.documents,doStemming);
    P.ParseAll();
    //indexer=new Indexer(P.m_terms,0,pathToPosting);
    try {
    indexer =new Indexer(P.m_terms,i,pathToPosting);//changed to i
    } catch (IOException e) {
    e.printStackTrace();
    System.out.println("here dosnt");
    }
    i++;
    //indexer=new Indexer();//add the m_terms and the path for posting files
    }
    try {
    indexer.mergAllFile();
    } catch (IOException e) {
    e.printStackTrace();
    }
    dictionary.setItems(getDictionaryTermGui());
    cache.setItems(getCacheTermGui());

    finish=true;
    }
    });
     }
     */
    public ObservableList<String> getDictionaryTermGui()
    {//get the items for the dictionary
        dictionary =new ListView<>();
        ObservableList<String> termsDictionary= FXCollections.observableArrayList();
        Map<String,TermDic> dict= indexer.m_Dictionary;//change to public for dictionary in indexer

        for(String str: dict.keySet())
        {
            String i=dict.get(str).getApperances()+"";
            termsDictionary.add("Term: "+dict.get(str).getName()+" , Frequency in our corpus: "+i);

        }
        //**********need to add the words from the dictionary**************
        return termsDictionary;
    }

    public ObservableList<CacheTermGui>getCacheTermGui()
    {//get the items for the dictionary
        ObservableList<CacheTermGui> termsCache= FXCollections.observableArrayList();
        cache =new TableView<>();
        // ObservableList<CacheTermGui> termsDictionary= FXCollections.observableArrayList();
        Map<String,TermCache>cachesss= indexer.m_Cache;//change to public for dictionary in indexer
        //**********need to add the words from the dictionary**************
        for(String str: cachesss.keySet())
        {
            //String i="";
            termsCache.add(new CacheTermGui(str,cachesss.get(str).getFavDocs()));
        }
        return termsCache;
    }


    public void displayDictTable()
    {//opens another window with the dictionary table display
        //if not working well try listView
        //https://stackoverflow.com/questions/27414689/a-java-advanced-text-logging-pane-for-large-output
        VBox vBox = new VBox();
        vBox.getChildren().addAll(dictionary);
        Scene dictionaryScene=new Scene(vBox);
        Stage dicwindow = new Stage();

        //Block events to other windows
        dicwindow.initModality(Modality.APPLICATION_MODAL);
        dicwindow.setTitle("THE DICTIONARY");
        dicwindow.setMinWidth(250);
        dicwindow.setScene(dictionaryScene);
        dicwindow.show();
    }
    public void displayCacheTable()
    {//opens another window with the dictionary table display
        VBox vBox = new VBox();
        vBox.getChildren().addAll(cache);
        Scene cacheScene=new Scene(vBox);
        Stage cachewindow = new Stage();

        //Block events to other windows
        cachewindow.initModality(Modality.APPLICATION_MODAL);
        cachewindow.setTitle("THE CACHE");
        cachewindow.setMinWidth(250);
        cachewindow.setScene(cacheScene);
        cachewindow.show();
    }
    public void loadFiles(boolean isStemming) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(loadInput+"myDictionary.ser"));
        FileInputStream fi2 = new FileInputStream(new File(loadInput+"myCache.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        ObjectInputStream zi = new ObjectInputStream(fi2);
        // Read objects
        indexer.m_Dictionary = (Map<String,TermDic>) oi.readObject();
        indexer.m_Cache=(Map<String,TermCache>) zi.readObject();
        /**List<File> selectedFiles=fc.showOpenMultipleDialog(null);
         if(selectedFiles!=null)
         {//https://www.youtube.com/watch?v=hNz8Xf4tMI4
         for(int i=0; i<selectedFiles.size();i++)
         {
         listview.getItems().add(selectedFiles.getAbsolutePath());
         }
         */
    }

    public void saveFiles() throws IOException
    {

        FileOutputStream fos = new FileOutputStream(saveInput+"\\myDictionary.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(indexer.m_Dictionary);
        oos.close();

        FileOutputStream fos1 = new FileOutputStream(saveInput+"\\myCache.ser");
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(indexer.m_Cache);
        oos1.close();

        //FileChooser fc=new FileChooser();
        //fc.setInitialDirectory((new File("C:\\")));
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.t));
        //File selectedFile=fc.showSaveDialog(null);
        /**List<File> selectedFiles=fc.showOpenMultipleDialog(null);
         if(selectedFiles!=null)
         {//https://www.youtube.com/watch?v=hNz8Xf4tMI4
         for(int i=0; i<selectedFiles.size();i++)
         {
         listview.getItems().add(selectedFiles.getAbsolutePath());
         }
         */
    }

    /** public void browser(){
     DirectoryChooser dc=new DirectoryChooser();
     dc.setInitialDirectory((new File("C:\\")));
     File selectedFile=dc.showDialog(null);
     s=selectedFile.getAbsolutePath();
     postingInput.setText(s);

     }
     public void browserPosting()
     {
     DirectoryChooser dc=new DirectoryChooser();
     dc.setInitialDirectory((new File("C:\\")));
     File selectedFile=dc.showDialog(null);
     s=selectedFile.getAbsolutePath();
     corpusInput.setText(s);
     }
     */
    /**
     * this method browse for path to load the dictionary and cache for the program?
     */
    public void browserLoadQueryF()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        s=selectedFile.getAbsolutePath();
        loadInput.setText(s);
    }

    /**
     * this method browse for path to save the results into a file
     */
    public void browserSaveQueryF()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        s=selectedFile.getAbsolutePath();
        saveInput.setText(s);
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
    /**
     *
     */

    public void browseQueryFileF() {

    }
    public void loadDictionaryF(){

    }
    public void browseDocF(){

    }

}