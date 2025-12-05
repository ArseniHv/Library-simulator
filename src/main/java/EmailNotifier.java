public final class EmailNotifier implements OverdueObserver {
    @Override
    public void notifyOverdue(User user, LibraryItem item, int daysLate) {
        String userId = (user == null) ? "unknown" : String.valueOf(user.getId());
        System.out.println("[EmailNotifier] User " + userId + ": '" + item.getTitle() + "' is overdue by " + daysLate + " days.");
    }
}