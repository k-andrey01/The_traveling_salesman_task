public class MyGraph {
    public static void main(String[] args) {
        Finder3 myFinder = new Finder3();
        int[][] matrix = {{-1, 38, 63, 35, 79, 39},
                          {38, -1, 45, 18, 46, 14},
                          {63, 45, -1, 36, 21, 40},
                          {35, 18, 36, -1, 37, 6},
                          {79, 46, 21, 37, -1, 38},
                          {39, 14, 40, 6, 38, -1}};
        myFinder.find(matrix);
        Finder2 myFinder2 = new Finder2();
        //myFinder2.find(matrix);
    }
}
