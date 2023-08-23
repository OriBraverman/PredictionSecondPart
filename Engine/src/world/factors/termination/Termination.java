package world.factors.termination;
import java.sql.Time;

public class Termination {
    private int secondsCount = -1;
    private int ticksCount = -1;
    //private boolean interactive; //TODO: check type


    public int getSecondsCount() {
        return secondsCount;
    }

    public void setSecondsCount(int secondsCount) {
        this.secondsCount = secondsCount;
    }

    public int getTicksCount() {
        return ticksCount;
    }

    public void setTicksCount(int ticksCount) {
        this.ticksCount = ticksCount;
    }

    public boolean isTerminated(int currentTick, Time currentTime) {
        if (isTerminatedBySecondsCount(currentTime) || isTerminatedByTicksCount(currentTick)) {
            return true;
        }
        return false;
    }

    public boolean isTerminatedBySecondsCount(Time timer) {
        if (this.secondsCount != -1) {
            if (timer.getSeconds() >= this.secondsCount) {
                return true;
            }
        }
        return false;
    }

    public boolean isTerminatedByTicksCount(int currentTick) {
        if (this.ticksCount != -1) {
            if (currentTick >= this.ticksCount) {
                return true;
            }
        }
        return false;
    }
}
