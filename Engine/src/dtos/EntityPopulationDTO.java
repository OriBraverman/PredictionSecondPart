package dtos;

public class EntityPopulationDTO {
    private final String name;
    private final String value;
    private final boolean hasValue;


    public EntityPopulationDTO(String name, String value, boolean hasValue) {
        this.name = name;
        this.value = value;
        this.hasValue = hasValue;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() { return value; }
}
