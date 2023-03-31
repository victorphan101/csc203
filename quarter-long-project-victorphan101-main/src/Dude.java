import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Dude extends moveTo{

    private int resourceCount;
    private int resourceLimit;


    public Dude(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod, int resourceLimit) {
        super(id, position, images, imageIndex, actionPeriod, animationPeriod);
        this.resourceCount = 0;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit(){
        return this.resourceLimit;
    }
    public int getResourceCount(){return this.resourceCount;}
    public void setResourceCount(int count){ this.resourceCount = count;}


    @Override
    protected Predicate<Point> uniqueBound(WorldModel world) {
        return (p) -> !world.isOccupied(p) || (WorldView.getOccupancyCell(world, p) instanceof Stump);
    }
}
