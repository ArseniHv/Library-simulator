public final class Journal extends LibraryItem implements Loanable {
    private final String eissn;
    private final String publisher;
    private final String latestIssue;
    private final String url;

    public Journal(int id, String title, String eissn, String publisher, String latestIssue, String url) {
        super(id, title, 0);
        this.eissn = eissn;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.url = url;
    }

    @Override
    public int getLoanPeriod(User user) {
        return 14;
    }

    @Override
    public double getDailyOverdueFee() {
        return 1.0;
    }

    @Override
    protected int typeOrder() { return 2; }

    @Override
    protected String[] getSearchableFields() {
        return new String[] { getTitle(), eissn, publisher, latestIssue, url };
    }
}
