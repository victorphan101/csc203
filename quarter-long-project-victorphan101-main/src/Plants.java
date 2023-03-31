import processing.core.PImage;

import java.util.List;

public abstract class Plants extends ActiveAnimatedEntity{

    private int health;
    private final int healthLimit;

    public Plants(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod, int health, int healthLimit){
        super(id, position, images, imageIndex, actionPeriod,animationPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth(){
        return this.health;
    };
    public void setHealth(int health){
        this.health = health;
    };

    public int getHealthLimit(){
        return this.healthLimit;
    }
    public boolean transformPlant(WorldModel world,
                                  EventScheduler scheduler,
                                  ImageStore imageStore){

        if (this.getHealth()<= 0) {
            Entity stump = new Stump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            return true;
        }
        return _transformCheckSapling(world,
                scheduler,
                imageStore);
    }

    protected abstract boolean _transformCheckSapling(WorldModel world,
                                                      EventScheduler scheduler,
                                                      ImageStore imageStore);


}
