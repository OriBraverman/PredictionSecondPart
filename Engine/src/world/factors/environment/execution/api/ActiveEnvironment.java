package world.factors.environment.execution.api;

import world.factors.property.execution.PropertyInstance;

public interface ActiveEnvironment {
    PropertyInstance getProperty(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);
}
