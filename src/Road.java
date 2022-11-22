import java.util.Stack;

public class Road implements Cloneable{
    int city;
    int cost;
    int[][] matrix;
    int[] remainingcity;
    int city_left_to_expand;
    Stack<Integer> way;

    Road(int number) {
        matrix = new int[number][number];
        way = new Stack<Integer>();
    }
}
