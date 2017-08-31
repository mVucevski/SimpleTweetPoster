package twitterbot;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfig {

    public static String APIKey;
    public static String APISecret;
    public static String Token;
    public static String TokenSecret;
    public static ConfigurationBuilder cb;
    public static TwitterFactory tf;
    public static Twitter twitter;
    public static boolean validConfig;

    public static boolean saveTwitterConfig(String APIKey, String APISecret, String Token, String TokenSecret){
        validConfig = true;
        TwitterConfig.APIKey = APIKey;
        TwitterConfig.APISecret = APISecret;
        TwitterConfig.Token = Token;
        TwitterConfig.TokenSecret = TokenSecret;

        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConfig.APIKey)
                .setOAuthConsumerSecret(TwitterConfig.APISecret)
                .setOAuthAccessToken(TwitterConfig.Token)
                .setOAuthAccessTokenSecret(TwitterConfig.TokenSecret);
         tf = new TwitterFactory(cb.build());
         twitter = tf.getInstance();

        try{
            twitter.getId();
        }catch(Exception ex){
            System.out.println("FAIL");
            validConfig = false;
        }
        Controller.configConnected.setValue(validConfig);
        return validConfig;
    }

    public static void postTwitter(String tweet) throws TwitterException {
        Status status = twitter.updateStatus(tweet);
    }

}
