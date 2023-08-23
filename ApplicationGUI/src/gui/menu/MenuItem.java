package gui.menu;

public class MenuItem{
    private final String text;
    private final Menu subMenu;
    private final String instructions;

    public MenuItem(String text, String instructions, Menu subMenu){
        this.text = text;
        this.instructions = instructions;
        this.subMenu = subMenu;
    }

    public void PrintMenu(){
        System.out.println(this.text);
    }

    public String getInstructions(){
        return this.instructions;
    }

    @Override
    public String toString() {
        return this.text;
    }
}