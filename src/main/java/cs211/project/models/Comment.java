package cs211.project.models;

public class Comment extends Postable {
    private int id;
    private int postId;
    private String commentDetail;
    private String usernameComment;

    public Comment(String postDetails) {
        super(postDetails);
    }
    public Comment(int id, int postId, String commentDetail, String usernameComment) {
        this.id = id;
        this.postId = postId;
        this.commentDetail = commentDetail;
        this.usernameComment = usernameComment;
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getCommentDetail() {
        return commentDetail;
    }
    public String getUsernameComment() {
        return usernameComment;
    }
}
