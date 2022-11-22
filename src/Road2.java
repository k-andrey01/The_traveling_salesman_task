import java.util.ArrayList;

public class Road2 {
    public int city;
    public int nextCity;
    public int[][] matrix;
    public int cost;

    public Road2(int city, int nextCity, int[][] matrix, int cost) {
        this.city = city;
        this.nextCity = nextCity;
        this.matrix = matrix;
        this.cost = cost;
    }
}
