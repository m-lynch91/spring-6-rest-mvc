package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.model.CustomerDTO;
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

	// ---------------------- CREATE ROUTES ----------------------//
	@PostMapping(CUSTOMERS_PATH)
	public ResponseEntity<CustomerDTO> postCustomer(@RequestBody CustomerDTO customer) {
		CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", CUSTOMERS_PATH + "/" + savedCustomer.getId().toString());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	// ---------------------- READ ROUTES ------------------------//
	@GetMapping(CUSTOMERS_PATH)
	public List<CustomerDTO> getAllCustomers() {
		return customerService.getCustomers();
	}

	@GetMapping(CUSTOMERS_PATH_ID)
	public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
		return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
	}

	// ---------------------- UPDATE ROUTES ----------------------//
	@PutMapping(CUSTOMERS_PATH_ID)
	public ResponseEntity<CustomerDTO> updateCustomerById(@PathVariable("customerId") UUID customerId,
			@RequestBody CustomerDTO customer) {
		if (customerService.updateCustomerById(customerId, customer).isEmpty())
			throw new NotFoundException();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping(CUSTOMERS_PATH_ID)
	public ResponseEntity<CustomerDTO> patchCustomerById(@PathVariable UUID customerId,
			@RequestBody CustomerDTO customer) {
		customerService.patchCustomerById(customerId, customer);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ---------------------- DELETE ROUTES ----------------------//
	@DeleteMapping(CUSTOMERS_PATH_ID)
	public ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
		if (!customerService.deleteCustomerById(customerId))
			throw new NotFoundException();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
