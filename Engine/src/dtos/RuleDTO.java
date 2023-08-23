package dtos;

public class RuleDTO {
    private String name;
    private ActivationDTO activation;
    private int numberOfActions;
    private String[] actions;

    public RuleDTO(String name, ActivationDTO activation, int numberOfActions, String[] actions) {
        this.name = name;
        this.activation = activation;
        this.numberOfActions = numberOfActions;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public ActivationDTO getActivation() {
        return activation;
    }

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public String[] getActions() {
        return actions;
    }
}
