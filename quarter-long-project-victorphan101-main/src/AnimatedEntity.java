import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity{

    private final int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int imageIndex, int animationPeriod) {
        super(id, position, images, imageIndex);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    };

    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    };

    public Action createAnimationAction(int repeatCount){
        return new AnimationKind(this, null, null,
                repeatCount);
    };

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    };
}
