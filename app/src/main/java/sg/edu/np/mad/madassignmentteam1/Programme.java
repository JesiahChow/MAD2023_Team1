package sg.edu.np.mad.madassignmentteam1;

public class Programme {
    private String title;
    private String description;
    private String category;
    private String imageFileName;

    // Constructor
    public Programme(String title, String description, String category, String imageFileName) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageFileName = imageFileName;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
