package endorphins.bingfa;

/**
 * @author timothy
 * @createTime 2022年02月13日 18:58:00
 */
public class Test {
    private static long count = 0;

    private void add10k() {
        int idx = 0;
        while (idx++ < 10000) {
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {
        final Test test = new Test();
        Thread th1 = new Thread(() -> {
           test.add10k();
        });
        Thread th2 = new Thread(() -> {
            test.add10k();
        });

        th1.start();
        th2.start();

        th1.join();
        th2.join();
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(calc());
    }
}
