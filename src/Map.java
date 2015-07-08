import java.util.HashMap;
/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map
{
    private HashMap<String, Room> roomIndex;
    
    public Map() {
        roomIndex = new HashMap<>();
    }
    
    public Room getRoom(String roomName) {
        return roomIndex.get(roomName);
    }
    
    public void addRoom(Room room) {
        roomIndex.put(room.getRoomName(), room);
    }
}
