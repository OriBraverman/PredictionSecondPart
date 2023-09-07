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
        entities = new EntityTreeItem(worldDTO.getEntities());
        rules = new RuleTreeItem(worldDTO.getRules());
        terminationConditions = new TerminationConditionTreeItem(worldDTO.getTermination());
        setExpanded(true);
        this.getChildren().addAll(envVariables, entities, rules, terminationConditions);
    }
}
