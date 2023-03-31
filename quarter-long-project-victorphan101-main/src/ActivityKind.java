public class ActivityKind implements Action{
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityKind(
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

    public WorldModel getWorldModel(){
        return world;
    }
    public ImageStore getImageStore() {
        return imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        ((ActiveAnimatedEntity)entity).executeActivity(this.getWorldModel(),
                    this.getImageStore(), scheduler);

    }
}
