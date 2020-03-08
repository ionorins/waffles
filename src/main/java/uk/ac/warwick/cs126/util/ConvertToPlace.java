package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.structures.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class ConvertToPlace implements IConvertToPlace {
    Place[] placesArray;
    MyHashtable<String, Place> placesTable;

    public ConvertToPlace() {
        placesArray = this.getPlacesArray();
        placesTable = new MyHashtable<String, Place>();

        for (Place place : placesArray)
            placesTable.add(stringCoords(place), place);
    }

    /**
     * Converts place coordinates to string representation
     *
     * @param place the place to be converted
     * @return string representation of the coordinates of the place taken as input
     */
    private String stringCoords(Place place) {
        String latitude = ((Float) place.getLatitude()).toString();
        String longitude = ((Float) place.getLongitude()).toString();
        return latitude + longitude;
    }

    /**
     * Converts floating point coordinates to string representation
     *
     * @param latitude  latitude of coordinates to be converted
     * @param longitude longitude of coordinates to be converted
     * @return string representation of the coordinates taken as input
     */
    private String stringCoords(Float latitude, Float longitude) {
        return latitude.toString() + longitude.toString();
    }

    public Place convert(float latitude, float longitude) {
        Place place = placesTable.get(stringCoords(latitude, longitude));

        if (place == null)
            return new Place("", "", 0.0f, 0.0f);
        return place;
    }

    public Place[] getPlacesArray() {
        Place[] placeArray = new Place[0];

        try {
            InputStream resource = ConvertToPlace.class.getResourceAsStream("/data/placeData.tsv");
            if (resource == null) {
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                String resourcePath = Paths.get(currentPath, "data", "placeData.tsv").toString();
                File resourceFile = new File(resourcePath);
                resource = new FileInputStream(resourceFile);
            }

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

            Place[] loadedPlaces = new Place[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int placeCount = 0;
            String row;

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Place place = new Place(data[0], data[1], Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    loadedPlaces[placeCount++] = place;
                }
            }
            tsvReader.close();

            placeArray = loadedPlaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeArray;
    }
}
