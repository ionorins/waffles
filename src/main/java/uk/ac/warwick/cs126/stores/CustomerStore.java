package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyTree<Long, Customer> customerArray;
    private MyHashtable<Long, Boolean> blacklist;
    private DataChecker dataChecker;
    private Lambda<Customer> idComp, nameComp;

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyTree<Long, Customer>();
        blacklist = new MyHashtable<Long, Boolean>();
        dataChecker = new DataChecker();

        idComp = new Lambda<Customer>() {

            @Override
            public int call(Customer a, Customer b) {
                long aID = a.getID(), bID = b.getID();

                if (aID < bID)
                    return -1;
                if (aID == bID)
                    return 0;
                return 1;
            }
        };

        nameComp = new Lambda<Customer>() {

            @Override
            public int call(Customer a, Customer b) {
                if (a.getLastName().equals(b.getLastName()))
                    return a.getFirstName().compareTo(b.getFirstName());
                return a.getLastName().compareTo(b.getLastName());
            }
        };
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

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

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(Long.parseLong(data[0]), data[1], data[2],
                            formatter.parse(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5])));

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
        if (!dataChecker.isValid(customer) || blacklist.contains(customer.getID()))
            return false;

        Customer check = this.getCustomer(customer.getID());
        if (check != null) {
            blacklist.add(customer.getID(), true);
            customerArray.remove(customer.getID());
            return false;
        }

        customerArray.add(customer.getID(), customer);
        return true;
    }

    public boolean addCustomer(Customer[] customers) {
        // TODO
        Boolean success = true;
        for (Customer customer : customers) {
            success = this.addCustomer(customer) && success;
        }

        return success;
    }

    public Customer getCustomer(Long id) {
        // TODO
        return customerArray.search(id);
    }

    public Customer[] getCustomers() {
        // TODO
        return (Customer[]) toArray(customerArray.toArrayList());
    }

    public Customer[] getCustomers(Customer[] customers) {
        // TODO
        Customer[] aux = new Customer[customers.length];
        for (int i = 0; i < customers.length; i++)
            aux[i] = customers[i];
        Algorithms.sort(aux, idComp);
        return aux;
    }

    public Customer[] getCustomersByName() {
        // TODO
        Customer[] aux = toArray(customerArray.toArrayList());
        Algorithms.sort(aux, nameComp);
        return aux;
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        // TODO
        Customer[] aux = new Customer[customers.length];
        for (int i = 0; i < customers.length; i++)
            aux[i] = customers[i];
        Algorithms.sort(aux, nameComp);
        return aux;
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster =
        // stringFormatter.convertAccentsFaster(searchTerm);
        return new Customer[0];
    }

    private Customer[] toArray(MyArrayList<Customer> arr) {
        Customer[] aux = new Customer[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }

}
