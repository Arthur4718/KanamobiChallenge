package devarthur.post.gitrepos.model;

/*
    This model class is responsible for creating a pattern to be used the
    recycler view adapter.

 */
public class GitRepo {

    //Member Variables
    private String repoName;
    private String repoDesc;
    private String forkCount;
    private String starCount;
    private String username;
    private String fullname;
    private String html_url;
    private String pull_url;
    private String languague;

    // You can check the JSON object structure at
    // http://www.jsonmate.com/permalink/5bc7b62a85da04b10bcf7bb5

    public GitRepo() {


    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public String getForkCount() {
        return forkCount;
    }

    public void setForkCount(String forkCount) {
        this.forkCount = forkCount;
    }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getLanguague() {
        return languague;
    }

    public void setLanguague(String languague) {
        this.languague = languague;
    }
}
