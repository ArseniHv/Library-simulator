public class Journal extends LibraryItem {
    private String eissn;
    private String publisher;
    private String latestIssue;
    private String url;

    public Journal(int id, String title, String eissn, String publisher, String latestIssue, String url) {
        super(id, title);
        this.eissn = eissn;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.url = url;
    }

    @Override
    public int getLoanPeriodDaysForBorrower() {
        return 14;
    }

    @Override
    public double getFinePerDay() {
        return 1.0;
    }
}
