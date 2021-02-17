import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCode {


    @Test
    public void test() {
        String s = "123456579";
        System.out.println(splitIntoFibonacci(s));
    }

    public List<Integer> splitIntoFibonacci(String S) {
        List<Integer> list = new ArrayList<>();
        backtrack(S, 0, list);
        return list;
    }

    private boolean backtrack(String S, int index, List<Integer> list) {
        if(index == S.length() && list.size() >= 3) {
            return true;
        }
        for(int i = index; i < S.length(); i++) {
            long num = getCurValue(S, index, i);
            if(num > Integer.MAX_VALUE || num < 0) {
                break;
            }
            int size = list.size();
            if(size >= 2 && list.get(size - 2) + list.get(size - 1) < num) {
                break;
            }
            if(size <= 1 || list.get(size - 2) + list.get(size - 1) == num) {
                list.add((int)num);
                if(backtrack(S, i + 1, list)){
                    return true;
                }
                list.remove(list.size() - 1);
            }
        }
        return false;
    }

    private long getCurValue(String S, int left, int right) {
        if(left < right && S.charAt(left) == '0'){
            return -1;
        }
        long sum = 0;
        while(left <= right) {
            sum = sum * 10 + (long) S.charAt(left);
            left++;
        }
        return sum;
    }
}
