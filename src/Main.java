import java.util.*; // Импортируем необходимые классы из пакета java.util

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // создаем массив строк длиной 25 элементов
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000); // генерируем строки
        }

        long startTs = System.currentTimeMillis(); // начало замера времени
        List<Thread> threads = new ArrayList<>(); // список для хранения потоков

        // обрабатываем каждую строку в отдельном потоке
        for (String text : texts) {
            // создаем новый поток
            Thread thread = new Thread(() -> {
                int maxSize = 0; // переменная для хранения max размера интервала
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) { // пропускаем, если индекс начала больше или равен индексу конца
                            continue;
                        }
                        boolean bFound = false; // флаг для отслеживания наличия 'b'
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true; // найден символ 'b'
                                break; // выходим из цикла, если 'b' найден
                            }
                        }
                        // обновляем max размер интервала, если 'b' не найден
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i; // обновляем max размер интервала
                        }
                    }
                }
                // выводим результат для текущей строки
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
            });
            threads.add(thread); // добавляем поток в список
            thread.start(); // запускаем поток
        }

        // ожидание завершения всех потоков
        for (Thread thread : threads) {
            thread.join(); // зависаем, ждем, когда поток завершится
        }

        long endTs = System.currentTimeMillis(); // окончание замера времени
        System.out.println("Time: " + (endTs - startTs) + "ms"); // вывод времени выполнения
    }

    // Метод для генерации случайной строки
    public static String generateText(String letters, int length) {
        Random random = new Random(); // создаем объект Random для генерации случайных символов
        StringBuilder text = new StringBuilder(); // используем StringBuilder для создания строки
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length()))); // добавляем случайный символ
        }
        return text.toString(); // возвращаем сгенерированную строку
    }
}