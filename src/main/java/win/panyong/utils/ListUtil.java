package win.panyong.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pan on 2020/5/2 1:55 PM
 */
public class ListUtil {

    /**
     * 将一个list均分成n个size尽量平均的list
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssignList(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int number = source.size() / n, remaider = source.size() % n;
        int fromIndex = 0;
        for (int i = 0; i < n; i++) {
            int toIndex = (--remaider >= 0 ? (number + 1) * (i + 1) : fromIndex + number);
            System.out.println(fromIndex + "," + toIndex);
            result.add(source.subList(fromIndex, toIndex));
            fromIndex = toIndex;
        }
        return result;
    }

    /**
     * 将一个list均分成多个size为n的list
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssignItem(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int length = source.size(), num = (length + n - 1) / n;//多少组
        int fromIndex = 0;
        for (int i = 0; i < num; i++) {
            int toIndex = (i + 1) * n < length ? (i + 1) * n : length;
            result.add(source.subList(fromIndex, toIndex));
            fromIndex = toIndex;
        }
        return result;
    }
}
