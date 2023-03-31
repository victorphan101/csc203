import processing.core.PImage;

import java.util.List;

public class Tree extends Plants{

    //private int resourceCount;


    public Tree(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health) {
        super(id, position, images, 0, actionPeriod,animationPeriod, health, 0);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    @Override
    protected boolean _transformCheckSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

}

