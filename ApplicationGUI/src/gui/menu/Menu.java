package gui.menu;

import java.util.List;

public class Menu {
    private final String menuTitle = "Prediction System";
    private final String menuSubTitle = "Please choose an option:";
    private List<MenuItem> menuItems;
    public Menu(List<MenuItem> menuItems){
        this.menuItems = menuItems;
    }

    @Override
    public String toString(){
        String result = menuTitle + "\n" + menuSubTitle + "\n";
        for(int i = 0; i < menuItems.size(); i++){
            result += (i + 1) + ". " + menuItems.get(i).toString() + "\n";
        }
        return result;
    }

    public String getMenuItemInstructions(int index){
        return this.menuItems.get(index).getInstructions();
    }
}