package dtos;

public class EnvVariableValueDTO {
    private String name;
    private String value;
    private boolean hasValue;


    public EnvVariableValueDTO(String name, String value, boolean hasValue) {
        this.name = name;
        this.value = value;
        this.hasValue = hasValue;
    }

    public boolean isHasValue() {
        return hasValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() { return value; }
}
