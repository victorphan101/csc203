import java.util.List;
import java.util.Objects;
import java.util.Optional;

import processing.core.PImage;

import static jdk.dynalink.linker.support.Guards.isInstance;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = new Point(position.x, position.y);
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public String getId(){
        return this.id;
    }
    public Point getPosition(){
        return this.position;
    }
    public void setPosition(Point point){
        position = new Point(point.x, point.y);
    }
    public List<PImage> getImages(){
        return this.images;
    }
    public int getImageIndex(){
        return this.imageIndex;
    }
    public void setImageIndex(int index){
        this.imageIndex = index;
    }
    public PImage getCurrentImage() {
        return this.getImages().get((this).getImageIndex());
    }
}
