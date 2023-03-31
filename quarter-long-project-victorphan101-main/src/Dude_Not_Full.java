import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Dude_Not_Full extends Dude {


    public Dude_Not_Full(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images, 0, actionPeriod, animationPeriod, resourceLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<Class>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !super.moveTo(world,
                target.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    @Override
    protected boolean moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        super.setResourceCount(super.getResourceCount()+1);;
        ((Plants)target).setHealth(((Plants)target).getHealth()-1);
        return true;
    }

    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (super.getResourceCount() >= super.getResourceLimit()) {
            Entity miner = new Dude_Full(this.getId(),
                    this.getPosition(),this.getImages(), super.getResourceLimit(),this.getActionPeriod(),
                    this.getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((ActiveAnimatedEntity)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
