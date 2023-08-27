package simulation;

import context.ContextImpl;
import world.World;
import world.factors.action.api.Action;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.entity.execution.manager.EntityInstanceManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.rule.Rule;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Simulation implements Serializable {
    private final int id;
    private final ActiveEnvironment activeEnvironment;
    private final World world;
    private final EntityInstanceManager entityInstanceManager;
    private boolean isTerminatedBySecondsCount = false;
    private boolean isTerminatedByTicksCount = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    private String formattedStartTime;


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

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    private void initEntityInstancesArray() {
        EntityDefinition entityDefinition = world.getEntities().get(0);
        for (int i = 0; i < entityDefinition.getPopulation(); i++) {
            entityInstanceManager.create(entityDefinition);
        }
    }

    public String run() {
        Instant start = Instant.now();
        Date date = new Date();
        this.formattedStartTime = this.dateFormat.format(date);

        initEntityInstancesArray();
        int currentTick = 0;
        Instant now = Instant.now();
        Duration duration = Duration.between(start, now);
        long seconds = duration.getSeconds();
        while (!this.world.getTermination().isTerminated(currentTick, seconds)) {
            currentTick++;
            for (Rule rule: this.world.getRules()){
                if (rule.isRuleActive(currentTick)){
                    List<EntityInstance> tempEntityInstances = new ArrayList<>(this.entityInstanceManager.getInstances());
                    for (EntityInstance entityInstance : tempEntityInstances) {
                        for (Action action: rule.getActionsToPerform()){
                            action.invoke(new ContextImpl(entityInstance, entityInstanceManager, this.activeEnvironment));
                        }
                    }
                }
            }
            now = Instant.now();
            duration = Duration.between(start, now);
            seconds = duration.getSeconds();
        }
        isTerminatedBySecondsCount = this.world.getTermination().isTerminatedBySecondsCount(seconds);
        isTerminatedByTicksCount = this.world.getTermination().isTerminatedByTicksCount(currentTick);
        return "Simulation finished";
    }

}
