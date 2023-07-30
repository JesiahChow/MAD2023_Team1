package sg.edu.np.mad.madassignmentteam1;

import org.json.JSONObject;

import java.util.List;

public class Programme {

    private String name, type, description, body, dataset, categoryDescription;
    private List<String> tags;
    private Double rating;
    private Address address;
    private List<ImageInfo> images, thumbnails;
    private List<JSONObject> reviews;


    // Constructor
    public Programme(String name, String type, List<String> tags, String description, String body, Address address, String dataset, String categoryDescription, Double rating, List<ImageInfo> images, List<ImageInfo> thumbnails, List<JSONObject> reviews) {
        this.name = name;
        this.type = type;
        this.tags = tags;
        this.description = description;
        this.body = body;
        this.address = address;
        this.dataset = dataset;
        this.categoryDescription = categoryDescription;
        this.rating = rating;
        this.images = images;
        this.thumbnails = thumbnails;
        this.reviews=reviews;
    }
    public int getReviews()
    {
        return this.reviews.size();
    }
    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<ImageInfo> getImages() {
        return images;
    }


    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }

    public List<ImageInfo> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<ImageInfo> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String[] getImagesArray()
    {
        String[] myArray = new String[images.size()];
       for (int a = 0; a<images.size(); a++)
       {
           if(images.get(a).getUuid().isEmpty() || images.get(a).getUuid().matches(""))
           {
               myArray[a] = images.get(a).getUrl();
           }
           else{
               myArray[a] = images.get(a).getUuid();
           }

       }
       return myArray;
    }
}

class Address {
    private String block;
    private String streetName;
    private String floorNumber;
    private String unitNumber;
    private String buildingName;
    private String postalCode;

    // Constructor, Getters, and Setters

    public String getBlock() {
        return block;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString()
    {
        return getBlock()+" "+getStreetName()+" "+getFloorNumber()+" "+getUnitNumber()+" "+getBuildingName()+" "+getPostalCode();
    }
}

class ImageInfo {

    private String uuid;
    private String url;
    private String libraryUuid;
    private String primaryFileMediumUuid;

    @Override
    public String toString() {
        return "ImageInfo{" +
                "uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", libraryUuid='" + libraryUuid + '\'' +
                ", primaryFileMediumUuid='" + primaryFileMediumUuid + '\'' +
                '}';
    }
    // Constructor, Getters, and Setters
    public String getUuid() {
        return uuid;
    }

    public String getUrl() {
        return url;
    }

    public String getLibraryUuid() {
        return libraryUuid;
    }

    public String getPrimaryFileMediumUuid() {
        return primaryFileMediumUuid;
    }

}

