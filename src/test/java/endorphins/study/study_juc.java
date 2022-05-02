package endorphins.study;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author timothy
 * @createTime 2022年02月10日 21:14:00
 */
public class study_juc {

    @Test
    public void test() {
        int a = 9;
        int b = 3;
        int sub;
        while (b != 0) {
            sub = Math.floorMod(a, b);
            a = b;
            b = sub;
        }
        System.out.println(a);
    }

    public void maxNumberOfBalloons(String text) {

    }
}
