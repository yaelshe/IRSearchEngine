
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.nio.file.Files.deleteIfExists;

public class GUI extends Application {

    Stage window;
    //Scene dictionaryScene, cacheScene;
    ListView<String> dictionary;
    ListView <String>cache;
    private boolean doStemming=true;
    TextField postingInput;
    TextField loadInput;
    TextField saveInput;
    TextField corpusInput;
    String pathToSave="";
    String pathToLoad="";
    String s="";
    Map<String, TermDic> loadDictinary;
    Map<String, TermCache> loadCache;
    String pathToPosting="";
    String pathToCorpus="";
    ReadFile r;
    Parse P;
    Indexer indexer;
    long totalTime;
    boolean finish=false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Welcome to our search engine! ");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //corpus Label - constrains use (child, column, row)
        Label corpusLabel = new Label("Enter path to corpus:");
        GridPane.setConstraints(corpusLabel, 0, 0);

        //corpos path Input
        corpusInput = new TextField();
        corpusInput.setPromptText("corpus path here");
        GridPane.setConstraints(corpusInput, 1, 0);
        //browse button
        Button browseButton2 = new Button("browse");
        GridPane.setConstraints(browseButton2, 2, 0);
        browseButton2.setOnAction(e->browser());

        //posting Label
        Label postingLabel = new Label("Enter path to posting files:");
        GridPane.setConstraints(postingLabel, 0, 1);

        //posting path Input
        postingInput = new TextField();
        postingInput.setPromptText("posting path here");
        GridPane.setConstraints(postingInput, 1, 1);

        //browse button
        Button browseButton = new Button("browse");
        GridPane.setConstraints(browseButton, 2, 1);
        browseButton.setOnAction(e-> browserPosting());

        //Stemming
        Label stemmLabel = new Label("Do you want to preform Stemming?");
        GridPane.setConstraints(stemmLabel, 1, 2);
        //ToggleGroup stemming = new ToggleGroup();
        CheckBox stemmerCheck=new CheckBox("Stemming?");
        GridPane.setConstraints(stemmerCheck, 2, 2);

