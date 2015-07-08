import java.util.HashMap;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Map gameMap;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        gameMap = new Map();
        parser = new Parser();
        createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        //Room kitchen, livingRoom, sunRoom, playersRoom, sistersRoom, parentsRoom;
        // create the rooms
        //First Floor
        gameMap.addRoom(new Room("kitchen"));
        gameMap.addRoom(new Room("livingRoom"));
        gameMap.addRoom(new Room("sunRoom"));
        //Second Floor
        gameMap.addRoom(new Room("playerRoom"));
        gameMap.addRoom(new Room("sistersRoom"));
        gameMap.addRoom(new Room("parentsRoom"));
        
        setExits("kitchen");
        setExits("livingRoom");
        setExits("sunRoom");
        setExits("playerRoom");
        setExits("sistersRoom");
        setExits("parentsRoom");
        
        gameMap.getRoom("playerRoom").setMaxSize(50);

        currentRoom = gameMap.getRoom("kitchen");  // start game outside
    }
    
    private void setExits(String roomName) {
        HashMap<String,String> exits = FileParser.getExits(roomName);
        Room nextRoom;
        for(String directionKey : exits.keySet()) {
            nextRoom = gameMap.getRoom(exits.get(directionKey));
            gameMap.getRoom(roomName).setExit(directionKey, nextRoom);
        }
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        
        //TODO Ask Name to create player
        
        player = new Player("Bob");
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the ZZ!");
        System.out.println("ZZ (Zombie-Zuul) is a new, incredibly awesome adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println("This may be null: " + currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case LOOK:
                look();
                break;
            
            case GO:
                goRoom(command);
                break;
            
            case INVENTORY:
                printInventory();
                break;
            
            case GRAB:
                grab(command);
                break;
                
            case DROP:
                drop(command);
                break;
            
            case EXAMINE:
                examine(command);
                break;
                
            case SLEEP:
                sleep();
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around a strange house.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /**
     * The command function for the player to take something from a room and add it to their inventory
     * If it is not visible or existing item. Print an error message.
     */
    private void grab(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Grab What?");
            return;
        }
        
        String itemName = command.getSecondWord();
        
        if(currentRoom.hasItem(itemName)) {
            player.addToInventory(currentRoom.getItem(itemName));
            currentRoom.removeItem(currentRoom.getItem(itemName));
            currentRoom.updateProtection();
        }else {
            System.out.println("That items isn't here");
        }
        
        
    }
    
    private void drop(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Drop What?");
            return;
        }
        
        String itemName = command.getSecondWord();
        
        if(player.hasItem(itemName)) {
            if(currentRoom.canAdd(player.getItem(itemName))){
                currentRoom.addItem(player.getItem(itemName));
                player.removeFromInventory(player.getItem(itemName));
                currentRoom.updateProtection();
            }else {
                System.out.println("There isn't enough room in here to drop that!");
            }
        }else {
            System.out.println("You don't have that item to drop");
        }
    }
    
    private void look() {
        System.out.println(currentRoom.getLongDescription());
    }
    
    private void printInventory() {
        if(player.getInventory() != null) {
            System.out.println("You have a/an:");
            for(Item item : player.getInventory()) {
                System.out.println("    " + item.getName());
            }
        }else {
            System.out.println("You don't have anything");
        }
    }
    
    /**
     * The function that prints the examine text. This command allows a closer look at items.
     * Can also be used as a read command.
     */
    private void examine(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Examine What?");
            return;
        }
        
        String itemName = command.getSecondWord();
        
        if(currentRoom.hasItem(itemName)) {
            System.out.println(currentRoom.getItem(itemName).getExamineText());
        }else if (player.hasItem(itemName)) {        //The player may be talking about item in their inventory
            System.out.println(player.getItem(itemName).getExamineText());
        }else {
            System.out.println("That item isn't here to be examined!");
        }
    }
    
    private void sleep() {
        if(!currentRoom.getRoomName().equals("playerRoom")) {
            System.out.println("You can't sleep here! It's discusting! Try sleeping when you are ready, and when you are in the room with the fortified door.");
        }else {
            currentRoom.updateProtection();
            if(currentRoom.getProtection() < 100) {
                printDeath();
            }else {
                printLife();
            }
        }
    }
    
    private void printLife() {
        //Add easter egg here
        System.out.println("Congratulations! You have won! \n Your fortifications have lasted you the night. \n I just hope you can make it another night too.");
        System.out.println("Type quit to quit.");
    }
    
    private void printDeath() {
        System.out.println("As you drift off to sleep, you get the horrible feeling that you might not wake up. \n In the middle of your slumber you wake up to loud bangs on your door. \n Soon enough a flood of zombies enter and eat you alive. ");
        System.out.println("Type quit to quit.");
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
