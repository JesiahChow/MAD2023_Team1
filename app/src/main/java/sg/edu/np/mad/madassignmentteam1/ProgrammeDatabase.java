package sg.edu.np.mad.madassignmentteam1;


import java.util.ArrayList;
import java.util.List;

public class ProgrammeDatabase {
    private List<Programme> programmeList;

    public ProgrammeDatabase() {
        programmeList = new ArrayList<>();

        // Create an array to store the program details
        String[][] programDetails = {
                {"Culinary Delights Tour", "Embark on a guided tour through local markets, taste authentic dishes, and learn about the culinary traditions of the region.", "Food & Dining", "culinary_tour"},
                {"Cooking Class with a Local Chef", "Join a hands-on cooking class led by a skilled local chef, where you'll learn to prepare traditional recipes using fresh local ingredients.", "Food & Dining", "cooking_class"},
                {"Mountain Hiking Expedition", "Embark on a thrilling hiking adventure, trekking through scenic trails, and reaching breathtaking viewpoints in the mountains.", "Adventure & Outdoor Activities", "hiking"},
                {"Snorkeling Adventure in Coral Reef", "Dive into the crystal-clear waters and explore vibrant coral reefs, encountering diverse marine life in their natural habitat.", "Adventure & Outdoor Activities", "snorkeling"},
                {"Full-Day Pass to Adventure World", "Enjoy a full day of excitement and fun at Adventure World, a thrilling theme park with roller coasters, water slides, and live shows.", "Theme Parks & Attractions", "sentosa"},
                {"Historical Walking Tour of Old Town", "Take a guided walking tour through the historical streets of the Old Town, discovering its fascinating stories and landmarks.", "Theme Parks & Attractions", "heritagesites"},
                {"City Highlights Bus Tour", "Hop on a comfortable sightseeing bus and explore the city's iconic landmarks, learning about its history and culture from an expert guide.", "Sightseeing & Tours", "bus_tour"},
                {"Day Trip to Spectacular Waterfalls", "Embark on a day trip to visit breathtaking waterfalls, immersing yourself in nature and witnessing the power and beauty of cascading water.", "Sightseeing & Tours", "waterfall"},
                {"Broadway Musical: The Magical Journey", "Experience the enchantment of a captivating Broadway musical, featuring talented performers, stunning sets, and memorable songs.", "Shows & Entertainment", "musical"},
                {"Cultural Dance Performance: Rhythms of Tradition", "Attend a vibrant cultural dance performance, showcasing traditional dances, music, and costumes from the region.", "Shows & Entertainment", "dance"}
        };



// Iterate through the program details array and create Programme objects
        for (String[] details : programDetails) {
            String title = details[0];
            String description = details[1];
            String category = details[2];
            String imageFileName = details[3];


            Programme programme = new Programme(title, description, category, imageFileName);
            programmeList.add(programme);
        }

    }

    public List<Programme> getProgrammes() {
        return programmeList;
    }


}

