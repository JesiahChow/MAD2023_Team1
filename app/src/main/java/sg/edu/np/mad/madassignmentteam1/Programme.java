package sg.edu.np.mad.madassignmentteam1;

public class Programme {
    private String name;
    private String description;
    private String category;

    // Constructor
    public Programme(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
