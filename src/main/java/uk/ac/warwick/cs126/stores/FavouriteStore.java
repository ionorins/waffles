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
    private MyHashtable<String, MyTree<Long, Favourite>> history;
    private MyHashtable<Long, String> blacklist;
    private DataChecker dataChecker;
    private Lambda<Favourite> compId, compDate, compRestaurant;

    public FavouriteStore() {
        // Initialise variables here
        favouriteTree = new MyTree<Long, Favourite>();
        history = new MyHashtable<String, MyTree<Long, Favourite>>();
        blacklist = new MyHashtable<Long, String>();
        dataChecker = new DataChecker();

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
                return a.getRestaurantID().compareTo(b.getRestaurantID());
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

            history.add(key, new MyTree<>());
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
        if (blacklist.get(id) != null)
            return null;
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
        Favourite[] c1 = getFavouritesByCustomerID(customer1ID);
        Favourite[] c2 = getFavouritesByCustomerID(customer2ID);
        Algorithms.sort(c1, compRestaurant);
        Algorithms.sort(c2, compRestaurant);
        MyArrayList<Favourite> aux = new MyArrayList<>();
        int i = 0, j = 0;
        while (i < c1.length && j < c2.length) {
            if (c1[i].getRestaurantID() == c2[j].getRestaurantID()) {
                aux.add(c1[i].getDateFavourited().compareTo(c2[j].getDateFavourited()) > 0 ? c1[i] : c2[j]);
                i++;
                j++;
            } else if (c1[i].getRestaurantID() < c2[j].getRestaurantID())
                i++;
            else
                j++;
        }
        Favourite[] arr = toArray(aux);
        Algorithms.sort(arr, compDate);
        Long[] ret = new Long[arr.length];
        for (i = 0; i < ret.length; i++)
            ret[i] = arr[i].getRestaurantID();

        return ret;
        // return new Long[0];
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // MyArrayList<Long> c1 = favouriteTable.get(customer1ID).toArrayList();
        // MyArrayList<Long> c2 = favouriteTable.get(customer2ID).toArrayList();
        // MyArrayList<Long> aux = new MyArrayList<Long>();
        // int i = 0, j = 0;
        // while (i < c1.size() && j < c2.size()) {
        // while (blacklist.contains(c1.get(i)))
        // i++;
        // while (blacklist.contains(c2.get(j)))
        // j++;

        // if (c1.get(i) == c2.get(j)) {
        // i++;
        // j++;
        // } else if (c1.get(i) < c2.get(j))
        // aux.add(c1.get(i++));
        // else
        // j++;
        // }

        // return toArrayL(aux);
        return new Long[0];
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // MyArrayList<Long> c1 = favouriteTable.get(customer1ID).toArrayList();
        // MyArrayList<Long> c2 = favouriteTable.get(customer2ID).toArrayList();
        // MyArrayList<Long> aux = new MyArrayList<Long>();
        // int i = 0, j = 0;
        // while (i < c1.size() && j < c2.size()) {
        // while (blacklist.contains(c1.get(i)))
        // i++;
        // while (blacklist.contains(c2.get(j)))
        // j++;

        // if (c1.get(i) == c2.get(j)) {
        // i++;
        // j++;
        // } else if (c1.get(i) < c2.get(j))
        // aux.add(c1.get(i++));
        // else
        // aux.add(c2.get(j++));
        // }

        // while (i < c1.size())
        // aux.add(c1.get(i++));

        // while (j < c2.size())
        // aux.add(c2.get(j++));

        // return toArrayL(aux);
        return new Long[0];
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    private Favourite[] toArray(MyArrayList<Favourite> arr) {
        Favourite[] aux = new Favourite[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }

    private Long[] toArrayL(MyArrayList<Long> arr) {
        Long[] aux = new Long[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }
}
