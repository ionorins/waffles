package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;

import uk.ac.warwick.cs126.util.DataChecker;

public class FavouriteStore implements IFavouriteStore {

    private MyTree<Long, Favourite> favouriteTree;
    private MyHashtable<Long, MyTree<Long, Long>> favouriteTable;
    private MyHashtable<Long, Boolean> blacklist;
    private DataChecker dataChecker;

    public FavouriteStore() {
        // Initialise variables here
        favouriteTree = new MyTree<Long, Favourite>();
        favouriteTable = new MyHashtable<Long, MyTree<Long, Long>>();
        favouriteTable = new MyHashtable<Long, MyTree<Long, Long>>();
        blacklist = new MyHashtable<Long, Boolean>();
        dataChecker = new DataChecker();
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

        Favourite check = this.getFavourite(favourite.getID());
        if (check != null) {
            blacklist.add(favourite.getID(), true);
            // favouriteTree.get(favourite)
            return false;
        }

        favouriteTree.add(favourite.getID(), favourite);

        if (!favouriteTable.contains(favourite.getCustomerID()))
            favouriteTable.add(favourite.getCustomerID(), new MyTree<Long, Long>());

        try {
            favouriteTable.get(favourite.getCustomerID()).add(favourite.getRestaurantID(), favourite.getRestaurantID());
        } catch (Exception e) {
            System.out.println(e);
        }

        return true;
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
        return favouriteTree.search(id);
    }

    public Favourite[] getFavourites() {
        // TODO
        return (Favourite[]) toArray(favouriteTree.toArrayList());
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // TODO
        // sort
        MyArrayList<Favourite> aux = new MyArrayList<Favourite>();
        Favourite[] favourites = getFavourites();
        for (int i = 0; i < favourites.length; i++)
            if (favourites[i].getCustomerID().equals(id))
                aux.add(favourites[i]);
        return toArray(aux);
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // TODO
        // sort
        MyArrayList<Favourite> aux = new MyArrayList<Favourite>();
        Favourite[] favourites = getFavourites();
        for (int i = 0; i < favourites.length; i++)
            if (favourites[i].getRestaurantID().equals(id))
                aux.add(favourites[i]);
        return toArray(aux);
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // MyArrayList<Long> c1 = favouriteTable.get(customer1ID).toArrayList();
        // MyArrayList<Long> c2 = favouriteTable.get(customer2ID).toArrayList();
        // MyArrayList<Long> aux = new MyArrayList<Long>();
        // int i = 0, j = 0;
        // while (i < c1.size() && j < c2.size()) {
        //     while (blacklist.contains(c1.get(i)))
        //         i++;
        //     while (blacklist.contains(c2.get(j)))
        //         j++;

        //     if (c1.get(i) == c2.get(j)) {
        //         aux.add(c1.get(i));
        //         i++;
        //         j++;
        //     } else if (c1.get(i) < c2.get(j))
        //         i++;
        //     else
        //         j++;
        // }

        // return toArrayL(aux);
        return new Long[0];
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // MyArrayList<Long> c1 = favouriteTable.get(customer1ID).toArrayList();
        // MyArrayList<Long> c2 = favouriteTable.get(customer2ID).toArrayList();
        // MyArrayList<Long> aux = new MyArrayList<Long>();
        // int i = 0, j = 0;
        // while (i < c1.size() && j < c2.size()) {
        //     while (blacklist.contains(c1.get(i)))
        //         i++;
        //     while (blacklist.contains(c2.get(j)))
        //         j++;

        //     if (c1.get(i) == c2.get(j)) {
        //         i++;
        //         j++;
        //     } else if (c1.get(i) < c2.get(j))
        //         aux.add(c1.get(i++));
        //     else
        //         j++;
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
        //     while (blacklist.contains(c1.get(i)))
        //         i++;
        //     while (blacklist.contains(c2.get(j)))
        //         j++;

        //     if (c1.get(i) == c2.get(j)) {
        //         i++;
        //         j++;
        //     } else if (c1.get(i) < c2.get(j))
        //         aux.add(c1.get(i++));
        //     else
        //         aux.add(c2.get(j++));
        // }

        // while (i < c1.size())
        //     aux.add(c1.get(i++));

        // while (j < c2.size())
        //     aux.add(c2.get(j++));

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
