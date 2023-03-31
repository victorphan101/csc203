public class AnimationKind implements Action{
    Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationKind(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public Entity getEntity(){
        return entity;
    }
    public WorldModel getWorldModel(){
        return world;
    }
    public ImageStore getImageStore(){
        return imageStore;
    }
    public int repeatCount(){
        return repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        ((AnimatedEntity)entity).nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                    ((AnimatedEntity)entity).createAnimationAction(Math.max(repeatCount - 1,
                            0)),
                    ((AnimatedEntity)entity).getAnimationPeriod());
        }
    }
}
