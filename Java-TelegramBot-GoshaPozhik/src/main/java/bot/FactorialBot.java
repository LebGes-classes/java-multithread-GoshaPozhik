package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactorialBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text.equalsIgnoreCase("/start")) {
                sendMsg(chatId, "Привет! Отправь команду вида /factorial 5 10 15");
            }
            else if (text.startsWith("/factorial")) {
                String[] parts = text.split(" ");
                if (parts.length < 2) {
                    sendMsg(chatId, "Укажите числа через пробел!");
                    return;
                }

                List<Integer> numbers = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    try {
                        numbers.add(Integer.parseInt(parts[i]));
                    } catch (NumberFormatException e) {
                        sendMsg(chatId, "Ошибка: '" + parts[i] + "' не число!");
                    }
                }

                if (numbers.isEmpty()) {
                    sendMsg(chatId, "Не найдено чисел для расчета");
                    return;
                }

                sendMsg(chatId, "⏱ Начинаю расчет " + numbers.size() + " факториалов...");

                List<String> results = Collections.synchronizedList(new ArrayList<>());
                List<Thread> threads = new ArrayList<>();

                ThreadCounter counter = new ThreadCounter(numbers.size());

                for (int num : numbers) {
                    Thread thread = new Thread(() -> {
                        sendMsg(chatId, "▶ Начал расчет " + num + "!");

                        BigInteger result = calculateFactorial(num);
                        String resLine = num + "! = " + result;

                        synchronized (results) {
                            results.add(resLine);
                        }

                        sendMsg(chatId, "✅ Закончил " + num + "!");

                        synchronized (counter) {
                            counter.increment();
                            if (counter.isAllDone()) {
                                sendFinalResults(chatId, results);
                            }
                        }
                    });

                    thread.start();
                    threads.add(thread);
                }
            }
        }
    }

    private void sendFinalResults(long chatId, List<String> results) {
        Collections.sort(results);
        StringBuilder sb = new StringBuilder("📊 Результаты:\n");
        for (String res : results) {
            sb.append(res).append("\n");
        }
        sendMsg(chatId, sb.toString());
    }

    private BigInteger calculateFactorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private void sendMsg(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() { return "mytextfiles_bot"; }

    @Override
    public String getBotToken() { return "7769549599:AAGPGa5WBi7zAeTLwh6SbEHJnaDqHJScOxs"; }

    static class ThreadCounter {
        private int completed = 0;
        private final int total;

        public ThreadCounter(int total) {
            this.total = total;
        }

        public void increment() {
            completed++;
        }

        public boolean isAllDone() {
            return completed == total;
        }
    }
}