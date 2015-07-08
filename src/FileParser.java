import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
/**
 * Room parser class to aid in the creation of rooms. Cleans up code by allowing for descriptions and such to be parsed from a text file rather than declared.
 */
public class FileParser
{
    // instance variables - replace the example below with your own
    private static String descriptionFileName = "descriptions.txt";
    private static String exitFileName = "exits.txt";
    private static String itemFileName = "items.txt";
    private static String roomItemsFileName = "roomItems.txt";
    
    /**
     * Constructor for objects of class RoomParser
     */
    public FileParser() {
        //Pff IDK
    }
    
    public static String getDescription(String roomName) {
        String description = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(descriptionFileName));
            boolean isReading = false;
            String line;
            while((line = reader.readLine()) != null) {
                if(line.equals(roomName)) {
                    isReading = true;
                }else if(line.equals("END" + roomName)) {
                    isReading = false;
                    break;
                }else if(isReading) {
                    description += line + "\n";
                }
            }
            return description;
        }catch (Exception e) {
            System.err.format("Exception occurred when trying to read descriptions.txt");
            e.printStackTrace();
            return null;
        }
    }
    
    public static HashMap<String,String> getExits(String roomName) {
        String direction;
        String nextRoomName;
        HashMap<String,String> exits = new HashMap<String, String>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(exitFileName));
            boolean isReading = false;
            String line;
            
            while((line = reader.readLine()) != null) {
                if(line.equals(roomName)) {
                    isReading = true;
                }else if(line.equals("END" + roomName)) {
                    isReading = false;
                    break;
                }else if(isReading) {
                    List<String> splitLine = Arrays.asList(line.split(",",2));
                    direction = splitLine.get(0);
                    nextRoomName = splitLine.get(1);
                    exits.put(direction,nextRoomName);
                }
            }
            return exits;
        }catch (Exception e) {
            System.err.format("Exception occurred when trying to read descriptions.txt");
            e.printStackTrace();
            return exits;
        }
        
    }
    
    public static Item createItem(String itemName) {
        //Parsing item from file
        String description = "";
        String examineText = "";
        int size = 0;
        int protection = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(itemFileName));
            boolean inItemBlock = false;
            boolean inDescriptionBlock = false;
            boolean inExamineBlock = false;
            String line;
            
            while((line = reader.readLine()) != null) {
                //The following is just navigational things to find headers and say where we are.
                if(line.equals("ITEM" + itemName)) {
                    inItemBlock = true;
                }else if(line.equals("END" + itemName)) {
                    inItemBlock = false;
                    break;
                }else if(inItemBlock && line.equals("DESCRIPTION")) {
                    inDescriptionBlock = true;
                }else if(inItemBlock && line.equals("ENDDESCRIPTION")) {
                    inDescriptionBlock = false;
                }else if(inItemBlock && line.equals("EX")) {
                    inExamineBlock = true;
                }else if(inItemBlock && line.equals("ENDEX")) {
                    inExamineBlock = false;
                }
                //Now for the actual parsing
                if(inDescriptionBlock && !line.equals("DESCRIPTION")) {
                    description += line + "\n";
                }else if(inExamineBlock && !line.equals("EX")) {
                    examineText += line + "\n";
                }else if(line.contains("SIZE")) {
                    List<String> splitLine = Arrays.asList(line.split("SIZE "));
                    size = Integer.valueOf(splitLine.get(1));
                }else if(line.contains("PROTECT")) {
                    List<String> splitLine = Arrays.asList(line.split("PROTECT "));
                    protection = Integer.valueOf(splitLine.get(1));
                }
            }
            //Create item here
            return new Item(itemName, description, examineText, size, protection);
            
        }catch (Exception e) {
            e.printStackTrace();
            return new Item();
        }
    }
    
    //Now need a parser for the roomItem file
    public static ArrayList<Item> getRoomItems(String roomName) {
        ArrayList<Item> roomItems = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(roomItemsFileName));
            String line;
            boolean inRoomBlock = false;
        
            while((line = reader.readLine()) != null) {
                if(line.equals("START" + roomName)) {
                    inRoomBlock = true;
                }else if(line.equals("END" + roomName)) {
                    inRoomBlock = false;
                }else if(inRoomBlock == true) {
                    roomItems.add(createItem(line));
                }
            }
            return roomItems;
               
        }catch (Exception e) {
            System.out.println("We had an issue!");
            return roomItems;
        }        
        //Method in room class : setItems. . .a for loop that adds to room inventory from this list
        
    }
}
