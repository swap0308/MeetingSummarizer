package src.main.java.com.example.MeetingSummarizer.Response.UIResponse;


public class UserResponse {

    private String id;
    private String summarizedText;
    private String title;
    private String actionItems;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSummarizedText() {
        return summarizedText;
    }

    public void setSummarizedText(String summary) {
        this.summarizedText = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionItems(){
        return actionItems;
    }

    public void setActionItems(String actionItems){
        this.actionItems = actionItems;
    }
}
