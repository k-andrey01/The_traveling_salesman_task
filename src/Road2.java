import java.util.ArrayList;

public class Road2 {
    public int city;
    public int nextCity;
    public int[][] matrix;
    public int cost;
    ArrayList<Integer> way;
    ArrayList<Integer> citiesToGo;

    public Road2(int city, int nextCity, int[][] matrix, int cost) {
        this.city = city;
        this.nextCity = nextCity;
        this.matrix = matrix;
        this.cost = cost;
    }

    public ArrayList<Integer> getWay() {
        return way;
    }

    public void setWay(ArrayList<Integer> way) {
        this.way = way;
    }

    public ArrayList<Integer> getCitiesToGo() {
        return citiesToGo;
    }

    public void setCitiesToGo(ArrayList<Integer> citiesToGo) {
        this.citiesToGo = citiesToGo;
    }
}
