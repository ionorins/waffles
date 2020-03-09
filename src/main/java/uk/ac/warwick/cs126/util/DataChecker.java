package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

public class DataChecker implements IDataChecker {

    public Long extractTrueID(String[] repeatedID) {
        if (repeatedID.length != 3)
            return null;
        // check repeated ids two by two
        if (repeatedID[0].equals(repeatedID[1]) || repeatedID[0].equals(repeatedID[2]))
            return Long.parseLong(repeatedID[0]);
        if (repeatedID[1].equals(repeatedID[2]))
            return Long.parseLong(repeatedID[1]);
        return null;
    }

    public boolean isValid(Long inputID) {
        if (inputID < 0)
            return false;

        int[] count = new int[10];
        int len = 0;

        while (inputID > 0) {
            len++;
            if (len > 16)
                return false;
            int digit = (int) (inputID % 10);
            count[digit]++;
            if (count[digit] > 3)
                return false;
            inputID /= 10;
        }

        return len == 16;
    }

    public boolean isValid(Customer customer) {
        return customer != null && customer.getID() != null && customer.getFirstName() != null
                && customer.getLastName() != null && customer.getDateJoined() != null && isValid(customer.getID());
    }

    public boolean isValid(Restaurant restaurant) {
        if (restaurant != null && restaurant.getRepeatedID() != null)
            restaurant.setID(extractTrueID(restaurant.getRepeatedID()));
        return restaurant != null && restaurant.getRepeatedID() != null && restaurant.getID() != null
                && restaurant.getName() != null && restaurant.getOwnerFirstName() != null
                && restaurant.getOwnerLastName() != null && restaurant.getCuisine() != null
                && restaurant.getEstablishmentType() != null && restaurant.getPriceRange() != null
                && restaurant.getDateEstablished() != null && restaurant.getLastInspectedDate() != null
                && isValid(restaurant.getID())
                && !restaurant.getDateEstablished().after(restaurant.getLastInspectedDate())
                && 0 <= restaurant.getFoodInspectionRating() && restaurant.getFoodInspectionRating() <= 5
                && 0 <= restaurant.getWarwickStars() && restaurant.getWarwickStars() <= 3
                && 0 <= restaurant.getCustomerRating() && restaurant.getCustomerRating() <= 5;
    }

    public boolean isValid(Favourite favourite) {
        return favourite != null && favourite.getID() != null && favourite.getCustomerID() != null
                && favourite.getCustomerID() != null && favourite.getDateFavourited() != null
                && isValid(favourite.getID()) && isValid(favourite.getCustomerID())
                && isValid(favourite.getRestaurantID());
    }

    public boolean isValid(Review review) {
        return review != null && review.getCustomerID() != null && review.getRestaurantID() != null
                && review.getDateReviewed() != null && review.getReview() != null && isValid(review.getID())
                && isValid(review.getCustomerID()) && isValid(review.getRestaurantID());
    }
}