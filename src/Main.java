import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // объект Runtime, связанный с JVM
        Runtime runtime = Runtime.getRuntime();
        // кол-во доступных процессоров
        int availableProcessors = runtime.availableProcessors();

        // cоздаем пул потоков
        final ExecutorService threadPool = Executors.newFixedThreadPool(availableProcessors);

        // список для хранения Future объектов
        List<Future<Integer>> futures = new ArrayList<>();

        // создаем массив строк длиной 25 элементов
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        // start time
        long startTs = System.currentTimeMillis();

        // отправляем задачи в пул потоков
        for (String text : texts) {

            // используем Callable для возврата max интервала
            Future<Integer> future = threadPool.submit(() -> {
                int maxSize = 0;  // переменная для хранения max размера интервала
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false; //// флаг для отслеживания наличия 'b'
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;   // обновляем max размер интервала
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;  // возвращаем max размер интервала в .sumbit
            });
            futures.add(future); // добавляем в список
        }
        // находим max интервал
        int globalMaxSize = 0;
        for (Future<Integer> future : futures) {
            int localMaxSize = future.get();
            if (localMaxSize > globalMaxSize) {
                globalMaxSize = localMaxSize;
            }

        }
        threadPool.shutdown();
        // end time
        long endTs = System.currentTimeMillis();
        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Максимальный размер интервала: " + globalMaxSize);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}