package service;

import model.Employee;
import model.Task;

public class WorkDaySimulator implements Runnable {
    private static final int DAY_HOURS = 8;  // Длительность рабочего дня
    private final Employee employee;         // Сотрудник

    public WorkDaySimulator(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void run() {
        // Продолжаем работу, пока есть задачи
        while (!employee.getTasks().isEmpty()) {
            int workedToday = 0;  // Сколько часов уже отработано сегодня

            // Работаем, пока не закончится день или задачи
            while (workedToday < DAY_HOURS && !employee.getTasks().isEmpty()) {
                Task currentTask = employee.getTasks().peek();
                
                // Определяем, сколько часов можем потратить на текущую задачу
                int hoursToWork = Math.min(
                    currentTask.getHoursLeft(),           // Оставшееся время на задачу
                    DAY_HOURS - workedToday              // Оставшееся время в дне
                );

                // Учитываем отработанное время
                employee.addWorkedHours(hoursToWork);
                workedToday += hoursToWork;
                
                // Обновляем оставшееся время на задачу
                currentTask.setHoursLeft(currentTask.getHoursLeft() - hoursToWork);
                
                // Если задача выполнена, удаляем её из очереди
                if (currentTask.getHoursLeft() == 0) {
                    employee.getTasks().poll();
                }
            }

            // Учитываем время простоя
            int idleHours = DAY_HOURS - workedToday;
            if (idleHours > 0) {
                employee.addIdleHours(idleHours);
            }

            // Учитываем полный рабочий день
            employee.addPresenceHours(DAY_HOURS);

            // Имитация конца дня
            try { 
                Thread.sleep(50); 
            } catch (InterruptedException ignored) {}
        }
    }
}