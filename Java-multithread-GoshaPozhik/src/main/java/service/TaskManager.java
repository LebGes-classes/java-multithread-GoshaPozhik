package service;

import model.Employee;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private final List<Employee> employees = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void addEmployee() {
        System.out.print("Введите имя сотрудника: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Имя не может быть пустым!");
            return;
        }

        employees.add(new Employee(name));
        System.out.println("Сотрудник " + name + " добавлен");
    }

    public void addTask() {
        if (employees.isEmpty()) {
            System.out.println("Сначала добавьте сотрудников!");
            return;
        }

        // Показываем список сотрудников
        System.out.println("\nСписок сотрудников:");
        for (int i = 0; i < employees.size(); i++) {
            System.out.println((i + 1) + ". " + employees.get(i).getName());
        }

        // Выбор сотрудника
        System.out.print("Выберите номер сотрудника: ");
        int empIndex = getIntInput() - 1;
        
        if (empIndex < 0 || empIndex >= employees.size()) {
            System.out.println("Неверный номер сотрудника!");
            return;
        }

        // Ввод данных задачи
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine().trim();
        
        if (description.isEmpty()) {
            System.out.println("Описание не может быть пустым!");
            return;
        }

        System.out.print("Введите количество часов (1-16): ");
        int hours = getIntInput();
        
        if (hours < 1 || hours > 16) {
            System.out.println("Количество часов должно быть от 1 до 16!");
            return;
        }

        // Добавление задачи
        employees.get(empIndex).getTasks().add(new Task(description, hours));
        System.out.println("Задача добавлена");
    }

    public void startWork() {
        if (employees.isEmpty()) {
            System.out.println("Нет сотрудников для работы!");
            return;
        }

        // Создаем и запускаем потоки для каждого сотрудника
        List<Thread> threads = new ArrayList<>();
        for (Employee emp : employees) {
            Thread thread = new Thread(new WorkDaySimulator(emp));
            threads.add(thread);
            thread.start();
        }

        // Ждем завершения всех потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Ошибка при ожидании завершения потока: " + e.getMessage());
            }
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Пожалуйста, введите число: ");
            }
        }
    }
} 