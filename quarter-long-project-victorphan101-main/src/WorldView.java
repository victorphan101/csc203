import processing.core.PApplet;

import java.util.Optional;

public final class WorldView
{
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public PApplet getScreen(){
        return this.screen;
    }
    public WorldModel getWorld(){
        return this.world;
    }
    public int getTileWidth(){
        return this.tileWidth;
    }
    public int getTileHeight(){
        return this.tileHeight;
    }
    public Viewport getViewport(){
        return this.viewport;
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = Functions.clamp(this.getViewport().getCol() + colDelta, 0,
                this.getWorld().getNumCols() - this.getViewport().getNumCols());
        int newRow = Functions.clamp(this.getViewport().getRow() + rowDelta, 0,
                this.getWorld().getNumRows() - this.getViewport().getNumRows());

        this.getViewport().shift(newCol, newRow);
    }

    public void drawViewport() {
        WorldModel.drawBackground(this);
        WorldModel.drawEntities(this);
    }

    public static Point worldToViewport(Viewport viewport, int col, int row) {
        return new Point(col - viewport.getCol(), row - viewport.getRow());
    }

    public static Background getBackgroundCell(WorldModel world, Point pos) {
        return world.getBackgroundArray()[pos.y][pos.x];
    }

    public static void setBackgroundCell(
            WorldModel world, Point pos, Background background)
    {
        world.getBackgroundArray()[pos.y][pos.x] = background;
    }

    public static Optional<Entity> getOccupant(WorldModel world, Point pos) {
        if (world.isOccupied(pos)) {
            return Optional.of(getOccupancyCell(world, pos));
        }
        else {
            return Optional.empty();
        }
    }

    public static Entity getOccupancyCell(WorldModel world, Point pos) {
        return world.getOccupancy()[pos.y][pos.x];
    }

    public static void setOccupancyCell(
            WorldModel world, Point pos, Entity entity)
    {
        world.getOccupancy()[pos.y][pos.x] = entity;
    }

}
