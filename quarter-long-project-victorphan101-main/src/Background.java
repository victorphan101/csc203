import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public String getId(){
        return this.id;
    }
    public List<PImage> getImages(){
        return this.images;
    }
    public int getImageIndex(){
        return this.imageIndex;
    }

    public static Optional<PImage> getBackgroundImage(
            WorldModel world, Point pos)
    {
        if (world.withinBounds(pos)) {
            return Optional.of(WorldView.getBackgroundCell(world, pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public static void setBackground(
            WorldModel world, Point pos, Background background)
    {
        if (world.withinBounds(pos)) {
            WorldView.setBackgroundCell(world, pos, background);
        }
    }

    public PImage getCurrentImage() {
        return this.images.get(
                    (this).getImageIndex());
    }


}
