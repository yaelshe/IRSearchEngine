

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

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This Class activate the Gui for Part B  of the search engine
 */
public class GuiPartB extends Application {

    Stage window;
    TextField queryFileInput;
    TextField loadInput;
    TextField saveInput;
    TextField queryInput;
    public static String pathToLoad = "";
    String pathToSave = "";
    String pathToQueryFile = "";
    String s = "";
    boolean load = false;
    long totalTime;
    ListView<String> singleQuery;
    ListView<String> multiQuery;

    /**
     * this function that launch the program
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method activate the window for the Gui
     *
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
        //dictionary = new ListView<>();

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
        browseButton4.setOnAction(e -> browserLoadQueryF());

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
        CheckBox doc5sentences = new CheckBox("Get 5 for doc?");
        GridPane.setConstraints(doc5sentences, 3, 1);

        //Stemming
        CheckBox stemmerCheck = new CheckBox("Stemming?");
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
        runQuery.setOnAction(e -> runTheQueryF(queryInput.getText(), doc5sentences.isSelected(), stemmerCheck.isSelected()));

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

        run2Query.setOnAction(e -> runQueryFiles(stemmerCheck.isSelected(), pathToQueryFile));
        //browse query file  button
        Button queryFileBrowse = new Button("browse");
        GridPane.setConstraints(queryFileBrowse, 2, 2);
        queryFileBrowse.setOnAction(e -> browserQueryFile());

        //RESET
        Button resetButton = new Button("RESET");
        GridPane.setConstraints(resetButton, 1, 4);
        resetButton.setOnAction(e -> resetFuncButton());
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
                saveFiles();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        //browse for save
        Button browseSaveLocation = new Button("browse");
        GridPane.setConstraints(browseSaveLocation, 2, 7);
        browseSaveLocation.setOnAction(e -> browserSaveQueryF());

        //Button browseLoadLocation = new Button("browse");
        //.setConstraints(browseLoadLocation, 2, 8);
        //browseLoadLocation.setOnAction(e-> browserLoadQueryF());

        saveInput = new TextField();
        saveInput.setPromptText("save path here");
        GridPane.setConstraints(saveInput, 1, 7);

        //Add everything to grid
        grid.getChildren().addAll(stemmerCheck, run2Query, loadDictionary, loadLabel, queryLabel, queryInput, doc5sentences, fileQueryLabel, queryFileInput, runQuery, queryFileBrowse
                , resetButton, resetLabel, saveButton, browseButton4, saveLabel, saveInput, loadInput, browseSaveLocation);
        Scene scene = new Scene(grid, 700, 300);
        window.setScene(scene);
        window.show();
    }
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)

    public void runTheQueryF(String query, boolean isDoc, boolean withStemm) {
        if (load) {
            if (query != null && !query.isEmpty()) {
                System.out.println("enterted run function");
                long startTime = System.currentTimeMillis();
                Searcher s;
                if (!isDoc) {//handle query inserted
                    s = new Searcher(query, withStemm);
                    long endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;
                    System.out.println(totalTime / 1000 / 60);
                    displaySingleQuery(query,Ranker.docsToReturn);
                }
                else
                {//handle docnumber inserted return 5 most important sentences
                    //System.out.println(tt.get("FBIS3-49"));
                    String ss = null;
                    boolean isValidDocId=false;
                    if (Parse.docPosting.containsKey(query))
                        isValidDocId=true;
                    else {
                        query=query.replaceAll(" ","");
                        query=" "+query+" ";
                        if (Parse.docPosting.containsKey(query))
                            isValidDocId=true;
                    }
                    if(isValidDocId){
                    try {
                        String pats = pathToLoad + "\\";
                        String pat2 = Parse.docPosting.get(query).getDirectoryPathDoc();
                        pat2=pats+ pat2.substring(pat2.indexOf("\\corpus"));
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
                else
                {
                    AlertBox.display("Alert", "Error: There is no " +query+" document!");
                }
                }
            } else {
                try {
                    AlertBox.display("Missing Input for query", "Error: no query has been written!");
                } catch (Exception e) {
                    System.out.println(" i caught the problem text field is empty");
                }
            }
        } else {
            AlertBox.display("Alert", "Error: no files and structures has been loaded!");
        }
    }

    public void browserQueryFile() {
        FileChooser fc = new FileChooser();
        //DirectoryChooser dc=new DirectoryChooser();
        fc.setInitialDirectory((new File("C:\\")));
        File selectedFile = fc.showOpenDialog(null);
        try {
            s = selectedFile.getAbsolutePath();
            queryFileInput.setText(s);
            pathToQueryFile = s;
        } catch (Exception e) {
        }

    }

    /**
     * this method shows the 5 sentences for a document that we found
     *
     * @param sentences - the sentences that we found
     */
    private void show5sentences(HashMap<String, Integer> sentences) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String str : sentences.keySet()) {
            sb = sb.append(i + ". Score:" + sentences.get(str) + "\n");
            sb = sb.append(str + "\n");
            i++;
        }
        AlertBox fiveSentences = new AlertBox();
        fiveSentences.display("5 Most important sentences in ", sb.toString());
    }

    private void show50resultDocs(List<String> results) {
        AlertBox boxResults = new AlertBox();
        StringBuilder sbResult = new StringBuilder();
        sbResult = sbResult.append("we found " + results.size() + " relevent documents" + "\n");
        sbResult = sbResult.append("Running query time: " + totalTime + "\n");
        for (String str : results) {
            sbResult = sbResult.append(str + "\n");
        }
        boxResults.display("Results:", sbResult.toString());
    }

    /***
     * method to save the results into a text file
     * @throws IOException
     */
    public void saveFiles() throws IOException {
        if (!pathToSave.equals("")) {
            AlertBox boxResults = new AlertBox();
            File docFile = new File(pathToSave + ".txt");
            BufferedWriter writerDoc = new BufferedWriter(new FileWriter(docFile));
            if (Searcher.allResults != null && !Searcher.allResults.isEmpty()) {
                String newLine = System.getProperty("line.separator");
                for (String str : Searcher.allResults) {
                    writerDoc.write(str);
                }
                writerDoc.close();

                boxResults.display("Alert:", "saved!");
            } else {
                boxResults.display("Alert:", "no results to save!");
            }
        } else {
            AlertBox boxResults = new AlertBox();
            boxResults.display("done", "no path to save inserted");
        }
    }

    /**
     * this method browse for path to load the dictionary and cache for the program?
     */
    public void browserLoadQueryF() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory((new File("C:\\")));
        File selectedFile = dc.showDialog(null);
        try {
            s = selectedFile.getAbsolutePath();
            loadInput.setText(s);
            pathToLoad = s;
        } catch (Exception e) {
        }
    }

    /**
     * this method browse for path to save the results into a file
     */
    public void browserSaveQueryF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.showSaveDialog(null);
        String path;
        //String filename = chooser.getSelectedFile().getName();
        try {
            path = chooser.getSelectedFile().getAbsolutePath();
            s = path;
            System.out.println(s);
            saveInput.setText(s);
            pathToSave = s;
        } catch (Exception e) {

        }
    }

    /**
     * This method run the Search engine for a set of queries from a file
     * @param stem-  to perform stemming or not
     * @param pathToFile- path to the file with the queries
     */
    private void runQueryFiles(boolean stem, String pathToFile) {
        if(load) {
            if (!pathToFile.equals("")) {
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
                    displayMultiQuery(Searcher.docsToDisplay);
                }
                AlertBox boxResults = new AlertBox();
                boxResults.display("Alert", "done with all the queries");
            } else {
                AlertBox boxResults = new AlertBox();
                boxResults.display("Alert", "no path to file with queries inserted");
            }
        }
        else{
            AlertBox.display("Alert", "Error: no files and structures has been loaded!");
        }
    }

    /**
     * This method load the data structure that are needed for the search engine accorrding to
     * with or without stemming process
     * @param isStemming- stem or not
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadDictionaryF(boolean isStemming) throws IOException, ClassNotFoundException {
        FileInputStream fi, fi2, file3;
        if (!pathToLoad.equals("")) {
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
            load = true;
            AlertBox.display("finish loading", "finish loading");
        } else {
            AlertBox boxResults = new AlertBox();
            boxResults.display("Alert", "no path to load inserted");
        }
    }

    private void resetFuncButton() {
        if(!load)
            AlertBox.display("Alert", "nothing was loaded yet!");
        else {
            if(queryFileInput.getText().equals("")&&queryInput.getText().equals(""))
                AlertBox.display("Alert", "No queries run yet!");
            else {
                load=false;
                queryFileInput.setText("");
                loadInput.setText("");
                saveInput.setText("");
                queryInput.setText("");
                pathToLoad = "";
                pathToQueryFile = "";
                s = "";
                if (Ranker.docsToReturn != null && !Ranker.docsToReturn.isEmpty())
                    Ranker.docsToReturn = null;
                if (Searcher.allResults != null && !Searcher.allResults.isEmpty())
                    Searcher.allResults = null;
                //delete the result file
                File file = new File(pathToSave + ".txt");
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }
                if (Searcher.docsToDisplay != null && !Searcher.docsToDisplay.isEmpty())
                    Searcher.docsToDisplay = null;
                if (Indexer.m_Dictionary != null && !Indexer.m_Dictionary.isEmpty())
                    Indexer.m_Dictionary = null;
                if (Indexer.m_Cache != null && !Indexer.m_Cache.isEmpty())
                    Indexer.m_Cache = null;
                if (Parse.docPosting != null && !Parse.docPosting.isEmpty())
                    Parse.docPosting = null;
                pathToSave = "";
            }
        }

    }

    public void displaySingleQuery(String query,List<String> results)
    {
    singleQuery =new ListView<>();
    //dictionary.setItems(getDictionaryTermGui());
    ObservableList<String> docsSingleQuery = FXCollections.observableArrayList();
        docsSingleQuery.add(("we found " + results.size() + " relevent documents"));
        docsSingleQuery.add(("the query: " + query));
        docsSingleQuery.add(("Running query time: " + totalTime/1000 +"sec"));
        for(int i=0; i<results.size();i++)
        {
            String str=results.get(i);
            docsSingleQuery.add(str);

        }
        singleQuery.setItems(docsSingleQuery);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(singleQuery);
        Scene singleResultsScene=new Scene(vBox);
        Stage resWindow = new Stage();
        resWindow.initModality(Modality.APPLICATION_MODAL);
        resWindow.setTitle("THE RESULTS:");
        resWindow.setMinWidth(250);
        resWindow.setScene(singleResultsScene);
        resWindow.show();
}
    public void displayMultiQuery(List<String> results)
    {
        multiQuery =new ListView<>();
        //dictionary.setItems(getDictionaryTermGui());
        ObservableList<String> docsMultiQuery = FXCollections.observableArrayList();
        docsMultiQuery.add(("RESULTS TO ALL THE QUERIES:"));
        docsMultiQuery.add(("Total running query time: " + totalTime/1000/60 +"min"));
        for(int i=0; i<results.size();i++)
        {
            String str=results.get(i);
            docsMultiQuery.add(str);

        }
        multiQuery.setItems(docsMultiQuery);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(multiQuery);
        Scene multiResultsScene=new Scene(vBox);
        Stage resMultiWindow = new Stage();
        resMultiWindow.initModality(Modality.APPLICATION_MODAL);
        resMultiWindow.setTitle("THE RESULTS:");
        resMultiWindow.setMinWidth(250);
        resMultiWindow.setScene(multiResultsScene);
        resMultiWindow.show();
    }
}