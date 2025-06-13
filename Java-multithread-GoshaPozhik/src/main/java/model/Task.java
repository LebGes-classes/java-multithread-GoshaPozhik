package model;

public class Task {
    private final String description;  // Описание задачи
    private int hoursLeft;            // Оставшееся время на выполнение

    public Task(String description, int hoursLeft) {
        this.description = description;
        this.hoursLeft = hoursLeft;
    }

    // Геттеры и сеттеры
    public String getDescription() { return description; }
    public int getHoursLeft() { return hoursLeft; }
    public void setHoursLeft(int hoursLeft) { this.hoursLeft = hoursLeft; }
}