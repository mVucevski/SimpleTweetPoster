package twitterbot;

import java.util.ArrayList;

public class Quote {
    private String text;
    private int parts;
    private ArrayList<String> tweets;
    private final int tweetlenght = 135;

    public Quote(String text){
        update(text);
    }

    private int calculateParts(){
        return 1 + (text.length() / 140);
    }

    private ArrayList<String> constructTweet() {
        ArrayList<String> list = new ArrayList<>();
        if (parts == 1) list.add(text);
        else {
            int count = 1;
            int startIndex = tweetlenght;
            int endIndex = 0;

            while (count <= parts) {
                if(text.length()-startIndex>0) {
                    for (int i = startIndex; i > endIndex; i--) {
                        if (text.charAt(i) == ' ') {
                            list.add(String.format("%s (%d/%d)", text.substring(endIndex, i), count, parts));
                            endIndex = i+1;
                            startIndex = i + tweetlenght;
                            break;
                        }
                    }
                }
                else{
                    list.add(String.format("%s (%d/%d)", text.substring(endIndex), count, parts));
                }
            count++;
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return text;
    }

    public int getParts() {
        return parts;
    }

    public ArrayList<String> getTweets() {
        return tweets;
    }

    public int getTweetlenght() {
        return text.length();
    }

    public void setText(String text) {
        update(text);
    }

    public void update(String text){
        this.text = text;
        parts = calculateParts();
        tweets = constructTweet();
    }
}
