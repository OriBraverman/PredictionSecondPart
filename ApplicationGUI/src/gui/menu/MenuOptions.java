package gui.menu;

import java.util.HashMap;
import java.util.Map;

public enum MenuOptions {
    LOAD_WORLD_XML(1),
    SHOW_SIMULATION_DETAILS(2),
    ACTIVATE_SIMULATION(3),
    FULL_PAST_SIMULATION_DETAILS(4),
    EXIT(5);

    private int id;
    private final static Map<Integer, MenuOptions> idToChoice = new HashMap<>();


    MenuOptions(int choiceId){
        this.id = choiceId;

    }
    static {
        for(MenuOptions option : MenuOptions.values()){
            idToChoice.put(option.id, option);
        }
    }

    public static MenuOptions getChoice(Integer choice){
        return idToChoice.get(choice);
    }
}

