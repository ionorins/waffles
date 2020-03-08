package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;

import uk.ac.warwick.cs126.util.DataChecker;

public class FavouriteStore implements IFavouriteStore {

    private MyTree<Long, Favourite> favouriteTree;
    private MyHashtable<Long, MyTree<Long, Favourite>> favouriteTable;
    private MyHashtable<String, MyTree<Long, Favourite>> history;
    private MyHashtable<Long, String> blacklist;
    private DataChecker dataChecker;
    private Lambda<Favourite> compId, compDate, compRestaurant;
    private Lambda<TopObject> compTop;
    private MyTree<Long, Long> restaurants, customers;

    public FavouriteStore() {
        // Initialise variables here
        favouriteTree = new MyTree<>();
        history = new MyHashtable<>();
        blacklist = new MyHashtable<>();
        favouriteTable = new MyHashtable<>();
        dataChecker = new DataChecker();
        restaurants = new MyTree<>();
        customers = new MyTree<>();

        compId = new Lambda<Favourite>() {

            @Override
            public int call(Favourite a, Favourite b) {
                return a.getID().compareTo(b.getCustomerID());
            }
        };

        compDate = new Lambda<Favourite>() {

            @Override
            public int call(Favourite a, Favourite b) {
                if (a.getDateFavourited().equals(b.getDateFavourited()))
                    return compId.call(a, b);
                return -a.getDateFavourited().compareTo(b.getDateFavourited());
            }
        };

        compRestaurant = new Lambda<Favourite>() {

            @Override
            public int call(Favourite a, Favourite b) {
                if (a.getDateFavourited().equals(b.getDateFavourited()))
                    return a.getRestaurantID().compareTo(b.getRestaurantID());
                return -a.getDateFavourited().compareTo(b.getDateFavourited());
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
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(Long.parseLong(data[0]), Long.parseLong(data[1]),
                            Long.parseLong(data[2]), formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    public boolean addFavourite(Favourite favourite) {
        // TODO
        if (!dataChecker.isValid(favourite) || blacklist.contains(favourite.getID()))
            return false;

        String key = favourite.getCustomerID().toString() + favourite.getRestaurantID().toString();

        Favourite check = this.getFavourite(favourite.getID());
        if (check != null) {
            blacklist.add(favourite.getID(), "dublicate");
            favouriteTree.remove(favourite.getID());
            if (history.get(key) != null) {
                MyArrayList<Favourite> aux = history.get(key).toArrayList();
                for (int i = 0; i < aux.size(); i++)
                    if (blacklist.get(aux.get(i).getID()).equals("toonew")) {
                        blacklist.add(aux.get(i).getID(), null);
                        favouriteTree.add(aux.get(i).getID(), aux.get(i));
                        break;
                    }
            }
            return false;
        }

        if (history.get(key) == null) {
            favouriteTree.add(favourite.getID(), favourite);

            Long date = customers.search(favourite.getCustomerID());
            if (date == null)
                customers.add(favourite.getCustomerID(), favourite.getDateFavourited().getTime());
            else if (date.compareTo(favourite.getDateFavourited().getTime()) > 0) {
                customers.remove(favourite.getCustomerID());
                customers.add(favourite.getCustomerID(), favourite.getDateFavourited().getTime());
            }

            date = restaurants.search(favourite.getRestaurantID());
            if (date == null)
                restaurants.add(favourite.getRestaurantID(), favourite.getDateFavourited().getTime());
            else if (date.compareTo(favourite.getDateFavourited().getTime()) > 0) {
                restaurants.remove(favourite.getRestaurantID());
                restaurants.add(favourite.getRestaurantID(), favourite.getDateFavourited().getTime());
            }

            history.add(key, new MyTree<>());

            if (!favouriteTable.contains(favourite.getCustomerID()))
                favouriteTable.add(favourite.getCustomerID(), new MyTree<Long, Favourite>());

            favouriteTable.get(favourite.getCustomerID()).add(favourite.getRestaurantID(), favourite);

            history.get(key).add(favourite.getDateFavourited().getTime(), favourite);
            return true;
        }

        MyArrayList<Favourite> arr = history.get(key).toArrayList();
        int i = 0;
        while (blacklist.get(arr.get(i).getID()) != null && blacklist.get(arr.get(i).getID()).equals("dublicate"))
            i++;
        Favourite oldest = arr.get(i);
        try {
            history.get(key).add(favourite.getDateFavourited().getTime(), favourite);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (favourite.getDateFavourited().getTime() < oldest.getDateFavourited().getTime()) {
            blacklist.add(oldest.getID(), "toonew");
            favouriteTree.remove(oldest.getID());
            favouriteTree.add(favourite.getID(), favourite);
        }

        return false;
    }

    public boolean addFavourite(Favourite[] favourites) {
        // TODO
        boolean success = true;
        for (Favourite favourite : favourites) {
            success = this.addFavourite(favourite) && success;
        }

        return success;
    }

    public Favourite getFavourite(Long id) {
        // TODO
        // if (blacklist.get(id) != null)
        //     return null;
        return favouriteTree.search(id);
    }

    public Favourite[] getFavourites() {
        // TODO
        return toArray(favouriteTree.toArrayList());
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // TODO
        MyArrayList<Favourite> aux = new MyArrayList<Favourite>();
        Favourite[] favourites = getFavourites();
        for (int i = 0; i < favourites.length; i++)
            if (favourites[i].getCustomerID().equals(id))
                aux.add(favourites[i]);
        if (aux.size() == 0)
            return new Favourite[0];
        Favourite[] ret = toArray(aux);
        Algorithms.sort(ret, compDate);
        return ret;
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // TODO
        MyArrayList<Favourite> aux = new MyArrayList<Favourite>();
        Favourite[] favourites = getFavourites();
        for (int i = 0; i < favourites.length; i++)
            if (favourites[i].getRestaurantID().equals(id))
                aux.add(favourites[i]);
        if (aux.size() == 0)
            return new Favourite[0];
        Favourite[] ret = toArray(aux);
        Algorithms.sort(ret, compDate);
        return ret;
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!favouriteTable.contains(customer1ID) || !favouriteTable.contains(customer2ID))
            return new Long[0];
        MyArrayList<Favourite> c1 = favouriteTable.get(customer1ID).toArrayList();
        MyArrayList<Favourite> c2 = favouriteTable.get(customer2ID).toArrayList();
        if (c1.size() == 0 || c2.size() == 0)
            return new Long[0];

        MyArrayList<Favourite> aux = new MyArrayList<>();
        int i = 0, j = 0;
        while (i < c1.size() && j < c2.size()) {
            while (i < c1.size() && blacklist.contains(c1.get(i).getID()))
                i++;
            while (j < c2.size() && blacklist.contains(c2.get(j).getID()))
                j++;

            if (i == c1.size() || j == c2.size())
                break;

            if (c1.get(i) == c2.get(j)) {
                aux.add(c1.get(i).getDateFavourited().compareTo(c2.get(j).getDateFavourited()) > 0 ? c1.get(i)
                        : c2.get(j));
                i++;
                j++;
            } else if (c1.get(i).getRestaurantID() < c2.get(j).getRestaurantID())
                i++;
            else
                j++;
        }

        if (aux.size() == 0)
            return new Long[0];

        Favourite[] arr = toArray(aux);
        Algorithms.sort(arr, compRestaurant);
        Long[] ret = new Long[arr.length];

        for (i = 0; i < ret.length; i++)
            ret[i] = arr[i].getRestaurantID();
        return ret;
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!favouriteTable.contains(customer1ID) || !favouriteTable.contains(customer2ID))
            return new Long[0];
        MyArrayList<Favourite> c1 = favouriteTable.get(customer1ID).toArrayList();
        MyArrayList<Favourite> c2 = favouriteTable.get(customer2ID).toArrayList();
        if (c1.size() == 0 || c2.size() == 0)
            return new Long[0];

        MyArrayList<Favourite> aux = new MyArrayList<>();
        int i = 0, j = 0;
        while (i < c1.size() && j < c2.size()) {
            while (i < c1.size() && blacklist.contains(c1.get(i).getID()))
                i++;
            while (j < c2.size() && blacklist.contains(c2.get(j).getID()))
                j++;

            if (i == c1.size() || j == c2.size())
                break;

            if (c1.get(i).getRestaurantID() == c2.get(j).getRestaurantID()) {
                i++;
                j++;
            } else if (c1.get(i).getRestaurantID() < c2.get(j).getRestaurantID())
                aux.add(c1.get(i++));
            else
                j++;
        }

        if (aux.size() == 0)
            return new Long[0];

        Favourite[] arr = toArray(aux);
        Algorithms.sort(arr, compRestaurant);
        Long[] ret = new Long[arr.length];

        for (i = 0; i < ret.length; i++)
            ret[i] = arr[i].getRestaurantID();
        return ret;
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (!favouriteTable.contains(customer1ID) || !favouriteTable.contains(customer2ID))
            return new Long[0];
        MyArrayList<Favourite> c1 = favouriteTable.get(customer1ID).toArrayList();
        MyArrayList<Favourite> c2 = favouriteTable.get(customer2ID).toArrayList();
        if (c1.size() == 0 || c2.size() == 0)
            return new Long[0];

        MyArrayList<Favourite> aux = new MyArrayList<>();
        int i = 0, j = 0;
        while (i < c1.size() && j < c2.size()) {
            while (i < c1.size() && blacklist.contains(c1.get(i).getID()))
                i++;
            while (j < c2.size() && blacklist.contains(c2.get(j).getID()))
                j++;

            if (i == c1.size() || j == c2.size())
                break;

            if (c1.get(i).getRestaurantID() == c2.get(j).getRestaurantID()) {
                i++;
                j++;
            } else if (c1.get(i).getRestaurantID() < c2.get(j).getRestaurantID())
                aux.add(c1.get(i++));
            else
                aux.add(c2.get(j++));
        }

        while (i < c1.size())
            aux.add(c1.get(i++));

        while (j < c2.size())
            aux.add(c2.get(j++));

        if (aux.size() == 0)
            return new Long[0];

        Favourite[] arr = toArray(aux);
        Algorithms.sort(arr, compRestaurant);
        Long[] ret = new Long[arr.length];

        for (i = 0; i < ret.length; i++)
            ret[i] = arr[i].getRestaurantID();
        return ret;
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        MyHashtable<Long, Integer> customerFavourites = new MyHashtable<>();

        Favourite[] favourites = getFavourites();

        for (Favourite favourite : favourites) {
            if (!customerFavourites.contains(favourite.getCustomerID()))
                customerFavourites.add(favourite.getCustomerID(), 0);
            customerFavourites.add(favourite.getCustomerID(), customerFavourites.get(favourite.getCustomerID()) + 1);
        }

        MyArrayList<Long> ids = customers.toArrayListofKeys();
        MyArrayList<Long> dates = customers.toArrayList();

        MyArrayList<TopObject> top = new MyArrayList<>();

        for (int i = 0; i < ids.size(); i++)
            if (customerFavourites.contains(ids.get(i)))
                top.add(new TopObject(ids.get(i), customerFavourites.get(ids.get(i)), dates.get(i)));

        TopObject[] topArr = new TopObject[top.size()];
        for (int i = 0; i < topArr.length; i++)
            topArr[i] = top.get(i);

        Algorithms.sort(topArr, compTop);

        Long[] ret = new Long[20];

        for (int i = 0; i < 20; i++)
            ret[i] = topArr[i].getId();

        return ret;
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        MyHashtable<Long, Integer> restaurantFavourites = new MyHashtable<>();

        Favourite[] favourites = getFavourites();

        for (Favourite favourite : favourites) {
            if (!restaurantFavourites.contains(favourite.getRestaurantID()))
                restaurantFavourites.add(favourite.getRestaurantID(), 0);
            restaurantFavourites.add(favourite.getRestaurantID(),
                    restaurantFavourites.get(favourite.getRestaurantID()) + 1);
        }

        MyArrayList<Long> ids = restaurants.toArrayListofKeys();
        MyArrayList<Long> dates = restaurants.toArrayList();

        MyArrayList<TopObject> top = new MyArrayList<>();

        for (int i = 0; i < ids.size(); i++)
            if (restaurantFavourites.contains(ids.get(i)))
                top.add(new TopObject(ids.get(i), restaurantFavourites.get(ids.get(i)), dates.get(i)));

        TopObject[] topArr = new TopObject[top.size()];
        for (int i = 0; i < topArr.length; i++)
            topArr[i] = top.get(i);

        Algorithms.sort(topArr, compTop);

        Long[] ret = new Long[20];

        for (int i = 0; i < 20; i++)
            ret[i] = topArr[i].getId();

        return ret;
    }

    private Favourite[] toArray(MyArrayList<Favourite> arr) {
        Favourite[] aux = new Favourite[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }
}
