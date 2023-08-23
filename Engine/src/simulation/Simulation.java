package simulation;

import context.Context;
import context.ContextImpl;
import world.World;
import world.factors.action.api.Action;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.EntityInstanceImpl;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.entity.execution.manager.EntityInstanceManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.property.definition.api.EntityPropertyDefinition;
import world.factors.rule.Rule;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final int id;
    private final ActiveEnvironment activeEnvironment;
    private final World world;
    private final EntityInstanceManager entityInstanceManager;
    private boolean isTerminatedBySecondsCount = false;
    private boolean isTerminatedByTicksCount = false;
    private SimpleDateFormat startTime = new SimpleDateFormat("dd-mm-yyyy | hh.mm.ss");


    public Simulation(ActiveEnvironment activeEnvironment, World world, int id) {
        this.activeEnvironment = activeEnvironment;
        this.world = world;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ActiveEnvironment getActiveEnvironment() {
        return activeEnvironment;
    }

    public World getWorld() {
        return world;
    }

    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public boolean isTerminatedBySecondsCount() {
        return isTerminatedBySecondsCount;
    }

    public boolean isTerminatedByTicksCount() {
        return isTerminatedByTicksCount;
    }

    public SimpleDateFormat getStartTime() {
        return startTime;
    }

    private void initEntityInstancesArray() {
        EntityDefinition entityDefinition = world.getEntities().get(0);
        for (int i = 0; i < entityDefinition.getPopulation(); i++) {
            entityInstanceManager.create(entityDefinition);
        }
    }

    public String run() {
        Time timer = new Time(0);
        this.startTime.format(timer);
        initEntityInstancesArray();
        int currentTick = 0;
        while (this.world.getTermination().isTerminated(currentTick, timer)) {
            currentTick++;
            for (Rule rule: this.world.getRules()){
                if (rule.isRuleActive(currentTick)){
                    for (EntityInstance entityInstance : this.entityInstanceManager.getInstances()) {
                        for (Action action: rule.getActionsToPerform()){
                            action.invoke(new ContextImpl(entityInstance, entityInstanceManager, this.activeEnvironment));
                        }
                    }
                }
            }
        }
        isTerminatedBySecondsCount = this.world.getTermination().isTerminatedBySecondsCount(timer);
        isTerminatedByTicksCount = this.world.getTermination().isTerminatedByTicksCount(currentTick);
        return "Simulation finished";
    }

}
