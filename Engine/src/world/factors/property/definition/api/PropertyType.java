package world.factors.property.definition.api;

public enum PropertyType {
    DECIMAL {

        public Integer convert(Object value) {
            if (!(value instanceof Integer)) {
                throw new IllegalArgumentException("value " + value + " is not of a DECIMAL type (expected Integer class)");
            }
            return (Integer) value;
        }

        public boolean isMyType(String value) {
            return value.matches("-?\\d+");
        }
    }, BOOLEAN {

        public Boolean convert(Object value) {
            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException("value " + value + " is not of a BOOLEAN type (expected Boolean class)");
            }
            return (Boolean) value;
        }
        public boolean isMyType(String value) {
               return value.matches("true|false");
        }
    }, FLOAT {

        public Double convert(Object value) {
            if (!(value instanceof Double)) {
                throw new IllegalArgumentException("value " + value + " is not of a FLOAT type (expected Double class)");
            }
            return (Double) value;
        }
        public boolean isMyType(String value) {
            return value.matches("-?\\d+(\\.\\d+)?");
        }
    }, STRING {

        public String convert(Object value) {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("value " + value + " is not of a STRING type (expected String class)");
            }
            return (String) value;
        }
        public boolean isMyType(String value) {
            return true;
        }
    };

    public abstract <T> T convert(Object value);
    public abstract boolean isMyType(String value);
}