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

    private MyTree<Long, Customer> customerTree;
    private MyHashtable<Long, Boolean> blacklist;
    private DataChecker dataChecker;
    private Lambda<Customer> idComp, nameComp;

    public CustomerStore() {
        customerTree = new MyTree<Long, Customer>();
        blacklist = new MyHashtable<Long, Boolean>();
        dataChecker = new DataChecker();

        idComp = new Lambda<Customer>() {

            @Override
            public int call(Customer a, Customer b) {
                return a.getID().compareTo(b.getID());
            }
        };

        nameComp = new Lambda<Customer>() {

            @Override
            public int call(Customer a, Customer b) {
                if (a.getLastName().toLowerCase().equals(b.getLastName().toLowerCase()))
                    if (a.getFirstName().toLowerCase().equals(b.getFirstName().toLowerCase()))
                        return a.getID().compareTo(b.getID());
                    else
                        return a.getFirstName().toLowerCase().compareTo(b.getFirstName().toLowerCase());
                return a.getLastName().toLowerCase().compareTo(b.getLastName().toLowerCase());
            }
        };
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerTree = new Customer[0];

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

            customerTree = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerTree;
    }

    public boolean addCustomer(Customer customer) {
        if (!dataChecker.isValid(customer) || blacklist.contains(customer.getID()))
            return false;

        // check if customer is already in tree
        Customer check = this.getCustomer(customer.getID());
        if (check != null) {
            blacklist.add(customer.getID(), true);
            customerTree.remove(customer.getID());
            return false;
        }

        customerTree.add(customer.getID(), customer);
        return true;
    }

    public boolean addCustomer(Customer[] customers) {
        boolean success = true;
        for (Customer customer : customers) {
            success = this.addCustomer(customer) && success;
        }

        return success;
    }

    public Customer getCustomer(Long id) {
        return customerTree.search(id);
    }

    public Customer[] getCustomers() {
        return toArray(customerTree.toArrayList());
    }

    public Customer[] getCustomers(Customer[] customers) {
        Customer[] aux = new Customer[customers.length];
        for (int i = 0; i < customers.length; i++)
            aux[i] = customers[i];
        Algorithms.sort(aux, idComp);
        return aux;
    }

    public Customer[] getCustomersByName() {
        Customer[] aux = getCustomers();
        Algorithms.sort(aux, nameComp);
        return aux;
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        Customer[] aux = new Customer[customers.length];
        for (int i = 0; i < customers.length; i++)
            aux[i] = customers[i];
        Algorithms.sort(aux, nameComp);
        return aux;
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        if (searchTerm.equals(""))
            return new Customer[0];

        String term = StringFormatter.convertAccentsFaster(searchTerm).toLowerCase();
        MyArrayList<Customer> customerArray = customerTree.toArrayList();
        MyArrayList<Customer> result = new MyArrayList<Customer>();

        // iterate through every customer
        for (int i = 0; i < customerArray.size(); i++)
            if (customerArray.get(i).getLastName().toLowerCase().contains(term)
                    || customerArray.get(i).getFirstName().toLowerCase().contains(term))
                result.add(customerArray.get(i));

        Customer[] res = toArray(result);
        Algorithms.sort(res, nameComp);
        return res;
    }

    /**
     * Converts MyArrayList<Customer> to Customer[]
     * @param arr array to be converted
     * @return converted array
     */
    private Customer[] toArray(MyArrayList<Customer> arr) {
        Customer[] aux = new Customer[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            aux[i] = arr.get(i);
        }
        return aux;
    }
}
