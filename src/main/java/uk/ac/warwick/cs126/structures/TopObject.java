package uk.ac.warwick.cs126.structures;

public class TopObject {
    private long id;
    private long date;
    private int count;
    private double rating;

    public TopObject(long id, int count, long date) {
        this.id = id;
        this.count = count;
        this.date = date;
    }

    public TopObject(long id, double rating, long date) {
        this.id = id;
        this.rating = rating;
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

    public Double getRating() {
        return this.rating;
    }
}