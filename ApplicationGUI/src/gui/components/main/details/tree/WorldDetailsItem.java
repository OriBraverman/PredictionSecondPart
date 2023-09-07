package gui.components.main.details.tree;

import dtos.world.WorldDTO;
import javafx.scene.control.TreeItem;

public class WorldDetailsItem extends TreeItem<String> {
    private WorldDTO worldDTO;
    private TreeItem<String> envVariables;
    private TreeItem<String> entities;
    private TreeItem<String> rules;
    private TreeItem<String> terminationConditions;

    public WorldDetailsItem(WorldDTO worldDTO) {
        super("Simulation Details:");
        this.worldDTO = worldDTO;
        envVariables = new EnvVariablesTreeItem(worldDTO.getEnvironment());
        entities = new EntitiesTreeItem(worldDTO.getEntities());
        rules = new RulesTreeItem(worldDTO.getRules());
        terminationConditions = new TerminationTreeItem(worldDTO.getTermination());
        setExpanded(true);
        this.getChildren().addAll(envVariables, entities, rules, terminationConditions);
    }
}
