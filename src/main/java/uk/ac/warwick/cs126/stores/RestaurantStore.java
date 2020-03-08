package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;

import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class RestaurantStore implements IRestaurantStore {

    private MyTree<Long, Restaurant> restaurantTree;
    private MyHashtable<Long, Boolean> blacklist;
    private DataChecker dataChecker;
    private Lambda<Restaurant> idComp, nameComp, dateComp, starsComp, ratingComp;
    private Lambda<RestaurantDistance> distComp;
    private ConvertToPlace convertToPlace;

    public RestaurantStore() {
        restaurantTree = new MyTree<Long, Restaurant>();
        blacklist = new MyHashtable<Long, Boolean>();
        dataChecker = new DataChecker();
        convertToPlace = new ConvertToPlace();

        idComp = new Lambda<Restaurant>() {

            @Override
            public int call(Restaurant a, Restaurant b) {
                return a.getID().compareTo(b.getID());
            }
        };

        nameComp = new Lambda<Restaurant>() {

            @Override
            public int call(Restaurant a, Restaurant b) {
                if (a.getName().toLowerCase().equals(b.getName().toLowerCase()))
                    return idComp.call(a, b);
                return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
            }
        };

        dateComp = new Lambda<Restaurant>() {

            @Override
            public int call(Restaurant a, Restaurant b) {
                if (a.getDateEstablished().equals(b.getDateEstablished()))
                    return nameComp.call(a, b);
                return a.getDateEstablished().compareTo(b.getDateEstablished());
            }
        };

        starsComp = new Lambda<Restaurant>() {

            @Override
            public int call(Restaurant a, Restaurant b) {
                if (a.getWarwickStars() == b.getWarwickStars())
                    return nameComp.call(a, b);
                return b.getWarwickStars() - a.getWarwickStars();
            }
        };

        ratingComp = new Lambda<Restaurant>() {
            @Override
            public int call(Restaurant a, Restaurant b) {
                if (a.getCustomerRating() == b.getCustomerRating())
                    return nameComp.call(a, b);
                if (a.getCustomerRating() < b.getCustomerRating())
                    return 1;
                return -1;
            }
        };

        distComp = new Lambda<RestaurantDistance>() {

            @Override
            public int call(RestaurantDistance a, RestaurantDistance b) {
                Float distA = a.getDistance();
                Float distB = b.getDistance();

                if (distA.equals(distB))
                    return idComp.call(a.getRestaurant(), b.getRestaurant());
                else
                    return distA.compareTo(distB);
            }
        };
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

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

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(data[0], data[1], data[2], data[3], Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]), PriceRange.valueOf(data[6]), formatter.parse(data[7]),
                            Float.parseFloat(data[8]), Float.parseFloat(data[9]), Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]), Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]), formatter.parse(data[16]), Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    public boolean addRestaurant(Restaurant restaurant) {
        if (!dataChecker.isValid(restaurant) || blacklist.contains(restaurant.getID()))
            return false;

        // check if restaurant is already in tree
        Restaurant check = this.getRestaurant(restaurant.getID());
        if (check != null) {
            blacklist.add(restaurant.getID(), true);
            restaurantTree.remove(restaurant.getID());
            return false;
        }

        restaurantTree.add(restaurant.getID(), restaurant);
        return true;
    }

    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean success = true;
        for (Restaurant restaurant : restaurants) {
            success = this.addRestaurant(restaurant) && success;
        }

        return success;
    }

    public Restaurant getRestaurant(Long id) {
        return restaurantTree.search(id);
    }

    public Restaurant[] getRestaurants() {
        return toArray(restaurantTree.toArrayList());
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        Restaurant[] aux = new Restaurant[restaurants.length];
        for (int i = 0; i < restaurants.length; i++)
            aux[i] = restaurants[i];
        Algorithms.sort(aux, idComp);
        return aux;
    }

    public Restaurant[] getRestaurantsByName() {
        Restaurant[] aux = getRestaurants();
        Algorithms.sort(aux, nameComp);
        return aux;
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        Restaurant[] aux = getRestaurants();
        Algorithms.sort(aux, dateComp);
        return aux;
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        Restaurant[] aux = new Restaurant[restaurants.length];
        for (int i = 0; i < restaurants.length; i++)
            aux[i] = restaurants[i];
        Algorithms.sort(aux, dateComp);
        return aux;
    }

    public Restaurant[] getRestaurantsByWarwickStars() {

        MyArrayList<Restaurant> filtered = new MyArrayList<Restaurant>();
        MyArrayList<Restaurant> restaurants = restaurantTree.toArrayList();

        // filters out restaurants with 0 stars
        for (int i = 0; i < restaurants.size(); i++)
            if (restaurants.get(i).getWarwickStars() > 0)
                filtered.add(restaurants.get(i));

        Restaurant[] aux = toArray(filtered);
        Algorithms.sort(aux, starsComp);
        return aux;
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        Restaurant[] aux = new Restaurant[restaurants.length];
        for (int i = 0; i < restaurants.length; i++)
            aux[i] = restaurants[i];
        Algorithms.sort(aux, ratingComp);
        return aux;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        Restaurant[] restaurants = getRestaurants();
        RestaurantDistance[] aux = new RestaurantDistance[restaurants.length];

        // creates array with restaurants and distances form input coordinates
        for (int i = 0; i < restaurants.length; i++)
            aux[i] = new RestaurantDistance(restaurants[i], HaversineDistanceCalculator
                    .inKilometres(restaurants[i].getLatitude(), restaurants[i].getLongitude(), latitude, longitude));

        Algorithms.sort(aux, distComp);
        return aux;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude,
            float longitude) {
        RestaurantDistance[] aux = new RestaurantDistance[restaurants.length];

        // creates array with restaurants and distances form input coordinates
        for (int i = 0; i < restaurants.length; i++)
            aux[i] = new RestaurantDistance(restaurants[i], HaversineDistanceCalculator
                    .inKilometres(restaurants[i].getLatitude(), restaurants[i].getLongitude(), latitude, longitude));

        Algorithms.sort(aux, distComp);
        return aux;
    }

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        searchTerm = searchTerm.trim().replaceAll(" +", " ").toLowerCase();
        if (searchTerm.equals(""))
            return new Restaurant[0];

        String term = StringFormatter.convertAccentsFaster(searchTerm).toLowerCase();
        MyArrayList<Restaurant> restaurantArray = restaurantTree.toArrayList();
        MyArrayList<Restaurant> result = new MyArrayList<Restaurant>();

        // iterates through every restaurant
        for (int i = 0; i < restaurantArray.size(); i++) {
            Restaurant restaurant = restaurantArray.get(i);
            if (restaurant.getName().toLowerCase().contains(term)
                    || restaurant.getCuisine().toString().toLowerCase().contains(term)
                    || convertToPlace.convert(restaurant.getLatitude(), restaurant.getLongitude()).getName()
                            .toLowerCase().contains(term))
                result.add(restaurantArray.get(i));
        }

        Restaurant[] res = toArray(result);
        Algorithms.sort(res, nameComp);
        return res;
    }

    /**
     * Converts MyArrayList<Restaurant> to Restaurant[]
     * @param arr array to be converted
     * @return converted array
     */
    private Restaurant[] toArray(MyArrayList<Restaurant> arr) {
        Restaurant[] aux = new Restaurant[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }
}
