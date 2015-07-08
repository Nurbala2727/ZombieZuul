import java.util.ArrayList;

/**
 * This is the class that deals with the player.
 * Deals with points, turns and player inventory.
 * @param name
 **/

public class Player{
    private String name = "";
    private int points = 0;
    private ArrayList<Item> inventory;
    private Room location;

    public Player(String name) {
       setName(name);
       inventory = new ArrayList<Item>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item) {
        inventory.add(item);
    }

    public void removeFromInventory(Item item) {
        inventory.remove(item); //doubt that this works
    }
    
    public Item getItem(String itemName) {
        if(hasItem(itemName)) {
            for(Item item : inventory) {
                if(item.getName().equals(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public boolean hasItem(String itemName) {
        for(Item item : inventory) {
            if(item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    

    public Room getLocation(){
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }
}
