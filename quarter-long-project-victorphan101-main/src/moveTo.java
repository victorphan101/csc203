import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class moveTo extends ActiveAnimatedEntity{
    public moveTo(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod) {
        super(id, position, images, imageIndex, actionPeriod, animationPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            return moveToHelper(world, target, scheduler);
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = WorldView.getOccupant(world, nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract boolean moveToHelper(WorldModel world, Entity target, EventScheduler scheduler);

    private Point nextPosition(WorldModel world, Point destPos)
    {

        Predicate<Point> canPass = uniqueBound(world);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> Point.adjacent(p1, p2);
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> points = strategy.computePath(this.getPosition(), destPos, canPass,
                withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        return points.size()!=0? points.get(0):this.getPosition();
    }

    protected abstract Predicate<Point> uniqueBound(WorldModel world);
}
