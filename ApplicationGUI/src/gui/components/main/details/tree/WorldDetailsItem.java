package gui.components.main.details.tree;

import dtos.world.WorldDTO;
import javafx.scene.control.TreeItem;

public class WorldDetailsItem extends TreeItem<String> {
    private WorldDTO worldDTO;
    private TreeItem<String> envVariables;
    private TreeItem<String> entities;
    private TreeItem<String> rules;
    private TreeItem<String> terminationConditions;
    private TreeItem<String> grid;
    private TreeItem<String> threadCount;

    public WorldDetailsItem(WorldDTO worldDTO) {
        super("Simulation Details:");
        this.worldDTO = worldDTO;
        envVariables = new EnvVariablesTreeItem(worldDTO.getEnvironment());
        entities = new EntitiesTreeItem(worldDTO.getEntities());
        rules = new RulesTreeItem(worldDTO.getRules());
        terminationConditions = new TerminationTreeItem(worldDTO.getTermination());
        grid = new TreeItem<>("Grid: Height: " + worldDTO.getGridHeight() + ", Width: " + worldDTO.getGridWidth());
        threadCount = new TreeItem<>("Thread Count: " + worldDTO.getThreadCount());
        setExpanded(true);
        this.getChildren().addAll(envVariables, entities, rules, terminationConditions, grid, threadCount);
    }
}
