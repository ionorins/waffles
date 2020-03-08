package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class ReviewStore implements IReviewStore {

    private MyTree<Long, Review> reviewTree;
    private MyHashtable<Long, MyTree<Long, Review>> reviewTable;
    private MyHashtable<String, MyTree<Long, Review>> history;
    private MyHashtable<Long, String> blacklist;
    private DataChecker dataChecker;
    private Lambda<Review> compId, compDate, compRating;
    private Lambda<TopObject> compTop, compTopRating;
    private MyTree<Long, Long> restaurants, customers;
    private KeywordChecker keywordChecker;

    public ReviewStore() {
        // Initialise variables here
        reviewTree = new MyTree<>();
        history = new MyHashtable<>();
        blacklist = new MyHashtable<>();
        reviewTable = new MyHashtable<>();
        dataChecker = new DataChecker();
        keywordChecker = new KeywordChecker();
        restaurants = new MyTree<>();
        customers = new MyTree<>();

        compId = new Lambda<Review>() {

            @Override
            public int call(Review a, Review b) {
                return a.getID().compareTo(b.getCustomerID());
            }
        };

        compDate = new Lambda<Review>() {

            @Override
            public int call(Review a, Review b) {
                if (a.getDateReviewed().equals(b.getDateReviewed()))
                    return compId.call(a, b);
                return -a.getDateReviewed().compareTo(b.getDateReviewed());
            }
        };

        compRating = new Lambda<Review>() {

            @Override
            public int call(Review a, Review b) {
                if (a.getRating() == b.getRating())
                    return compDate.call(a, b);
                return b.getRating() - a.getRating();
            }
        };

        compTop = new Lambda<TopObject>() {

            @Override
            public int call(TopObject a, TopObject b) {
                if (a.getCount() == b.getCount()) {
                    if (a.getDate().equals(b.getDate()))
                        return a.getId().compareTo(b.getId());
                    return b.getDate().compareTo(a.getDate());
                }
                return b.getCount() - a.getCount();
            }
        };

        compTopRating = new Lambda<TopObject>() {

            @Override
            public int call(TopObject a, TopObject b) {
                if (a.getRating().equals(b.getRating())) {
                    if (a.getDate().equals(b.getDate()))
                        return a.getId().compareTo(b.getId());
                    return b.getDate().compareTo(a.getDate());
                }
                return b.getRating().compareTo(a.getRating());
            }
        };
    }

    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(Long.parseLong(data[0]), Long.parseLong(data[1]),
                            Long.parseLong(data[2]), formatter.parse(data[3]), data[4], Integer.parseInt(data[5]));
                    loadedReviews[reviewCount++] = review;
                }
            }
            tsvReader.close();

            reviewArray = loadedReviews;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return reviewArray;
    }

    public boolean addReview(Review review) {
        // TODO
        if (!dataChecker.isValid(review) || blacklist.contains(review.getID()))
            return false;

        String key = review.getCustomerID().toString() + review.getRestaurantID().toString();

        Review check = this.getReview(review.getID());
        if (check != null) {
            blacklist.add(review.getID(), "dublicate");
            reviewTree.remove(review.getID());
            if (history.get(key) != null) {
                MyArrayList<Review> aux = history.get(key).toArrayList();
                for (int i = 0; i < aux.size(); i++)
                    if (blacklist.get(aux.get(i).getID()).equals("toonew")) {
                        blacklist.add(aux.get(i).getID(), null);
                        reviewTree.add(aux.get(i).getID(), aux.get(i));
                        break;
                    }
            }
            return false;
        }

        if (history.get(key) == null) {
            reviewTree.add(review.getID(), review);

            Long date = customers.search(review.getCustomerID());
            if (date == null)
                customers.add(review.getCustomerID(), review.getDateReviewed().getTime());
            else if (date.compareTo(review.getDateReviewed().getTime()) > 0) {
                customers.remove(review.getCustomerID());
                customers.add(review.getCustomerID(), review.getDateReviewed().getTime());
            }

            date = restaurants.search(review.getRestaurantID());
            if (date == null)
                restaurants.add(review.getRestaurantID(), review.getDateReviewed().getTime());
            else if (date.compareTo(review.getDateReviewed().getTime()) > 0) {
                restaurants.remove(review.getRestaurantID());
                restaurants.add(review.getRestaurantID(), review.getDateReviewed().getTime());
            }

            history.add(key, new MyTree<>());

            if (!reviewTable.contains(review.getCustomerID()))
                reviewTable.add(review.getCustomerID(), new MyTree<Long, Review>());

            reviewTable.get(review.getCustomerID()).add(review.getRestaurantID(), review);

            history.get(key).add(review.getDateReviewed().getTime(), review);
            return true;
        }

        MyArrayList<Review> arr = history.get(key).toArrayList();
        int i = 0;
        while (blacklist.get(arr.get(i).getID()) != null && blacklist.get(arr.get(i).getID()).equals("dublicate"))
            i++;
        Review oldest = arr.get(i);
        try {
            history.get(key).add(review.getDateReviewed().getTime(), review);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (review.getDateReviewed().getTime() < oldest.getDateReviewed().getTime()) {
            blacklist.add(oldest.getID(), "toonew");
            reviewTree.remove(oldest.getID());
            reviewTree.add(review.getID(), review);
        }

        return false;
    }

    public boolean addReview(Review[] reviews) {
        // TODO
        boolean success = true;
        for (Review review : reviews) {
            success = this.addReview(review) && success;
        }

        return success;
    }

    public Review getReview(Long id) {
        // TODO
        return reviewTree.search(id);
    }

    public Review[] getReviews() {
        // TODO
        return toArray(reviewTree.toArrayList());
    }

    public Review[] getReviewsByDate() {
        // TODO
        Review[] aux = toArray(reviewTree.toArrayList());
        Algorithms.sort(aux, compDate);
        return aux;
    }

    public Review[] getReviewsByRating() {
        // TODO
        Review[] aux = toArray(reviewTree.toArrayList());
        Algorithms.sort(aux, compRating);
        return aux;
    }

    public Review[] getReviewsByCustomerID(Long id) {
        // TODO
        MyArrayList<Review> aux = new MyArrayList<Review>();
        Review[] reviews = toArray(reviewTree.toArrayList());
        for (int i = 0; i < reviews.length; i++)
            if (reviews[i].getCustomerID().equals(id))
                aux.add(reviews[i]);
        if (aux.size() == 0)
            return new Review[0];
        Review[] ret = toArray(aux);
        Algorithms.sort(ret, compDate);
        return ret;
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        // TODO
        MyArrayList<Review> aux = new MyArrayList<Review>();
        Review[] reviews = toArray(reviewTree.toArrayList());
        for (int i = 0; i < reviews.length; i++)
            if (reviews[i].getRestaurantID().equals(id))
                aux.add(reviews[i]);
        if (aux.size() == 0)
            return new Review[0];
        Review[] ret = toArray(aux);
        Algorithms.sort(ret, compDate);
        return ret;
    }

    public float getAverageCustomerReviewRating(Long id) {
        // TODO
        int sum = 0, k = 0;
        for (Review review : toArray(reviewTree.toArrayList()))
            if (review.getCustomerID().equals(id)) {
                sum += review.getRating();
                k++;
            }

        if (k == 0)
            return 0;
        return (sum * 10 / k) / 10f;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        // TODO
        int sum = 0, k = 0;
        for (Review review : toArray(reviewTree.toArrayList()))
            if (review.getRestaurantID().equals(id)) {
                sum += review.getRating();
                k++;
            }

        if (k == 0)
            return 0;
        return (sum * 10 / k) / 10f;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        // TODO
        int[] res = new int[5];

        for (Review review : toArray(reviewTree.toArrayList()))
            if (review.getCustomerID().equals(id))
                res[review.getRating() - 1]++;

        return res;
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        // TODO
        int[] res = new int[5];

        for (Review review : toArray(reviewTree.toArrayList()))
            if (review.getRestaurantID().equals(id))
                res[review.getRating() - 1]++;

        return res;
    }

    public Long[] getTopCustomersByReviewCount() {
        // TODO
        MyHashtable<Long, Integer> customerReviews = new MyHashtable<>();

        Review[] reviews = toArray(reviewTree.toArrayList());

        for (Review review : reviews) {
            if (!customerReviews.contains(review.getCustomerID()))
                customerReviews.add(review.getCustomerID(), 0);
            customerReviews.add(review.getCustomerID(), customerReviews.get(review.getCustomerID()) + 1);
        }

        MyArrayList<Long> ids = customers.toArrayListofKeys();
        MyArrayList<Long> dates = customers.toArrayList();

        MyArrayList<TopObject> top = new MyArrayList<>();

        for (int i = 0; i < ids.size(); i++)
            if (customerReviews.contains(ids.get(i)))
                top.add(new TopObject(ids.get(i), customerReviews.get(ids.get(i)), dates.get(i)));

        TopObject[] topArr = new TopObject[top.size()];
        for (int i = 0; i < topArr.length; i++)
            topArr[i] = top.get(i);

        Algorithms.sort(topArr, compTop);

        Long[] ret = new Long[20];

        for (int i = 0; i < 20; i++)
            ret[i] = topArr[i].getId();

        return ret;
    }

    public Long[] getTopRestaurantsByReviewCount() {
        // TODO
        MyHashtable<Long, Integer> restaurantReviews = new MyHashtable<>();

        Review[] reviews = toArray(reviewTree.toArrayList());

        for (Review review : reviews) {
            if (!restaurantReviews.contains(review.getRestaurantID()))
                restaurantReviews.add(review.getRestaurantID(), 0);
            restaurantReviews.add(review.getRestaurantID(), restaurantReviews.get(review.getRestaurantID()) + 1);
        }

        MyArrayList<Long> ids = restaurants.toArrayListofKeys();
        MyArrayList<Long> dates = restaurants.toArrayList();

        MyArrayList<TopObject> top = new MyArrayList<>();

        for (int i = 0; i < ids.size(); i++)
            if (restaurantReviews.contains(ids.get(i)))
                top.add(new TopObject(ids.get(i), restaurantReviews.get(ids.get(i)), dates.get(i)));

        TopObject[] topArr = new TopObject[top.size()];
        for (int i = 0; i < topArr.length; i++)
            topArr[i] = top.get(i);

        Algorithms.sort(topArr, compTop);

        Long[] ret = new Long[20];

        for (int i = 0; i < 20; i++)
            ret[i] = topArr[i].getId();

        return ret;
    }

    public Long[] getTopRatedRestaurants() {
        // TODO
        MyHashtable<Long, Integer> restaurantReviewSum = new MyHashtable<>();
        MyHashtable<Long, Integer> restaurantReviewCount = new MyHashtable<>();

        Review[] reviews = toArray(reviewTree.toArrayList());

        for (Review review : reviews) {
            if (!restaurantReviewCount.contains(review.getRestaurantID())) {
                restaurantReviewSum.add(review.getRestaurantID(), 0);
                restaurantReviewCount.add(review.getRestaurantID(), 0);
            }
            restaurantReviewSum.add(review.getRestaurantID(), restaurantReviewSum.get(review.getRestaurantID()) + review.getRating());
            restaurantReviewCount.add(review.getRestaurantID(), restaurantReviewCount.get(review.getRestaurantID()) + 1);
        }

        MyArrayList<Long> ids = restaurants.toArrayListofKeys();
        MyArrayList<Long> dates = restaurants.toArrayList();

        MyArrayList<TopObject> top = new MyArrayList<>();

        for (int i = 0; i < ids.size(); i++)
            if (restaurantReviewCount.contains(ids.get(i))) {
                double rating;
                if (restaurantReviewCount.get(ids.get(i)) == 0)
                    rating = 0;
                else
                    rating = restaurantReviewSum.get(ids.get(i)) / (double) restaurantReviewCount.get(ids.get(i));
                top.add(new TopObject(ids.get(i), rating, dates.get(i)));
            }

        TopObject[] topArr = new TopObject[top.size()];
        for (int i = 0; i < topArr.length; i++)
            topArr[i] = top.get(i);

        Algorithms.sort(topArr, compTopRating);

        Long[] ret = new Long[20];

        for (int i = 0; i < 20; i++)
            ret[i] = topArr[i].getId();

        return ret;
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        // TODO
        String[] words = keywordChecker.getKeywords();
        MyHashtable<String, Integer> wordTable = new MyHashtable<>();

        for (String word : words)
            wordTable.add(word, 0);

        Review[] reviews = toArray(reviewTree.toArrayList());
        for (int i = 0; i < reviews.length; i++)
            if (reviews[i].getRestaurantID().equals(id))
                for (String word : reviews[i].getReview().split("\\W+")) {
                    word = word.toLowerCase();
                    if (keywordChecker.isAKeyword(word))
                        wordTable.add(word, wordTable.get(word) + 1);
                }

        MyKeyValuePair<String, Integer>[] top = new MyKeyValuePair[words.length];

        for (int i = 0; i < top.length; i++)
            top[i] = new MyKeyValuePair<String,Integer>(words[i], wordTable.get(words[i]));

        Lambda<MyKeyValuePair<String, Integer>> topComp = new Lambda<MyKeyValuePair<String,Integer>>(){

            @Override
            public int call(MyKeyValuePair<String, Integer> a, MyKeyValuePair<String, Integer> b) {
                if (a.getValue().equals(b.getValue()))
                    return a.getKey().compareTo(b.getKey());
                return b.getValue().compareTo(a.getValue());
            }
        };

        Algorithms.sort(top, topComp);

        String[] top5 = new String[5];

        for (int i = 0; i < 5; i++)
            if (top[i].getValue() > 0)
                top5[i] = top[i].getKey();

        return top5;
    }

    public Review[] getReviewsContaining(String searchTerm) {
        // TODO
        searchTerm = searchTerm.trim().replaceAll(" +", " ").toLowerCase();
        if (searchTerm.equals(""))
            return new Review[0];

        String term = StringFormatter.convertAccentsFaster(searchTerm);
        MyArrayList<Review> reviewArray = reviewTree.toArrayList();
        MyArrayList<Review> result = new MyArrayList<Review>();

        for (int i = 0; i < reviewArray.size(); i++) {
            Review review = reviewArray.get(i);
            if (review.getReview().toLowerCase().contains(term))
                result.add(reviewArray.get(i));
        }

        Review[] res = toArray(result);
        Algorithms.sort(res, compDate);
        return res;
    }

    private Review[] toArray(MyArrayList<Review> arr) {
        Review[] aux = new Review[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }
}
