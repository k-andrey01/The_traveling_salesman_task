import java.util.ArrayList;
import java.util.Arrays;

public class Finder3 {

    private final int maximum = Integer.MAX_VALUE;
    private ArrayList<Road2> roads = new ArrayList<>();
    private int numCities;
    private int indexI = 0;
    private int indexJ = 0;
    private int preCost=0;
    private int iter = 0;
    ArrayList<Integer> way = new ArrayList<>();
    ArrayList<Integer> citiesToGo = new ArrayList<>();

    public void find(int[][] matrixMain){
        numCities = matrixMain.length;
        citiesToGo.add(1);
        citiesToGo.add(2);
        citiesToGo.add(3);
        citiesToGo.add(4);
        citiesToGo.add(5);
        citiesToGo.add(6);
        int[][] matrix= cloneMatrix(matrixMain);

        int[] di = getMinArray(matrix, false);
        matrix = diffMatrix(matrix, di, false);
        int[] dj = getMinArray(matrix, true);
        matrix = diffMatrix(matrix, dj, true);
        preCost = sumArrays(di, dj);
        System.out.println("\ndi: " + Arrays.toString(di) + ";");
        System.out.println("dj: " + Arrays.toString(dj) + ";");
        System.out.println("\nМатрица после сокращения:");
        printMatrix(matrix);

        while (matrix.length > 2){
            di = getMinArray(matrix, false);
            matrix = diffMatrix(matrix, di, false);
            dj = getMinArray(matrix, true);
            matrix = diffMatrix(matrix, dj, true);

            int[][] matrixWith = getNewMatrix(cloneMatrix(matrix), "with");
            int[][] matrixWithout = getNewMatrix(cloneMatrix(matrix), "without");

            di = getMinArray(matrixWith, false);
            dj = getMinArray(matrixWith, true);
            int cost1 = preCost + sumArrays(di, dj);
            roads.add(new Road2(indexI, indexJ, matrixWith, cost1));

            int cost2 = preCost + getMinPoint(matrixWithout, indexI, indexJ);
            roads.add(new Road2(-(indexI), -(indexJ), matrixWithout, cost2));

            getNewWayAndCitiesToGo(roads.get(roads.size()-1));
            getNewWayAndCitiesToGo(roads.get(roads.size()-2));

            Road2 nowRoad = getRoad(roads);
            if (nowRoad.getWay()!=null)
                way = nowRoad.getWay();
            printWay(way);
            citiesToGo = nowRoad.getCitiesToGo();
            matrix = cloneMatrix(nowRoad.matrix);
            System.out.println();
            printMatrix(matrix);
        }
        way.add(way.get(0));
        System.out.println("Итоговый путь:");
        printWay(way);
        System.out.println("Стоимость:");
        System.out.println(getSum(way, matrixMain));
    }
    private int getSum(ArrayList<Integer> way, int[][] clone) {
        int sum = 0;
        int point = way.size()-1;
        if (point>=0) {
            int v = way.get(point);
            point--;
            while (point>=0) {
                sum += clone[v - 1][way.get(point) - 1];
                v = way.get(point);
                point--;
            }
        }
        return sum;
    }

    private void printWay(ArrayList<Integer> way) {
        StringBuilder sb = new StringBuilder();
        if (!way.isEmpty()) {
            for (Integer num : way) {
                sb.append(num).append(" -> ");
            }
            sb.delete(sb.length() - 4, sb.length());
        }
        System.out.println(sb.toString());
    }

