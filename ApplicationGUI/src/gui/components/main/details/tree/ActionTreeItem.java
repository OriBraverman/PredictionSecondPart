package gui.components.main.details.tree;

import dtos.world.action.*;
import javafx.scene.control.TreeItem;

public class ActionTreeItem extends TreeItem<String> implements OpenableItem {
    private AbstructActionDTO action;

    public ActionTreeItem(AbstructActionDTO action) {
        super(action.getType().toLowerCase() + " action");
        this.action = action;
    }

    @Override
    public TreeItem<String> getFullView() {
        switch (action.getType().toLowerCase()) {
            case "increase":
                if (action instanceof IncreaseActionDTO)
                    return getFullView((IncreaseActionDTO) action);
                break;
            case "decrease":
                if (action instanceof DecreaseActionDTO)
                    return getFullView((DecreaseActionDTO) action);
                break;
            case "calculation":
                if (action instanceof CalculationActionDTO)
                    return getFullView((CalculationActionDTO) action);
                break;
            case "set":
                if (action instanceof SetActionDTO)
                    return getFullView((SetActionDTO) action);
                break;
            case "kill":
                if (action instanceof KillActionDTO)
                    return getFullView((KillActionDTO) action);
                break;
            case "replace":
                if (action instanceof ReplaceActionDTO)
                    return getFullView((ReplaceActionDTO) action);
                break;
            case "condition":
                if (action instanceof ConditionActionDTO)
                    return getFullView((ConditionActionDTO) action);
                break;
            case "proximity":
                if (action instanceof ProximityActionDTO)
                    return getFullView((ProximityActionDTO) action);
                break;
        }
        return null;
    }

    private TreeItem<String> getFullView(IncreaseActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Increase Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(DecreaseActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Decrease Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(CalculationActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Calculation Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(SetActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Set Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(KillActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Kill Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(ReplaceActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Replace Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(ConditionActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Condition Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }

    private TreeItem<String> getFullView(ProximityActionDTO action) {
        TreeItem<String> root = new TreeItem<>("Proximity Action Details:");
        root.getChildren().add(new TreeItem<>("Entity: " + action.getPrimatyEntity().getName()));
        setExpanded(true);
        return root;
    }
}
