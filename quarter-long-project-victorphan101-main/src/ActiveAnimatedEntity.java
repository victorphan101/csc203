import processing.core.PImage;

import java.util.List;

public abstract class ActiveAnimatedEntity extends AnimatedEntity{
    private final int actionPeriod;

    public ActiveAnimatedEntity(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod) {
        super(id, position, images, imageIndex, animationPeriod);
        this.actionPeriod = actionPeriod;
    }


    protected abstract void executeActivity(WorldModel world,
                                            ImageStore imageStore,
                                            EventScheduler scheduler);

    public int getActionPeriod(){
        return actionPeriod;
    }
    public int getAnimationPeriod(){
        return super.getAnimationPeriod();
    };

    public Action createActivityAction(WorldModel world, ImageStore imageStore){
        return new ActivityKind(this, world, imageStore, 0);
    };

    public Action createAnimationAction(int repeatCount){
        return super.createAnimationAction(repeatCount);
    };


    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    };


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }




}
