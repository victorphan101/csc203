import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        //closed list
        HashSet<Point> closedList = new HashSet<Point>();

        //track the point nodes
        HashMap<Point, PointNode> ptNodes = new HashMap<>();

        //all the paths created, track list
        HashMap<Point, List<Point>> paths = new HashMap<>();

        //main queue to check every point, openList
        PriorityQueue<Point> openList = new PriorityQueue<Point>(12000, Comparator.comparingInt(pt -> ptNodes.get(pt).getFValue()));
        //List<Point> pointsPath = new ArrayList<Point>();

        //current node set
        openList.add(start);
        ptNodes.put(start, new PointNode(start, 0));
        ptNodes.get(start).setHValue(findHValue(start, end));
        ptNodes.get(start).updateFValue();
        paths.put(start, new ArrayList<Point>());
        paths.get(start).add(start);
        closedList.add(start);

        while (openList.size()!=0){
            Point currPt = openList.remove();

            if (currPt.equals(end)){
                System.out.println("Reached!!");
                return paths.get(currPt);
            }

            List<Point> adjacentNodes = potentialNeighbors.apply(currPt).filter(canPassThrough).collect(Collectors.toList());

            for (Point pt: adjacentNodes){
                // check if already in closed List, then check to update the g-value or not
                if (closedList.contains(pt)) {
                    if (ptNodes.get(currPt).getGDist() + 1 < ptNodes.get(pt).getGDist()) {
                        //System.out.println("true");
                        List<Point> newPathList = new ArrayList<Point>(paths.get(currPt));
                        newPathList.add(pt);

                        //add new pathList of node to keep track
                        paths.put(pt, newPathList);

                        //add new node with updated gValue for new point
                        //System.out.println("curr g" + ptNodes.get(currPt).getGDist());
                        ptNodes.get(pt).setGDist(ptNodes.get(currPt).getGDist() + 1);
                        //System.out.println("new g" + ptNodes.get(pt).getGDist());

                        ptNodes.get(pt).setGDist(ptNodes.get(currPt).getGDist() + 1);
                    }
                }
                else{
                    //add new node to node list
                    ptNodes.put(pt, new PointNode(pt, ptNodes.get(currPt).getGDist() + 1));

                    //add new list
                    List<Point> newPathList = new ArrayList<Point>(paths.get(currPt));
                    newPathList.add(pt);
                    paths.put(pt, newPathList);

                    //set hValue
                    ptNodes.get(pt).setHValue(findHValue(pt, end));

                    if (!openList.contains(pt)) {
                        openList.add(pt);
                    }
                    closedList.add(pt);
                }
                //System.out.println("new "+ (ptNodes.get(currPt).getGDist() + 1));
                //System.out.println("prev "+ (ptNodes.get(pt).getGDist()));
                ptNodes.get(pt).updateFValue();
                //System.out.println(openList);
            }
        }
        return new ArrayList<>();
    }
    public int findHValue(Point curr, Point end){
        return (Math.abs(end.y- curr.y)+Math.abs(end.x- curr.x));
    }
}


class PointNode{
    private final Point point;
    private int gDist;
    private int hValue;
    private int fValue;

    public PointNode(Point p, int gDist){
        this.point = p;
        this.gDist = gDist;
        this.hValue = 0;
        this.fValue = this.gDist + this.hValue;
    }
    public Point getPoint(){
        return this.point;
    }
    public int getGDist(){
        return this.gDist;
    }
    public void setGDist(int dist){
        this.gDist = dist;
    }
    public void setHValue(int dist){
        this.hValue = dist;
    }
    public int getFValue(){
        return this.fValue;
    }
    public void updateFValue(){
        this.fValue = this.gDist + this.hValue;
    }

}
*/
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        //closed list
        HashSet<Point> closedList = new HashSet<Point>();

        //track the point nodes
        HashMap<Point, PointNode> ptNodes = new HashMap<>();

        //all the paths created, track list
        HashMap<Point, List<Point>> paths = new HashMap<>();


        //main queue to check every point, openList
        PriorityQueue<Point> openList = new PriorityQueue<Point>(1000, (pt1, pt2) -> {
            if(ptNodes.get(pt1).getFValue() < ptNodes.get(pt2).getFValue()){
                return -1;
            }
            else if (ptNodes.get(pt2).getFValue() < ptNodes.get(pt1).getFValue()){
                return 1;
            }
            return 0;});

        //current node set
        openList.add(start);
        ptNodes.put(start, new PointNode(start, 0));
        ptNodes.get(start).setHValue(findHValue(start, end));
        ptNodes.get(start).updateFValue();
        paths.put(start, new ArrayList<>());

        while (openList.size()!=0){
            Point currPt = openList.poll();

            if (withinReach.test(currPt, end)){
                return paths.get(currPt);
            }

            List<Point> adjacentNodes = potentialNeighbors.apply(currPt).filter(canPassThrough).collect(Collectors.toList());

            for (Point pt: adjacentNodes){
                // check if already in closed List, then check to update the g-value or not
                if (!closedList.contains(pt)) {
                    //new list for node
                    List<Point> newPathList = new ArrayList<Point>(paths.get(currPt));
                    newPathList.add(pt);
                    //add new pathList of node to keep track
                    paths.put(pt, newPathList);

                    //add new node with updated gValue for new point
                    ptNodes.put(pt, new PointNode(pt, ptNodes.get(currPt).getGDist() + 1));

                    //update hValues, gValues and fValues
                    ptNodes.get(pt).setHValue(findHValue(pt, end));
                    if (openList.contains(pt)) {
                        //check g-values
                        if (ptNodes.get(currPt).getGDist() + 1 < ptNodes.get(pt).getGDist()) {
                            ptNodes.get(pt).setGDist(ptNodes.get(currPt).getGDist() + 1);
                        }
                    }
                    ptNodes.get(pt).updateFValue();

                    openList.add(pt);
                    closedList.add(pt);
                }
                else {
                    if (ptNodes.get(currPt).getGDist() + 1 < ptNodes.get(pt).getGDist()) {
                        ptNodes.get(pt).setGDist(ptNodes.get(currPt).getGDist() + 1);
                        ptNodes.get(pt).updateFValue();

                        List<Point> newPathList = new ArrayList<Point>(paths.get(currPt));
                        newPathList.add(pt);
                        //add new pathList of node to keep track
                        paths.put(pt, newPathList);
                    }
                }
            }
        }
        return new ArrayList<>();
    }
    public int findHValue(Point curr, Point end){
        return (Math.abs(end.y- curr.y)+Math.abs(end.x- curr.x));
    }
}


class PointNode{
    private final Point point;
    private int gDist;
    private int hValue;
    private int fValue;

    public PointNode(Point p, int gDist){
        this.point = p;
        this.gDist = gDist;
        this.hValue = 0;
        this.fValue = this.gDist + this.hValue;
    }
    public Point getPoint(){
        return this.point;
    }
    public int getGDist(){
        return this.gDist;
    }
    public void setGDist(int dist){
        this.gDist = dist;
    }
    public void setHValue(int dist){
        this.hValue = dist;
    }
    public int getFValue(){
        return this.fValue;
    }
    public void updateFValue(){
        this.fValue = this.gDist + this.hValue;
    }

}




