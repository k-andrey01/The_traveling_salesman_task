import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Finder2 {
    private final int maximum = Integer.MAX_VALUE;

    public void find(int[][] matrix) {

        final int number_cities = matrix.length;

        // Выполнение первого сокращения матрицы затрат
        int x = 0;
        x = reduce(matrix, x, maximum, maximum);
        Road road = new Road(number_cities);

        //Корень дерева начинается с города 0.
        road.city = 0;
        road.cost = x;
        road.matrix = matrix;
        road.way.push(0);
        road.remainingcity = new int[number_cities - 1];
        road.city_left_to_expand = number_cities - 1;

        for (int i = 0; i < number_cities - 1; i++) {
            road.remainingcity[i] = i + 1;
        }

        //Стек DS для ведения узлов в дереве
        Stack<Road> cities = new Stack<>();
        cities.push(road);

        //Временная переменная для хранения лучшего решения, найденного до сих пор
        Road mem_best_solution = new Road(number_cities);
        int current_best_cost = maximum;
        //Итерации стека, включая возврат
        // Запуск обхода дерева - обходы до тех пор, пока стек не станет пустым, т.е. все узлы были расширены
        while (!cities.empty()) {
            // Инициализация переменной
            List list = new ArrayList();
            Road city = cities.pop();
            //Расширяйте стек только в том случае, если узел не является конечным узлом и если его стоимость лучше, чем лучшая на данный момент
            if (city.city_left_to_expand == 0) {
                //Сравнивая стоимость этого узла с лучшими и обновляя при необходимости
                if (city.cost <= current_best_cost) {
                    mem_best_solution = city;
                    current_best_cost = mem_best_solution.cost;
                }
            } else {
                if (city.cost <= current_best_cost) {
                    // Расширение последнего узла, извлеченного из стека
                    expand(list, city);
                    //Определение порядка, в котором расширенные узлы должны быть помещены в стек
                    int[] arrow = new int[list.size()];
                    for (int pi = 0; pi < list.size(); pi++) {
                        Road help = (Road) list.get(pi);
                        arrow[pi] = help.cost;
                    }
                    // Сортировка узлов в порядке убывания на основе их стоимости
                    int[] tempppp = decreasing_sort(arrow);
                    for (int pi = 0; pi < tempppp.length; pi++) {
                        // Помещение узловых объектов в стек в порядке убывания
                        cities.push((Road) list.get(tempppp[pi]));
                    }
                }
            }
        }

        System.out.println("cost: " + current_best_cost);
        System.out.println();
        // Печать оптимального тура
        System.out.print("[ ");
        for (int st_i = 0; st_i < mem_best_solution.way.size()-1; st_i++) {
            Integer k = mem_best_solution.way.get(st_i);
            System.out.print(k + 1);
            System.out.print(" -> ");
        }
        System.out.println(mem_best_solution.way.get(0)+1+" ] ");
    }

    public int reduce(int[][] matrix, int cost, int row, int column) {
        // Массивы для хранения строк и столбцов, которые будут уменьшены
        int[] array_to_reduce = new int[matrix.length];
        int[] reduced_array = new int[0];
        // Переменная для хранения обновленной стоимости
        int new_cost = cost;
        // Петля для уменьшения рядов
        for (int i = 0; i < matrix.length; i++) {
            // Если строка соответствует текущему городу, не уменьшайте
            if (i == row) continue;
            // Если строка не соответствует текущему городу, попробуйте уменьшить
            for (int j = 0; j < matrix.length; j++) {
                // Выбрать строку, которая будет уменьшена
                array_to_reduce[j] = matrix[i][j];
            }
            // Проверьте, можно ли уменьшить текущий ряд
            if (minimum(array_to_reduce) != 0) {
                // Обновление новой стоимости
                new_cost = minimum(array_to_reduce) + new_cost;
                // Уменьшение строки
                reduced_array = min(array_to_reduce, minimum(array_to_reduce));
                // Отталкивание уменьшенного ряда обратно в исходный массив
                for (int k = 0; k < matrix.length; k++) {
                    matrix[i][k] = reduced_array[k];
                }
            }
        }
        // Петля для уменьшения столбцов
        for (int i = 0; i < matrix.length; i++) {
            // Если столбец соответствует текущему городу, не уменьшайте
            if (i == column) continue;
            // Если столбец не соответствует текущему городу, попробуйте уменьшить
            for (int j = 0; j < matrix.length; j++) {
                // Выборка столбца должна быть уменьшена
                array_to_reduce[j] = matrix[j][i];
            }
            // Проверьте, можно ли уменьшить текущий столбец
            if (minimum(array_to_reduce) != 0) {
                // Обновление текущей стоимости
                new_cost = minimum(array_to_reduce) + new_cost;
                // Уменьшение столбца
                reduced_array = min(array_to_reduce, minimum(array_to_reduce));
                // Отталкивание уменьшенного столбца обратно в исходный массив
                for (int k = 0; k < matrix.length; k++) {
                    matrix[k][i] = reduced_array[k];
                }
            }
        }

        System.out.println("уменьшенные значения: \n" + toString(reduced_array));
        System.out.println("уменьшенная матрица: \n" + toString(matrix));
        System.out.println();
        return new_cost;
    }

    public static int[] min(int[] array, int min) {
        // Recurse through array and reduce with the passed "min" value
        for (int j = 0; j < array.length; j++) {
            array[j] = array[j] - min;
        }
        // Return reduced array
        return array;
    }

    public int minimum(int[] array) {
        // Объявление по умолчанию как нечто меньшее, чем бесконечность, но выше, чем допустимые значения
        int min = 9000;
        // Рекурсивно через массив найти минимальное значение
        for (int i = 0; i < array.length; i++) {
            // Если значение действительно, т. Е. Меньше бесконечности, сбросьте мин с этим значением
            if (array[i] < min) {
                min = array[i];
            }
        }
        // Проверьте, не изменилось ли минимальное значение, то есть мы встретили бесконечный массив
        if (min == 9000) {
            // Вернуть 0 как ничто, чтобы уменьшить
            return 0;
        }
        // Иначе вернуть минимальное значение
        else {
            return min;
        }
    }

    /*
    Expand - Node Expansion Function
    Input - List to return the reduced cost matrix, Object of node to be expanded
    Return - Cost of Reduction
    */

    public void expand(List l, Road o) {
        // Количество городов для прохождения
        int length = o.remainingcity.length;
        for (int i = 0; i < length; i++) {
            // Инициализация переменной
            if (o.remainingcity[i] == 0) continue;
            int cost;
            cost = o.cost;
            int city = o.city;
            Stack<Integer> st = new Stack<>();
            for (int st_i = 0; st_i < o.way.size(); st_i++) {
                Integer k = o.way.get(st_i);
                st.push(k);
            }
            st.push(o.remainingcity[i]);
            // Извлечение содержимого матрицы во временную матрицу для сокращения
            int[][] temparray = new int[o.matrix.length][o.matrix.length];
            for (int i_1 = 0; i_1 < temparray.length; i_1++) {
                for (int i_2 = 0; i_2 < temparray.length; i_2++) {
                    temparray[i_1][i_2] = o.matrix[i_1][i_2];
                }
            }
            //Добавление значения ребра (i, j) к стоимости
            cost = cost + temparray[city][o.remainingcity[i]];
            //Делая i-й ряд и j-й столбец бесконечным
            for (int j = 0; j < temparray.length; j++) {
                temparray[city][j] = 9999;
                temparray[j][o.remainingcity[i]] = 9999;
            }
            //Делая (j, 0) бесконечностью
            temparray[o.remainingcity[i]][0] = 9999;
            //Сокращение этой матрицы в соответствии с указанными правилами
            int cost1 = reduce(temparray, cost, city, o.remainingcity[i]);
            // Обновление содержимого объекта, соответствующего текущего тура по городу
            Road finall = new Road(o.matrix.length);
            finall.city = o.remainingcity[i];
            finall.cost = cost1;
            finall.matrix = temparray;
            int[] temp_array = new int[o.remainingcity.length];
            // Ограничение расширения в случае возврата
            for (int i_3 = 0; i_3 < temp_array.length; i_3++) {
                temp_array[i_3] = o.remainingcity[i_3];
            }
            temp_array[i] = 0;
            finall.remainingcity = temp_array;
            finall.city_left_to_expand = o.city_left_to_expand - 1;
            finall.way = st;
            l.add(finall);
        }
    }

    private static String toString(int[] reduced_array) {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (int i : reduced_array) {
            if (i > 4500) {
                s.append(9999 - i);
            } else {
                s.append(i);
            }
            s.append(",");
        }
        s.append("]");
        return s.toString();
    }

    /*
    Decreasing Sort - сортирует переданный массив в порядке убывания и возвращает индекс
    Input - Массив для сортировки в порядке убывания
    Return - отсортированный массив
    */

    public static int[] decreasing_sort(int[] temp) {
        int[] y = new int[temp.length];
        // Получение содержимого массива
        for (int j = 0; j < temp.length; j++) {
            y[j] = temp[j];
        }
        int x = 0;
        // Сортировка
        for (int i = 0; i < temp.length - 1; i++) {
            if (temp[i] < temp[i + 1]) {
                x = temp[i];
                temp[i] = temp[i + 1];
                temp[i + 1] = x;
            }
        }
        int[] to_be_returned = new int[temp.length];
        int f = 0;
        // Помещение отсортированного содержимого в массив для возврата
        for (int j = 0; j < temp.length; j++) {
            for (int j1 = 0; j1 < temp.length; j1++) {
                if (temp[j] == y[j1]) {
                    to_be_returned[j] = j1;
                }
            }
        }
        return to_be_returned;
    }


    private static String toString(int[][] array) {
        StringBuilder str = new StringBuilder();
        str.append("[\n");
        for (int[] ints : array) {
            for (int anInt : ints) {
                if (anInt > 4500) {
                    str.append(9999 - anInt);
                } else {
                    str.append(anInt);
                }
                str.append(",");
            }
            str.append("\n");
        }
        str.append("]");
        return str.toString();
    }

}
