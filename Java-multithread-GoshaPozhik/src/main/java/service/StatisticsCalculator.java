package service;

import model.Employee;
import java.util.List;

public class StatisticsCalculator {
    public static void printStatistics(List<Employee> employees) {
        System.out.println("--- СТАТИСТИКА ---");
        for (Employee e : employees) {
            System.out.printf("%s: Задачи = %d ч, Простой = %d ч, Всего = %d ч, Эффективность = %.1f%%\n",
                    e.getName(),
                    e.getTotalWorkedHours(),
                    e.getIdleHours(),
                    e.getTotalPresenceHours(),
                    e.getEfficiency() * 100
            );
        }
    }

    public static double calculateOverallEfficiency(List<Employee> employees) {
        int totalWorked = 0, totalPresence = 0;
        for (Employee e : employees) {
            totalWorked += e.getTotalWorkedHours();
            totalPresence += e.getTotalPresenceHours();
        }
        return totalPresence == 0 ? 0 : ((double) totalWorked) / totalPresence;
    }
}