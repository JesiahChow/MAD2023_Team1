package sg.edu.np.mad.madassignmentteam1;

public class Programme {
    private String title;
    private String description;
    private String category;

    // Constructor
    public Programme(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
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
    }}
