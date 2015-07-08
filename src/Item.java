/**
 * The item class for things that are in a room and can be in an inventory
 * 
 */

public class Item {
    private String name;
    private String description;
    private String examineText;
    private int size;
    private int protection = 666;
    //Probably add more fields. Don't really know what more I want to do with it
    public Item() {
        System.out.println("For some reason there is an empty item");
    };
    
    public Item(String name, String description, int size, int protection) {
        setName(name);
        setDescription(description);
        setExamineText(description);
        setSize(size);
        setProtection(protection);
    }
    
    public Item(String name, String description, String examineText, int size, int protection) {
        setName(name);
        setDescription(description);
        setExamineText(examineText);
        setSize(size);
        setProtection(protection);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setExamineText(String examineText) {
        this.examineText = examineText;
    }
    
    public String getExamineText() {
        return examineText;
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
}