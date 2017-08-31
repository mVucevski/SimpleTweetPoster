package twitterbot;

public class Log {
    private String dateTime;
    private String action;
    private String content;

    public Log(String dateTime, String action, String content){
        this.dateTime = dateTime;
        this.action = action;
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("Date & Time: %s %nAction: %s %nMessage: %s %n",dateTime,action,content);
    }
}
