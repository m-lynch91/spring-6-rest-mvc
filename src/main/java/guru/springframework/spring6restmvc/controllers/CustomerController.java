package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMERS_PATH = "/api/v1/customers";
    public static final String CUSTOMERS_PATH_ID = CUSTOMERS_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PatchMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<Customer> patchCustomerById(@PathVariable UUID customerId,
                                                      @RequestBody Customer customer) {
        customerService.patchCustomerById(customerId, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<Customer> updateCustomerById(@PathVariable("customerId") UUID customerId,
                                                       @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMERS_PATH)
    public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMERS_PATH + "/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMERS_PATH)
    public List<Customer> getAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMERS_PATH_ID)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        return customerService.getCustomerById(customerId);
    }
}
