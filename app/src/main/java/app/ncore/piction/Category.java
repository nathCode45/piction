package app.ncore.piction;



import java.util.ArrayList;
import java.util.Collections;

public class Category {

    private ArrayList<String> words;

    private String categoryName;
    private int iconID;
    private String description;
    private int position;
    private int easyWordsRes;
    private int hardWordsRes;
    private int mediumWordsRes;

    public void setWords(ArrayList<String> words){
        this.words = words;
    }

    public int getEasyWordsRes() {
        return easyWordsRes;
    }

    public int getHardWordsRes() {
        return hardWordsRes;
    }

    public int getMediumWordsRes() {
        return mediumWordsRes;
    }

    public String yield(){
        String result = words.get(position);
        if(position>=(words.size()-1)){
            position = 0;
        }else{
            position++;
        }
        return result;
    }

    public Category(int easyWordsRes, int mediumWordsRes, int hardWordsRes, String categoryName, int iconID, String description) {
        this.categoryName = categoryName;
        this.iconID = iconID;
        this.description = description;
        this.easyWordsRes= easyWordsRes;
        this.mediumWordsRes = mediumWordsRes;
        this.hardWordsRes = hardWordsRes;
        position = 0;
    }


    public void shuffle(){
        Collections.shuffle(words);
        Collections.shuffle(words);
    }

    public String getDescription() {
        return description;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public int getIconID() {
        return iconID;
    }


}