    private Road2 getRoad(ArrayList roads){
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i< roads.size(); i++){
            Road2 nowRoad = (Road2) roads.get(i);

            if (nowRoad.cost<=min){
                index = i;
                min = nowRoad.cost;
            }
        }
        Road2 itogRoad = (Road2) roads.get(index);
        roads.remove(index);
        preCost = itogRoad.cost;
        return itogRoad;
    }

    private void getNewWayAndCitiesToGo(Road2 itogRoad){
        if (itogRoad.city<0 || itogRoad.nextCity<0){
            itogRoad.setWay(cloneArrayList(way));
            itogRoad.setCitiesToGo(cloneArrayList(citiesToGo));
        }
        else if (!citiesToGo.isEmpty()){
            way.add(citiesToGo.get(itogRoad.city));
            way.add(citiesToGo.get(itogRoad.nextCity));
            if (itogRoad.city > itogRoad.nextCity) {
                citiesToGo.remove(itogRoad.city);
                citiesToGo.remove(itogRoad.nextCity);
            }
            else{
                citiesToGo.remove(itogRoad.city);
                citiesToGo.remove(itogRoad.nextCity-1);
            }
            itogRoad.setWay(cloneArrayList(way));
            itogRoad.setCitiesToGo(cloneArrayList(citiesToGo));
        }
    }

    private int getMinPoint(int[][] matrix, int indexI, int indexJ){
        int sum = getMin(matrix, indexI, false, indexJ) + getMin(matrix, indexJ, true, indexI);
        return sum;
    }

    public int[][] getNewMatrix(int[][] matrix, String type){
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 0) {
                    int sum = getMin(matrix, i, false, j) + getMin(matrix, j, true, i);
                    if (sum > max) {
                        max = sum;
                        indexI = i;
                        indexJ = j;
                    }
                }
            }
        }
        matrix[indexI][indexJ] = -1;

        if (type.equals("with")) {
            iter++;
            matrix[indexJ][indexI] = -1;
            matrix = removeLineAndRow(matrix, indexI, indexJ);
        }

        return matrix;
    }

    private int[][] cloneMatrix(int[][] martrix) {
        int[][] clone = new int[martrix.length][];
        int count = 0;
        for (int[] line : martrix) {
            clone[count++] = line.clone();
        }
        return clone;
    }

    private ArrayList<Integer> cloneArrayList(ArrayList<Integer> list){
        ArrayList<Integer> clone = new ArrayList<>();
        for (int i=0; i< list.size(); i++){
            clone.add(list.get(i));
        }
        return clone;
    }


    private int[][] removeLineAndRow(int[][] matrix, int indexI, int indexJ) {
        int[][] result = new int[matrix.length - 1][matrix.length - 1];
        int countI = 0;
        int countJ;
        for (int i = 0; i < matrix.length; i++) {
            if (i != indexI) {
                countJ = 0;
                for (int j = 0; j < matrix.length; j++) {
                    if (j != indexJ) {
                        result[countI][countJ++] = matrix[i][j];
                    }
                }
                countI++;
            }
        }
        matrix = result;
        return matrix;
    }

    private int sumArrays(int[] firstArr, int[] secondArr){
        int sum = 0;
        for (int i = 0; i<firstArr.length; i++){
            sum += firstArr[i]+secondArr[i];
        }
        return sum;
    }

    private int getMin(int[][] matrix, int index, boolean row, int j) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            if (i != j) {
                int number = row ? matrix[i][index] : matrix[index][i];
                if (number != -1 && number < min) {
                    min = number;
                }
            }
        }
        return min;
    }

    private void printMatrix(int[][] matrix){
        for (int i=0; i< matrix.length; i++){
            for (int j=0; j<matrix.length; j++){
                String output = String.format("%4s",matrix[i][j]);
                System.out.print(output);
            }
            System.out.println("\n");
        }
    }

    private int[] getMinArray(int[][] matrix, boolean row) {
        int[] res = new int[matrix.length];
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            res[count++] = getMin(matrix, i, row, -1);
        }
        return res;
    }

    private int[][] diffMatrix(int[][] matrix, int[] d, boolean row) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != -1) {
                    matrix[i][j] -= row ? d[j] : d[i];
                }
            }
        }
        return matrix;
    }

}
