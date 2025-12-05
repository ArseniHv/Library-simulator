public interface Loanable {
    int getLoanPeriod(User user);
    double getDailyOverdueFee();
}
