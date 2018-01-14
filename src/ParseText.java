import sun.util.locale.ParseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParseText
{
    private static HashMap<String,String> m_StopWords;
    public HashMap<String,TermDic> m_terms;//**
    public HashMap<Integer,HashMap<String,TermDic>> m_Sentence;
    public int [] SentenceLength;
    public int SentenceId;
    public List<String> mysentence;
    private static Map<String,String> m_stem=new HashMap<>();// beforeStem,afterStem

    //private ArrayList<String> beforeTerms;
    //private Map<String,Document>m_documents;
    /* private final Regex r=new Regex(",$#!&=?<^>(){}\":;+|\\[\\]");
     private final HashMap<Character,Character> symbols=new HashMap<Character,Character>(){
     {put('&','x');put('#','x');put('$','x');put('!','x');put('?','x');put('*','x');put(':','x');put(';','x')
     ;put('\"','x');put('+','x');put('=','x');put('|','x');put('(','x');put(')','x');
     put('[','x');put(']','x');put('{','x');put('}','x');put('<','x');put('>','x');put('^','x');put('.','x');put('\'','x')
     ;}
     };
     */

    private boolean doStem=true;
    private Stemmer stemmer;
    int maxfrequency;

    Map <String,String> Months=new HashMap<String, String>(){{
        put("january","01"); put("february","02"); put("march","03");put("april","04");put("may","05");
        put("june","06");put("july","07");put("august","08");put("september","09");put("october","10");
        put("november","11");put("december","12");put("jan", "01");put("feb","02");put("mar","03");
        put("apr","04");put("may","05");put("jun","06");put("jul","07");put("aug","08");put("sep","09");
        put("oct","10");put("nov","11");put("dec","03");}};
    String currDoc;
    private String mytextdoc;

    /**
     *
     * @param m_StopWords -the stop words
     * @param text-the text was have to parse
     * @param docid-id of the doc
     * @param doStemming-weather to preform stemming(true) or not(false)
     */
    public ParseText(Map<String,String> m_StopWords, String text,String docid,boolean doStemming) {
        if(this.m_StopWords==null)
            this.m_StopWords = new HashMap<>(m_StopWords);//added new need tot check time to run
        this.m_terms = new HashMap<>();
        m_Sentence=new HashMap<>();
        this.mytextdoc=text;
        this.currDoc=docid;
        // m_documents=new HashMap<>(documents);
        doStem=true;
    }

    /**
     * function parsing all the sentence in the text
     */
    public void ParseAll()
    {
        List<String> l= new ArrayList<>();
        l.add("YIM");
        l.add("U.S");
        l.add(" Mr");
        l.add(" Ms");
        l.add(" Lr");
        l.add("Col");
        String []mysentence1=mytextdoc.split("\\. ");
        String s1,s2;
        s1="";
        this.mysentence= new ArrayList<>();
        for(int ii=0;ii<mysentence1.length;ii++){
            //System.out.println(ii+"."+mysentence1[ii]);
            if (mysentence1[ii].length()-3>0) {
                s1 = mysentence1[ii].substring(mysentence1[ii].length() - 3);
            }
            //System.out.println(ii+"."+s15);
            if(l.contains(s1)){
                if (ii+1<mysentence1.length) {
                    s2 = mysentence1[ii] + mysentence1[ii + 1];
                    mysentence.add(s2);
                    // System.out.println(ii+"."+mysentence.get(ii));
                    ii++;
                }
            }else {
                mysentence.add(mysentence1[ii]);
                // System.out.println(ii+"."+mysentence.get(ii));
            }
            //System.out.println(ii+"."+mysentence1[ii]);
        }
        //System.out.println("MYSENTENCESIZE:"+mysentence.size());
        //
        //List<String> lnew=new ArrayList<>();
        //String []mysentence=mytextdoc.split("\\. ");

        SentenceLength= new int[mysentence.size()];
        for(int i=0;i<mysentence.size();i++) {
            SentenceId=i+1;
            parseSentence(mysentence.get(i));
        }

    }

    /**
     *function that decodes each sentence that it receives from parseall, and devides it for terms, and then it puts it in m_terms and m_Sentence
     * @param text-the sentence
     */
    public void parseSentence(String text)
    {
        //String text=doc.getText().replaceAll("<(.*?)>","")
        //currDoc=mydoc;
        HashMap<String,TermDic> mytext_terms=new HashMap<>();
        int is=0;
        //System.out.println(is+"parse");
        is++;
        //doc.getText().replaceAll("-"," ");
        String []termsDoc=text.split("[\\s\\-]");
        //System.out.println(currDoc+"i split the text of it");
        int count;
        String curTerm;
        text=null;
        //doc.setText(null);
        //System.gc();
        for(int i=0;i<termsDoc.length;i++)
        {
            curTerm=termsDoc[i];
            //if(termsDoc[i].length()<1||(termsDoc[i].toUpperCase()).matches("^(?=.[A-Z])(?=.[0-9])[A-Z0-9]+$"))
            //   continue;
            count=0;
            //String SSS=termsDoc[i].replaceAll("-","");
            if((curTerm.length()==0)|| ((curTerm.trim().length()) == 0))
                //||SSS.trim().length()==0)
                continue;
            curTerm= removeExtra(curTerm);
            if (curTerm.length()>0)
            //&&)
            {
                if (!m_StopWords.containsKey(curTerm))
                {//maybe remove *??????
                    /** if(termsDoc[i].indexOf('-')!=-1&&termsDoc[i].length()>1)
                     {
                     termsDoc[i]=handleMakaf(termsDoc[i]);
                     addToTerm(termsDoc[i]);
                     continue;
                     }
                     */
                    /**if(curTerm.charAt(0)=='$')
                     {
                     while(curTerm.length()>0&&curTerm.charAt(0)=='$')
                     curTerm=curTerm.substring(1);
                     if(curTerm.equals("")||curTerm.length()==0){
                     addToTerm("dollar");;
                     continue;
                     }
                     addToTerm(curTerm+" dollar");
                     continue;
                     }
                     */
                    if(isNumber(curTerm))
                    {
                        curTerm = numbersHandler(curTerm);// numb 25-27,21/05/1991,29-word done
                        if(curTerm.length()>0)
                        {
                            if((!curTerm.contains("percent"))&&((i+1<termsDoc.length && ((removeExtra(termsDoc[i + 1])).toLowerCase()).equals("percentage"))))
                            {//check if percent
                                curTerm=curTerm+ " percent";
                                addToTermtext(curTerm,mytext_terms);
                                i++;
                                continue;
                            }
                            //addToTerm(curTerm);
                            else
                            {
                                String s1="";
                                String s3="";
                                if (i-1>=0)
                                {
                                    s1 = removeExtra(termsDoc[i -1]);
                                }
                                if (i+1<termsDoc.length)
                                {
                                    s3 = removeExtra(termsDoc[i + 1]);
                                }
                                if ((i+1<curTerm.length()||i-1>0)&&(isDate(s1, s3) && !curTerm.contains(".")))
                                {//216
                                    String s4="";
                                    if (i+2<termsDoc.length)
                                    {
                                        s4 = removeExtra(termsDoc[i + 2]);
                                    }
                                    String mydate = dateHandler(s1, curTerm, s3,s4 );
                                    if (mydate.length()>7)
                                    {
                                        if (Months.containsKey(s1))
                                        {
                                            i++;
                                        }else
                                        {
                                            i=i+2;
                                        }
                                    }else
                                    {
                                        if (!Months.containsKey(s1))
                                        {
                                            i++;
                                        }
                                    }
                                    //if(mydate.charAt(0)==' ')
                                    //mydate=mydate.substring(1);
                                    addToTermtext(curTerm,mytext_terms);// to update i ....
                                    continue;
                                }
                            }
                            //termsDoc[i]=termsDoc[i].replaceAll("[<>%^\\\\]","");
                            addToTermtext(curTerm,mytext_terms);
                            continue;
                        }
                    }//not a number

                    else if(curTerm.length()>0){
                        //if((curTerm.toUpperCase()).matches("^(?=.[A-Z])(?=.[0-9])[A-Z0-9]+$"))
                        //  continue;
                        curTerm=curTerm.replaceAll("[$%\\.// \\\\\\s]","");
                        if (curTerm.length()>0&&Character.isUpperCase(curTerm.charAt(0)))
                        {//check if the term capital letter up to phrase of 4 words.
                            String str1 = "", str2 = "", total = "";
                            if (i + 1 < termsDoc.length) {
                                str1 = termsDoc[i + 1];
                                if (i + 2 < termsDoc.length) {
                                    str2 = termsDoc[i + 2];
                                }
                            }
                            count=capitalTerm_text(curTerm, removeExtra(str1), removeExtra(str2),mytext_terms);
                            i = i + count - 1;
                            continue;
                        }
                        else
                        {
                            if(curTerm.length()>0) {
                                // termsDoc[i] = termsDoc[i].replaceAll("[\\s % \\.////]", "");
                                // termsDoc[i]=termsDoc[i].replaceAll(".")
                                if ((!m_StopWords.containsKey(curTerm.toLowerCase())) && curTerm.contains("\'"))
                                    curTerm = handleApostrophe(curTerm,mytext_terms);
                                addToTermtext(curTerm,mytext_terms);
                                continue;
                            }
                        }
                    }
                }
            }}
        m_Sentence.put(SentenceId,mytext_terms);
        //return mytext_terms;
    }
    /**
     * this method get the string of a term to insert to the tow terms Maps
     * perform the stemming if the boolean field is true
     * check for stop words
     * and finaly insert to terms
     *this function  counts all of the values which can help us afterwards, like (dfi and fij)
     * @param str - the word to be insert to terms
     */
    private void addToTermtext(String str,HashMap<String,TermDic> mytext_terms)
    {
        String strafter;
        //maxfrequency=m_documents.get(currDoc).getMax_tf();
        str=str.replaceAll("[\\']","");
        if((!str.equals(" "))&&str.length()>0&&!str.equals(null)) {
            str=str.toLowerCase();
            if (!m_StopWords.containsKey(str)) {
                //System.out.println(str);
                //System.out.println(str+" add to term");
                if(doStem)
                {
                    if(m_stem.containsKey(str))
                        str=m_stem.get(str);
                    else {
                        stemmer = new Stemmer();
                        stemmer.add(str.toCharArray(), str.length());
                        stemmer.stem();
                        strafter = stemmer.toString();
                        m_stem.put(str,strafter);
                        str=strafter;
                    }
                }
                // if(str.equals("illumin"))
                //   System.out.print("ilumin add to term");
                if (m_terms.containsKey(str)) {
                    m_terms.get(str).setApperances(1);//add 1 to total number of appernces in the text
                    if(!mytext_terms.containsKey(str)) {
                        m_terms.get(str).setNumOfDocs(1);
                    }


                } else
                {// first time term
                    TermDic newterm = new TermDic(str,1,0,1);
                    m_terms.put(str, newterm);
                    //m_documents.get(currDoc).max_tf = mytext_terms.get(str).docs.get(currDoc);
                    //newterm.docs.put(currDoc, 1);//update the list of docs the term is in

                }
                SentenceLength[SentenceId-1]++;
                if (mytext_terms.containsKey(str)) {
                    //think what have to update
                    mytext_terms.get(str).setApperances(1);//add 1 to total number of appernces in the entire Sentence
                } else
                {// first time term
                    TermDic newterm = new TermDic(str,1,SentenceId,1);
                    mytext_terms.put(str, newterm);
                    //m_documents.get(currDoc).max_tf = mytext_terms.get(str).docs.get(currDoc);
                    //newterm.docs.put(currDoc, 1);//update the list of docs the term is in

                }

            }
        }

    }
    /**
     * this function handle the first role we added which is to save words with the ' as prefix and also the hole word without it
     * @param str- the string that contain the '
     * @return the string without the '
     */
    private String handleApostrophe (String str,HashMap mytext_terms)
    {
        if (str.contains("\'")&&!m_StopWords.containsKey(str.toLowerCase())) {
            //checks if the word has an apostrphe in the middle and
            // save the word without it and the part before it
            int makaf = str.indexOf("\'");
            String part1 = str.substring(0, makaf);
            //System.out.println(part1 + "--6");
            part1=part1.replaceAll(" ","");
            addToTermtext(part1,mytext_terms);
            str = str.replace("\'", "");
            str=str.replaceAll(" ","");
        }
        return str;
    }

    public boolean isNumber(String str) {
        // a function to check if the term is a number
        // System.out.println(str);
        if (str.length() == 0 ) {
            return false;
        }
        if (Character.isAlphabetic(str.charAt(0)))
            return false;
        if (str.length() > 2 && str.substring(str.length() - 2).equals("th")) {
            str = str.substring(0, str.length() - 2);
        }
        if(str.endsWith("f")||str.endsWith("d"))
            return false;
        try {
            double d;
            if (str.length() > 1 && str.charAt(str.length() - 1) == '%') {
                d = Double.parseDouble(str.substring(0, str.length() - 1));
            }
            else
                d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    /**
     * this function checks if the two string that is given to her contains months names
     * @param s1-
     * @param s2-
     * @return if o ne of the string is a name of a month
     */
    private boolean isDate(String s1,String s2)
    {
        // a function to check if the term is part of a date
        if (Months.containsKey(s1.toLowerCase())|| Months.containsKey(s2.toLowerCase()))
        {
            return  true;
        }
        return false;
    }
    /**
     * this method handle the string which is a number to change number from 83.333333 to 83.33
     * remove th at the end
     * and the percent symbol %
     * @param s a string which is a number that needs to be altered
     * @return - the number in a string without th % or more then 2 digits after the dot
     */
    private String numbersHandler(String s) {
        //change number from 83.333333 to 83.33
        //String tt = "3.5555";
        boolean isPercent=false;
        StringBuilder percent=new StringBuilder();
        if (s.indexOf("th")!=-1)
        {
            //percent= percent.append("th");
            s=s.substring(0,s.length()-2);
        }
        else if (s.indexOf('%')!=-1)
        {
            isPercent=true;
            s=s.substring(0,s.length()-1);
        }
        if (s.indexOf(".")!=-1)
        {
            int y = s.indexOf(".");
            String ttt = (s.substring(y + 1));
            if (ttt.length() > 2) {
                s = s.substring(0, y + 3);
            }
        }
        if(isPercent)
            s=s+" percent";
        return  s;
    }
    /**
     * this function handle the strings to create a date in this Pattern 00/00/00 or 00/00 that we were obligated in the rules
     * @param s1-
     * @param s2-
     * @param s3-
     * @param s4-
     * @return the new string of a date in 00/00 or 00/00/00
     */
    public String dateHandler(String s1,String s2,String s3,String s4){//(termsDoc[i - 1], termsDoc[i], termsDoc[i + 1], termsDoc[i + 2])
        //change the format of the date in the text to the rule we have
        String day="";
        String month="";
        String year="";
        if (Months.containsKey(s3.toLowerCase()))
        {
            int intday=0;
            try {
                intday = Integer.parseInt(s2);
            }
            catch(NumberFormatException e){
                //System.out.println("s2 is "+s2);
            }
            day = cmpToDay(intday);
            month=Months.get(s3.toLowerCase());
            if (isNumber(s4))
            {
                if (s4.length() == 4)
                {
                    year = s4;
                    return day+"/"+month+"/"+year;
                }
                if (s4.length()==2)
                {
                    year = "19"+s4;
                    return day+"/"+month+"/"+year;
                }

            }
            return day+"/"+month;
        }
        else
        {
            month=Months.get(s1.toLowerCase());
            if (isNumber(s3)&& s3.length()==4)
            {
                year=s3;
                int intday = Integer.parseInt(s2);
                day = cmpToDay(intday);
                return day+"/"+month+"/"+year;
            }
            else
            {
                if (s2.length()<3)
                {
                    int intday = Integer.parseInt(s2);
                    day = cmpToDay(intday);
                    return day+"/"+month;
                }else
                {
                    year = s2;
                    return month+"/"+year;
                }
            }
        }
    }
    /**
     * this method change day structure to 00 structure
     * @param d
     * @return
     */
    private String cmpToDay(int d){
        String day = "";
        if (d<10)
        {
            day = "0"+Integer.toString(d);
        }
        else
        {
            day = Integer.toString(d);
        }
        return  day;
    }
    /**
     * this method remove all the parts from the words that we don't need to save
     * remove almost every Punctuation symbol
     * @param str- the term to be first handle from extra punctuation symbol
     * @return the altered term
     */
    public String removeExtra(String str)
    {
        str=str.replaceAll("[,#!&?*()<>^{}\\\":;+|\\[\\]\\s\\\\]","");
        //str=str.replaceAll([symbols.keySet().contains()],"");
        StringBuilder sdot= new StringBuilder();
        if (str.length()>0) {
            char last = str.charAt(str.length() - 1);
            char first = str.charAt(0);
            if (first=='%'||first=='\''||first=='.')
                str = str.substring(1);
            if(str.length()>0) {
                if (last=='\''||last=='.')
                    str = str.substring(0, str.length() - 1);
            }
            if (str.indexOf('.') != str.lastIndexOf('.')) {
                int dot = str.indexOf('.');
                sdot.append(str.substring(0, dot + 1));
                str = str.substring(dot + 1, str.length());
                str = str.replaceAll("\\.", "");
                sdot.append(str);
                str = sdot.toString();
            }
            //while(str.indexOf("-")==0)
            //  str=str.substring(1);
            //while(str.length()>0&&str.lastIndexOf('-')==str.length()-1)
            // str = str.substring(0, str.length()-1);
        }
        return str;
    }
    //***

    /**
     * this string handle words that appeared with a Capital letter and check for the words after it
     * to check if its a name or a phrase we need to save as a term together
     * @param s1- first word to have Capital letter
     * @param s2 - second word to check if also have capital letter
     *
     * @return the term we need to save if more then one word starts with capital letter
     */
    public  int capitalTerm_text(String s1, String s2,String s3,HashMap mytext_terms) {
        //ADD 4 STRING TO FUNC RETURN NUMBER OF WORDS IN phrase
        //List<String> phrase = new LinkedList<String>();
        if(s1.contains("\'"))
            s1=handleApostrophe(s1,mytext_terms);
        //s1=handleApostrophe(s1);
        StringBuilder phrase=new StringBuilder(s1);
        int count=1;
        //s1=s1.replaceAll(" ","");
        addToTermtext(s1,mytext_terms);
        // phrase.add(s1);
        if (s2.length()>0&&Character.isUpperCase(s2.charAt(0))&&!Character.isDigit(s2.charAt(0)))
        {
            if(s2.contains("\'"))
                s2=handleApostrophe(s2,mytext_terms);
            //s2=s2.replaceAll(" ","");
            addToTermtext(s2,mytext_terms);
            phrase=phrase.append(" "+s2);
            count++;
            /** if (s3.length()>0&&Character.isUpperCase(s3.charAt(0))&&!Character.isDigit(s3.charAt(0)))
             {
             if(s3.contains("\'"))
             s3=handleApostrophe(s3);
             //s3=handleApostrophe(s3);
             //s3=s3.replaceAll(" ","");
             phrase=phrase.append(" "+s3);
             addToTerm(s3);
             count++;
             }
             */
        }
        if(count>1) {
            addToTermtext(phrase.toString(), mytext_terms);
        }
        return count;
    }
//******was changed
}