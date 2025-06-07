package cs211.project.models;
public class Postable {
    public Postable() {
        // default constructor
    }
    protected String postDetails;

    public Postable(String postDetails) {
        this.postDetails = postDetails;
    }

    public String getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(String postDetails) {
        this.postDetails = postDetails;
    }

}
