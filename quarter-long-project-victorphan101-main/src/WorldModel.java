import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows(){
        return this.numRows;
    }
    public int getNumCols(){
        return this.numCols;
    }
    public Background[][] getBackgroundArray(){
        return this.background;
    }
    public Entity[][] getOccupancy(){
        return this.occupancy;
    }
    public Set<Entity> getEntities(){
        return this.entities;
    }

    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            WorldView.setOccupancyCell(this, entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && WorldView.getOccupancyCell(this, pos) != null) {
            Entity entity = WorldView.getOccupancyCell(this, pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            WorldView.setOccupancyCell(this, pos, null);
        }
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && WorldView.getOccupancyCell(this, pos) != null;
    }

    public static void drawBackground(WorldView view) {
        for (int row = 0; row < view.getViewport().getNumRows(); row++) {
            for (int col = 0; col < view.getViewport().getNumCols(); col++) {
                Point worldPoint = view.getViewport().viewportToWorld(col, row);
                Optional<PImage> image =
                        Background.getBackgroundImage(view.getWorld(), worldPoint);
                if (image.isPresent()) {
                    view.getScreen().image(image.get(), col * view.getTileWidth(),
                            row * view.getTileHeight());
                }
            }
        }
    }

    public static void drawEntities(WorldView view) {
        for (Entity entity : view.getWorld().entities) {
            Point pos = entity.getPosition();

            if (view.getViewport().contains(pos)) {
                Point viewPoint = WorldView.worldToViewport(view.getViewport(), pos.x, pos.y);
                view.getScreen().image(entity.getCurrentImage(),
                        viewPoint.x * view.getTileWidth(),
                        viewPoint.y * view.getTileHeight());
            }
        }
    }

    private Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = Point.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public Optional<Entity> findNearest(Point pos, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : this.entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            WorldView.setOccupancyCell(this, oldPos, null);
            removeEntityAt(pos);
            WorldView.setOccupancyCell(this, pos, entity);
            entity.setPosition(pos);
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.getNumRows() && pos.x >= 0
                && pos.x < this.getNumCols();
    }

    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!Functions.processLine(in.nextLine(), this, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }



}
