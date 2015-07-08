import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> roomItems;          //stores the "inventory" of the room
    private String roomName;
    private int maxSize;
    private int size;
    private int protection;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String roomName) 
    {
        this.description = FileParser.getDescription(roomName);
        exits = new HashMap<String, Room>();
        this.roomName = roomName;
        roomItems = FileParser.getRoomItems(roomName);
        maxSize = 9999;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room nextRoom) 
    {
        exits.put(direction, nextRoom);
    }
    
    /**
     * Add an item to the room. Can be used in initialization and for the drop command.
     * @param item The item to be dropped
     */
    public void addItem(Item item) {
        roomItems.add(item);
    }
    
    /**
     * Remove an item from the room. Can be used during grab command.
     * @param item The item to be removed from room
     */
    public void removeItem(Item item) {
        roomItems.remove(item);
    }
    
    /** 
     * Returns the item specified if it exists.
     * If it doesn't exist throw an error.
     */
    public Item getItem(String itemName) {
        if(hasItem(itemName)) {
            for(Item item : roomItems) {
                if(item.getName().equals(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
    
    /**
     * Returns true if room has item. Returns false otherwise
     * 
     */
    public boolean hasItem(String itemName) {
        for(Item item : roomItems) {
            if(item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return description + "\n" + getItemString() + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    /**
     * Return a string that describes the items in a room.
     * @return A string "list" of items
     */
    private String getItemString() {
        String itemString = "";
        for(Item item : roomItems) {
            itemString += "There is " + item.getDescription();
        }
        return itemString;
    }
    

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getProtection() {
        return protection;
    }
    
    public void setProtection(int protection) {
        this.protection = protection;
    }
    
    public void updateProtection() {
        int totalRoomProtection = 0;
        for(Item item : roomItems) {
            totalRoomProtection += item.getProtection();
        }
        setProtection(totalRoomProtection);
    }
    
    public void updateSize() {
        int totalRoomSize = 0;
        for(Item item : roomItems) {
            totalRoomSize += item.getSize();
        }
        setSize(totalRoomSize);
    }
    
    public boolean canAdd(Item item) {
        if(item.getSize() + getSize() <= getMaxSize()) {
            return true;
        }else {
            return false;
        }
    }
}

