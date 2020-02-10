package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

    public boolean addCustomer(Customer customer) {
        // TODO
        return false;
    }

    public boolean addCustomer(Customer[] customers) {
        // TODO
        return false;
    }

    public Customer getCustomer(Long id) {
        // TODO
        return null;
    }

    public Customer[] getCustomers() {
        // TODO
        return new Customer[0];
    }

    public Customer[] getCustomers(Customer[] customers) {
        // TODO
        return new Customer[0];
    }

    public Customer[] getCustomersByName() {
        // TODO
        return new Customer[0];
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        // TODO
        return new Customer[0];
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Customer[0];
    }

}
