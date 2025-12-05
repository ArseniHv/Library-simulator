public interface  OverdueObserver {
    void notifyOverdue(User user, LibraryItem item, int daysLate);
}
