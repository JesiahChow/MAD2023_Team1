package sg.edu.np.mad.madassignmentteam1;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProgrammeDatabase {
    private List<Programme> programmeList;
    private List<Programme> sortedProgrammes;


    public ProgrammeDatabase() {
        programmeList = new ArrayList<>();

        // Initialize and add programmes to the list
        //Tourist targeted
        // Create an array to store the program details
        String[][] programDetails = {
                {"Culinary Delights Tour", "Embark on a guided tour through local markets, taste authentic dishes, and learn about the culinary traditions of the region.", "Food and Dining"},
                {"Cooking Class with a Local Chef", "Join a hands-on cooking class led by a skilled local chef, where you'll learn to prepare traditional recipes using fresh local ingredients.", "Food and Dining"},
                {"Mountain Hiking Expedition", "Embark on a thrilling hiking adventure, trekking through scenic trails, and reaching breathtaking viewpoints in the mountains.", "Adventure and Outdoor Activities"},
                {"Snorkeling Adventure in Coral Reef", "Dive into the crystal-clear waters and explore vibrant coral reefs, encountering diverse marine life in their natural habitat.", "Adventure and Outdoor Activities"},
                {"Full-Day Pass to Adventure World", "Enjoy a full day of excitement and fun at Adventure World, a thrilling theme park with roller coasters, water slides, and live shows.", "Theme Parks and Attractions"},
                {"Historical Walking Tour of Old Town", "Take a guided walking tour through the historical streets of the Old Town, discovering its fascinating stories and landmarks.", "Theme Parks and Attractions"},
                {"City Highlights Bus Tour", "Hop on a comfortable sightseeing bus and explore the city's iconic landmarks, learning about its history and culture from an expert guide.", "Sightseeing and Day Tours"},
                {"Day Trip to Spectacular Waterfalls", "Embark on a day trip to visit breathtaking waterfalls, immersing yourself in nature and witnessing the power and beauty of cascading water.", "Sightseeing and Day Tours"},
                {"Broadway Musical: The Magical Journey", "Experience the enchantment of a captivating Broadway musical, featuring talented performers, stunning sets, and memorable songs.", "Shows and Entertainment"},
                {"Cultural Dance Performance: Rhythms of Tradition", "Attend a vibrant cultural dance performance, showcasing traditional dances, music, and costumes from the region.", "Shows and Entertainment"}
        };

// Create a list to store the Programme objects
        List<Programme> programmeList = new ArrayList<>();

// Iterate through the program details array and create Programme objects
        for (String[] details : programDetails) {
            Programme programme = new Programme(details[0], details[1], details[2]);
            programmeList.add(programme);
        }

        sortedProgrammes = new ArrayList<>(programmeList);
        Collections.sort(sortedProgrammes, (p1, p2) -> p1.getCategory().compareToIgnoreCase(p2.getCategory()));
    }

    public List<Programme> getProgrammes() {
        return programmeList;
    }

    public List<Programme> getSortedProgrammes() {
        return sortedProgrammes;
    }
    // Other methods for data manipulation, filtering, etc.

}

