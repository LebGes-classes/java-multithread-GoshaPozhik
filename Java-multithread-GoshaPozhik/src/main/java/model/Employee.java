package model;

import java.util.LinkedList;
import java.util.Queue;

public class Employee {
    private final String name;                    // Имя сотрудника
    private final Queue<Task> tasks;              // Очередь задач
    private int totalWorkedHours;                 // Общее время работы
    private int idleHours;                        // Время простоя
    private int totalPresenceHours;               // Общее время на работе

    public Employee(String name) {
        this.name = name;
        this.tasks = new LinkedList<>();
    }

    // Основные геттеры
    public String getName() { return name; }
    public Queue<Task> getTasks() { return tasks; }
    public int getTotalWorkedHours() { return totalWorkedHours; }
    public int getIdleHours() { return idleHours; }
    public int getTotalPresenceHours() { return totalPresenceHours; }

    // Методы для учета времени
    public void addWorkedHours(int hours) { totalWorkedHours += hours; }
    public void addIdleHours(int hours) { idleHours += hours; }
    public void addPresenceHours(int hours) { totalPresenceHours += hours; }

    // Расчет эффективности
    public double getEfficiency() {
        if (totalPresenceHours == 0) return 0.0;
        return (double) totalWorkedHours / totalPresenceHours;
    }
}