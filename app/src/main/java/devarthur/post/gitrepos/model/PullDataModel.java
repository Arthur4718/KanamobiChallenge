package devarthur.post.gitrepos.model;


/*
    This model class is responsible for creating a pattern to be used with the
    recycler view adapter.

 */
public class PullDataModel {

    //Member Variables
    private String pullTitle;
    private String pullDescription;
    private String pullAvatarUrl;
    private String pullUsername;
    private String pullFullname;
    private String pullURL;


    public PullDataModel() {

    }

    public PullDataModel(String pullTitle, String pullDescription, String pullAvatarUrl, String pullUsername, String pullFullname, String pullURL) {
        this.pullTitle = pullTitle;
        this.pullDescription = pullDescription;
        this.pullAvatarUrl = pullAvatarUrl;
        this.pullUsername = pullUsername;
        this.pullFullname = pullFullname;
        this.pullURL = pullURL;
    }

    public String getPullURL() {
        return pullURL;
    }

    public void setPullURL(String pullURL) {
        this.pullURL = pullURL;
    }

    public String getPullTitle() {
        return pullTitle;
    }

    public void setPullTitle(String pullTitle) {
        this.pullTitle = pullTitle;
    }

    public String getPullDescription() {
        return pullDescription;
    }

    public void setPullDescription(String pullDescription) {
        this.pullDescription = pullDescription;
    }

    public String getPullAvatarUrl() {
        return pullAvatarUrl;
    }

    public void setPullAvatarUrl(String pullAvatarUrl) {
        this.pullAvatarUrl = pullAvatarUrl;
    }

    public String getPullUsername() {
        return pullUsername;
    }

    public void setPullUsername(String pullUsername) {
        this.pullUsername = pullUsername;
    }

    public String getPullFullname() {
        return pullFullname;
    }

    public void setPullFullname(String pullFullname) {
        this.pullFullname = pullFullname;
    }
}
