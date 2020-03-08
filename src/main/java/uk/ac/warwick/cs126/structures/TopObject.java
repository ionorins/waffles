package uk.ac.warwick.cs126.structures;

public class TopObject {
    private long id;
    private long date;
    private int count;

    public TopObject(long id, int count, long date) {
        this.id = id;
        this.count = count;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public Long getDate() {
        return this.date;
    }

    public int getCount() {
        return this.count;
    }
}