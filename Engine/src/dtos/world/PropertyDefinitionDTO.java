package dtos.world;

public class PropertyDefinitionDTO {
    private final String name;
    private final String type;
    private final String from;
    private final String to;
    private final String defaultVal;
    private final boolean randomInit;

    public PropertyDefinitionDTO(String name, String type, String from, String to, String defaultVal, boolean randomInit) {
        this.name = name;
        this.type = type;
        this.from = from;
        this.to = to;
        this.defaultVal = defaultVal;
        this.randomInit = randomInit;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }


    public String getDefaultVal() {
        return defaultVal;
    }

    public boolean isRandomInit() {
        return randomInit;
    }
}
