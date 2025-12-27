package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        boolean[][] blocked = new boolean[WIDTH][HEIGHT];

        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                blocked[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int endX = targetUnit.getxCoordinate();
        int endY = targetUnit.getyCoordinate();

        if (blocked[startX][startY] || blocked[endX][endY]) {
            return new ArrayList<>();
        }

        class Node implements Comparable<Node> {
            int x, y;
            int g;
            int f;
            Node parent;

            Node(int x, int y, int g, int f, Node parent) {
                this.x = x;
                this.y = y;
                this.g = g;
                this.f = f;
                this.parent = parent;
            }

            @Override
            public int compareTo(Node o) {
                return Integer.compare(this.f, o.f);
            }
        }

        PriorityQueue<Node> open = new PriorityQueue<>();
        boolean[][] visited = new boolean[WIDTH][HEIGHT];

        int hStart = heuristic(startX, startY, endX, endY);
        open.add(new Node(startX, startY, 0, hStart, null));

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.x == endX && current.y == endY) {
                List<Edge> path = new ArrayList<>();
                Node node = current;
                while (node != null) {
                    path.add(new Edge(node.x, node.y));
                    node = node.parent;
                }
                Collections.reverse(path);
                return path;
            }

            if (visited[current.x][current.y]) continue;
            visited[current.x][current.y] = true;

            for (int[] dir : DIRECTIONS) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT && !blocked[nx][ny] && !visited[nx][ny]) {
                    int moveCost = (dir[0] != 0 && dir[1] != 0) ? 14 : 10;
                    int gNew = current.g + moveCost;
                    int fNew = gNew + heuristic(nx, ny, endX, endY);
                    open.add(new Node(nx, ny, gNew, fNew, current));
                }
            }
        }

        return new ArrayList<>();
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return 10 * (dx + dy) - 6 * Math.min(dx, dy);
    }
}