        //Start
        Button startButton = new Button("START");
        GridPane.setConstraints(startButton, 1, 3);
        startButton.setOnAction(e -> {
            try {
                StartButton(corpusInput.getText(), postingInput.getText(), stemmerCheck.isSelected());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        startButton.disableProperty().bind(Bindings.createBooleanBinding( () -> !((postingInput.getText()!=null && corpusInput.getText()!=null)),
                postingInput.textProperty(), corpusInput.textProperty()));

        //RESET
        Button resetButton = new Button("RESET");
        GridPane.setConstraints(resetButton, 2, 4);
        //reset Label
        Label resetLabel = new Label("To reset the posting and dictionary:");
        GridPane.setConstraints(resetLabel, 1, 4);
        resetButton.setOnAction(e->deleteReset());

        //Display cache
        Button cacheDisplayButton = new Button("Cache");
        GridPane.setConstraints(cacheDisplayButton, 2, 5);
        Label displayCacheLabel = new Label("To display the Cache:");
        GridPane.setConstraints(displayCacheLabel, 1, 5);
        cacheDisplayButton.setOnAction(e->displayCacheTable());

        //Display dictionary
        Button dictionaryDisplayButton = new Button("Dictionary");
        GridPane.setConstraints(dictionaryDisplayButton, 2, 6);
        Label displayDictionaryLabel = new Label("To display the Dictionary:");
        GridPane.setConstraints(displayDictionaryLabel, 1, 6);
        dictionaryDisplayButton.setOnAction(e->displayDictTable());

        //save the created files
        Button saveButton = new Button("SAVE");
        GridPane.setConstraints(saveButton, 4, 7);
        Label saveLabel = new Label("To save the files:");
        GridPane.setConstraints(saveLabel, 0, 7);
        saveButton.setOnAction(e -> {
            try {
                saveFiles();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button browseButton3 = new Button("browse");
        GridPane.setConstraints(browseButton3, 2, 7);
        browseButton3.setOnAction(e-> browserSave());

        Button browseButton4 = new Button("browse");
        GridPane.setConstraints(browseButton4, 2, 8);
        browseButton4.setOnAction(e-> browserLoad());


        //load the created files
        Button loadButton = new Button("LOAD");
        GridPane.setConstraints(loadButton, 4, 8);
        Label loadLabel = new Label("To load the files:");
        GridPane.setConstraints(loadLabel, 0, 8);
        loadButton.setOnAction(e -> {
            try {
                loadFiles();
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
        grid.getChildren().addAll(corpusLabel, corpusInput, postingLabel, postingInput,browseButton, startButton
                ,stemmerCheck,stemmLabel,resetButton,resetLabel,cacheDisplayButton,displayCacheLabel,saveButton,saveLabel,
                loadButton,loadLabel,browseButton2,dictionaryDisplayButton,displayDictionaryLabel,browseButton3,browseButton4,
                saveInput,loadInput);

        Scene scene = new Scene(grid, 500, 300);
        window.setScene(scene);
        window.show();
    }
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)

    public void StartButton (String s1, String s2, boolean box1) throws IOException
    {
        long startTime = System.currentTimeMillis();
        if(s1.length()>0&&s2.length()>0) {//the fields are filled

            pathToCorpus = s1;
            pathToPosting = s2;
            //change secene to alert and back to the main window to let write again
            //RadioButton chk = (RadioButton)box1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
            if (box1) {
                //  System.out.println("i checked yes");
                doStemming = true;
            } else {
                doStemming = false;
                // System.out.println("i checked no");
            }
            //r = new ReadFile(pathToCorpus);
            r = new ReadFile(pathToCorpus);
            P = new Parse(r.stopword, doStemming);
            //C:\Users\sheinbey\Downloads\corpus
            int i = 0;
            while (r.nextFile < r.filesPaths.size()) {
                // System.out.println(i);
                // Runtime instance=Runtime.getRuntime();
                //System.out.println((instance.totalMemory())/(1024*1024)+"fd");
                r.breakToFiles();
                //  System.out.println((instance.totalMemory())/(1024*1024)+"fdd");
                // P = new Parse(r.stopword,r.documents,doStemming);
                P.ParseAll(r.documents);
                // System.out.println("dont with parse for now");
                //  r.documents.clear();
                //r.allMatchesofdoc.clear();
                //indexer=new Indexer(P.m_terms,0,pathToPosting);
                try {
                    indexer = new Indexer(P.m_terms,i,pathToPosting,472525);//changed to i
                    P.m_terms.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                    //System.out.println("here dosnt");
                }
                i++;
                //indexer=new Indexer();//add the m_terms and the path for posting files
            }
            try {
                indexer.mergeAllFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getDictionaryTermGui();
            getCacheTermGui();
            long endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            System.out.println(totalTime / 1000 / 60);
            finish = true;
            finishData();
        }
        else {// the fields are missing
            //change scene to alert and back to the main window to let write again
            try {
                AlertBox.display("Missing Input", "Error: no paths had been written!");
            } catch (Exception e) {

            }
        }



    }
    public ObservableList<String> getDictionaryTermGui()
    {//get the items for the dictionary
        dictionary =new ListView<>();
        SortedSet<String> sortedKeys;
        ObservableList<String> termsDictionary= FXCollections.observableArrayList();
        Map<String,TermDic>dict;
        if(indexer!=null) {
            dict = indexer.m_Dictionary;//change to public for dictionary in indexer
            sortedKeys = new TreeSet<>(dict.keySet());
        }
        else {
            dict = loadDictinary;
            sortedKeys = new TreeSet<>(dict.keySet());
        }

        for(String str: sortedKeys)
        {
            String i=dict.get(str).getApperances()+"";
            termsDictionary.add("Term: "+dict.get(str).getName()+" , Frequency in our corpus: "+i);

        }
        dictionary.setItems(termsDictionary);
        return termsDictionary;
    }

    public ObservableList<String>getCacheTermGui()
    {//get the items for the dictionary
        //////////////////////
        cache =new ListView<>();
        SortedSet<String> sortedKeys;
        ObservableList<String> termsCache= FXCollections.observableArrayList();
        Map<String,TermCache>cac;
        if(indexer!=null) {
            cac = indexer.m_Cache;//change to public for dictionary in indexer
            sortedKeys = new TreeSet<>(cac.keySet());
        }
        else {
            cac = loadCache;
            sortedKeys = new TreeSet<>(cac.keySet());
        }

        for(String str: sortedKeys)
        {
            String i=cac.get(str).getFavDocs()+"";
            termsCache.add("Term: "+cac.get(str).getTerm()+" , Frequency in our corpus: "+i);

        }
        cache.setItems(termsCache);
        return termsCache;
    }


    public void displayDictTable()
    {//opens another window with the dictionary table display
        //if not working well try listView
        //https://stackoverflow.com/questions/27414689/a-java-advanced-text-logging-pane-for-large-output
        dictionary = new ListView<>();
        //dictionary.setItems(getDictionaryTermGui());
        ObservableList<String> termsDictionary= FXCollections.observableArrayList();
        Map<String,TermDic>dict;
        if(indexer!=null)
            dict= indexer.m_Dictionary;//change to public for dictionary in indexer
        else
            dict=loadDictinary;

        for(String str: dict.keySet())
        {
            String i=dict.get(str).getApperances()+"";
            termsDictionary.add("Term: "+dict.get(str).getName()+" , Frequency in our corpus: "+i);

        }
        dictionary.setItems(termsDictionary);

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
        cache = new ListView<>();
        //dictionary.setItems(getDictionaryTermGui());
        ObservableList<String> termCache= FXCollections.observableArrayList();
        Map<String,TermCache>cac;
        if(indexer!=null)
            cac= indexer.m_Cache;//change to public for dictionary in indexer
        else
            cac=loadCache;

        for(String str: cac.keySet())
        {
            String i=cac.get(str).getFavDocs()+"";
            termCache.add("Term: "+cac.get(str).getTerm()+" , Frequency in our corpus: "+i);
        }
        cache.setItems(termCache);


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
    public void loadFiles() throws IOException, ClassNotFoundException {
        FileInputStream fi;
        try {
            fi = new FileInputStream(new File(pathToLoad + "\\StemmyDictionary.ser"));
        }
        catch(FileNotFoundException e)
        {
            fi = new FileInputStream(new File(pathToLoad+"\\myDictionary.ser"));
        }
        FileInputStream fi2 = new FileInputStream(new File(pathToLoad+"\\myCache.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        ObjectInputStream zi = new ObjectInputStream(fi2);
        // Read objects
        Map<String,TermDic> temp=((Map<String,TermDic>) oi.readObject()) ;
        loadDictinary =temp;
        Map<String,TermCache> temp2=(Map<String,TermCache>) zi.readObject();
        loadCache = temp2;
        //dictionary.setItems(getDictionaryTermGui());
        //cache.setItems(getCacheTermGui());
        AlertBox.display("finish load", "finish load");
    }

    public void saveFiles() throws IOException
    {
        FileOutputStream fos,fos1;
        if(doStemming==true) {
            fos = new FileOutputStream(pathToSave + "\\StemmyDictionary.ser");
            fos1 = new FileOutputStream(pathToSave+"\\StemmyCache.ser");
        }
        else {
            fos = new FileOutputStream(pathToSave + "\\myDictionary.ser");
            fos1 = new FileOutputStream(pathToSave+"\\myCache.ser");
        }
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(indexer.m_Dictionary);
        oos.close();
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(indexer.m_Cache);
        oos1.close();
        FileOutputStream f1=new FileOutputStream(pathToSave + "\\docPosting.ser");
        ObjectOutputStream obe2 = new ObjectOutputStream(f1);
        obe2.writeObject(P.docPosting);
        obe2.close();
        AlertBox.display("finish save", "finish save");

    }

    public void browser(){
        try {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setInitialDirectory((new File("C:\\")));
            File selectedFile = dc.showDialog(null);
            s = selectedFile.getAbsolutePath();
            corpusInput.setText(s);
            pathToCorpus = s;
        }
        catch(Exception e) {

        }

    }
    public void browserPosting()
    {
        try {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setInitialDirectory((new File("C:\\")));
            File selectedFile = dc.showDialog(null);
            s = selectedFile.getAbsolutePath();
            postingInput.setText(s);
            pathToPosting = s + "\\";
        }
        catch(Exception e) {

        }
    }

    public void browserSave()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        s=selectedFile.getAbsolutePath();
        saveInput.setText(s);
        pathToSave=s;
    }

    public void browserLoad()
    {
        DirectoryChooser dc=new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile=dc.showDialog(null);
        s=selectedFile.getAbsolutePath();
        loadInput.setText(s);
        pathToLoad=s;
    }

    public void deleteReset() {
        //https://docs.oracle.com/javase/tutorial/essential/io/delete.html
        dictionary = null;
        cache = null;

        try {
            File file = new File(pathToSave+"/myCache.ser");
            File file2 = new File(pathToSave+"/myDictionary.ser");
            File file3= new File(pathToSave+"/StemmyDictionary.ser");

            try {
                if(file.exists())
                    file.delete();
                if(file2.exists())
                    file2.delete();
                if(file3.exists())
                    file3.delete();
            } catch (Exception e) {
            }
            try {
                //String directoryPath = pathToSave;
                String directoryPath = pathToSave;
                for (int i = 1; i <= 8; i++) {
                    File file5 = new File(directoryPath + "/" + i);
                    try {
                        //Deleting the directory recursively.
                        deleteDirectory(file5.getAbsolutePath());
                        System.out.println("Directory has been deleted recursively !");
                    } catch (IOException e) {
                        System.out.println("Problem occurs when deleting the directory : " + directoryPath);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

            }

        }
        catch(Exception e){}
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

        }
    }

    public void finishData()
    {//present all of the Data that is needed aout the program
        AlertBox.display("Program Information",
                "time of running:"+totalTime+"\n"+
                        "number of files indexed:4"+"472525"+"\n"+
                        "size of index in Bytes:1,232,817,036 bytes"+"\n"+
                        "size of cache in Bytes:82 bytes");

    }
    /*private static void delete(String filePath) throws IOException {
        try {
            delete(file);
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
                //throw new IOException();
            }
        }
        catch (Exception e)
        {

        }
    }
    */
}