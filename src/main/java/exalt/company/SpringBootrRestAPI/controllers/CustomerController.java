package exalt.company.SpringBootrRestAPI.controllers;

import exalt.company.SpringBootrRestAPI.models.Customer;
import exalt.company.SpringBootrRestAPI.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This Class will be as EndPoint for RestApI ( PUT , GET , .. )
 * and it will be used http://localhost:8080/customer/*
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {


    CustomerRepository customerRepositoryDB;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {

        this.customerRepositoryDB = customerRepository;
    }

    /**
     * This Method will receive GET Request as
     * http://localhost:8080/customer/all to get all Customers in DataBase
     *
     * @return Full Details Of Customer in DataBase.
     */
    @GetMapping("/all")
    public List<Customer> getCustomers() {
        return customerRepositoryDB.findAll();
    }


    /**
     * This Method will be used to receive GET Request as
     * http://localhost:8080/customer/1 to get full details about specific Customer
     * in DataBase using id
     *
     * @param id ID of Customer that we need to list his details
     * @return Full Details About Customer that we need
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Integer id) {

        if (!customerRepositoryDB.existsById(id)) {
            ResponseEntity<Customer> notFoundResponse = new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
            return notFoundResponse;
        }

        Customer customer = customerRepositoryDB.findById(id).get();
        ResponseEntity<Customer> successResponse = new ResponseEntity<Customer>(customer, HttpStatus.FOUND);
        return successResponse;
    }


    /**
     * This method is used to create New Customer in DataBase by
     * http://localhost:8080/customer/create
     *
     * @param customer Customer Object That we want to Create
     * @return Created Customer Object
     */
    @PostMapping(value = "/create")
    public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer customer) {

        int id = customer.getId();
        if (customerRepositoryDB.existsById(id)) {
            return new ResponseEntity<Customer>(HttpStatus.CONFLICT);
        }
        customerRepositoryDB.save(customer);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }


    /**
     * This method will update a customer that is already exist , so it is receive Patch request
     * as http://localhost:8080/customer/update/1
     *
     * @param customer Customer Object that contains Details That we need to Update
     * @param id       The ID of Customer That we want to Update him
     * @return Response Contains The Status that can be OK(200) if its done and
     * NOT_FOUND (404) if its failed since we do not have id enter in Customer Object
     */
    @PatchMapping("/update/{id}")
    public ResponseEntity updateExistCustomer(@RequestBody Customer customer
            , @PathVariable Integer id) {

        if (customerRepositoryDB.existsById(id)) {

            customer.setId(id);
            customerRepositoryDB.save(customer);
            return ResponseEntity.ok().build();

        } else {

            return ResponseEntity.notFound().build();
        }

    }


    /**
     * This Method will receive DELETE Request to delete A Customer that exist in DataBase
     * as http://localhost:8080/customer/delete/1
     *
     * @param id The ID of Customer that we need to delete his details
     * @return Response That either OK (200) if A Customer deleted with deleted Customer Details
     * Or NOT FOUND (404) if A Customer is Already Not Exist.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id) {

        if (!customerRepositoryDB.existsById(id)) {

            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        Customer deletedCustomer = customerRepositoryDB.findById(id).get();
        customerRepositoryDB.deleteById(id);
        return new ResponseEntity<Customer>(deletedCustomer, HttpStatus.OK);
    }

}
