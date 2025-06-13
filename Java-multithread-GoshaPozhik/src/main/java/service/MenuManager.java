package service;

import java.util.Scanner;

public class MenuManager {
    private final TaskManager taskManager;
    private final Scanner scanner;
    private boolean isRunning;

    public MenuManager() {
        this.taskManager = new TaskManager();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public void showMenu() {
        while (isRunning) {
            System.out.println("\n=== Меню ===");
            System.out.println("1. Добавить сотрудника");
            System.out.println("2. Добавить задачу");
            System.out.println("3. Начать работу");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    taskManager.addEmployee();
                    break;
                case "2":
                    taskManager.addTask();
                    break;
                case "3":
                    taskManager.startWork();
                    // Выводим статистику
                    System.out.println("\nСтатистика выполнения задач:");
                    StatisticsCalculator.printStatistics(taskManager.getEmployees());
                    System.out.printf("\nОбщая эффективность команды: %.1f%%\n",
                            StatisticsCalculator.calculateOverallEfficiency(taskManager.getEmployees()) * 100);
                    // Сохраняем в Excel
                    try {
                        ExcelHandler.saveStatistics(taskManager.getEmployees(), "output.xlsx");
                        System.out.println("\nСтатистика сохранена в output.xlsx");
                    } catch (Exception e) {
                        System.err.println("Ошибка при сохранении статистики: " + e.getMessage());
                    }
                    break;
                case "0":
                    isRunning = false;
                    System.out.println("Программа завершена");
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
} 