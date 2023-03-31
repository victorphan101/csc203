import processing.core.PImage;

import java.util.List;

public class Sapling extends Plants{

    public Sapling(String id, Point position, List<PImage> images, int health) {
        super(id, position, images, 0, Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, health, Functions.SAPLING_HEALTH_LIMIT);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.setHealth(this.getHealth() +1);
        if (!this.transformPlant(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }


    @Override
    protected boolean _transformCheckSapling(WorldModel world,
                                             EventScheduler scheduler,
                                             ImageStore imageStore) {
        if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = new Tree("tree_" + this.getId(),
                    this.getPosition(), imageStore.getImageList(Functions.TREE_KEY),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((ActiveAnimatedEntity) tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}
