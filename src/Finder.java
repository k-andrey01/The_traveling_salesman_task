import java.util.*;

public class Finder {
    public void find(int[][] matrix) {
        ArrayList<Integer> way = new ArrayList<>();
        int[][] clone = cloneMatrix(matrix);
        ArrayList<Integer> points= new ArrayList<>();
        for (int i = 1; i <= matrix.length; i++) {
            points.add(i);
        }
        printMatrix(matrix);
        int count = 1;
        while (matrix.length > 1) {
            System.out.println("\n-----------------------------------------------");
            System.out.println("ШАГ #" + count++ + ":");
            int[] di = getMinArray(matrix, false);
            matrix = diffMatrix(matrix, di, false);
            int[] dj = getMinArray(matrix, true);
            matrix = diffMatrix(matrix, dj, true);
            System.out.println("\ndi: " + Arrays.toString(di) + ";");
            System.out.println("dj: " + Arrays.toString(dj) + ";");
            System.out.println("\nМатрица после сокращения:");
            printMatrix(matrix);
            matrix = getPath(matrix, way, points);
            System.out.println("\nИзмененная матрица:");
            printMatrix(matrix);
            if (matrix.length == 1) {
                push(way, points.remove(0));
            }
            System.out.print("Текущий путь: ");
            printWay(way);
        }
        if (!way.isEmpty()) {
            way.add(way.get(0));
        }
        System.out.println("\n-----------------------------------------------");
        System.out.println("\nОтвет:");
        System.out.print("Путь: ");
        printWay(way);
        System.out.println("Сумма:  " + getSum(way, clone));
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

    private void printMatrix(int[][] matrix){
        for (int i=0; i< matrix.length; i++){
            for (int j=0; j<matrix.length; j++){
                String output = String.format("%4s",matrix[i][j]);
                System.out.print(output);
            }
            System.out.println("\n");
        }
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

    private int[][] cloneMatrix(int[][] martrix) {
        int[][] clone = new int[martrix.length][];
        int count = 0;
        for (int[] line : martrix) {
            clone[count++] = line.clone();
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

    private void push(ArrayList<Integer> way, int v) {
        if (way.contains(v) == false) {
            way.add(v);
        }
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

    private int[][] getPath(int[][] matrix, ArrayList<Integer> way, ArrayList<Integer> point) {
        int indexI = 0;
        int indexJ = 0;
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
        matrix = removeLineAndRow(matrix, indexI, indexJ);
        push(way, point.get(indexI));
        push(way, point.get(indexJ));
        point.remove(indexI);
        return matrix;
    }
}
