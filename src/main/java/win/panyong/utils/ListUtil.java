package win.panyong.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

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


    //随机获取列表中数据
    public static <T> List<T> getRandomList(List<T> list, int n) {
        Collections.shuffle(list);
        if (list.size() <= n) {
            return list;
        } else {
            return list.subList(0, n);
        }
    }


    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }

    public static <T> Predicate<T> predicateWithIndex(BiPredicate<T, Integer> predicate) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            return predicate.test(t, index);
        };
    }

    public static <T, R> Function<T, R> functionWithIndex(BiFunction<T, Integer, R> function) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            return function.apply(t, index);
        };
    }

}
