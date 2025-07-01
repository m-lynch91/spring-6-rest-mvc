package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerIntegrationTest {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;
    @Autowired
    private BeerRepository beerRepository;

    @BeforeEach
    void setUp() {
        // setting up mockMvc environment with spring data repositories injected into service
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    //---------------------- CREATE TESTS ----------------------//
    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("New Cust")
                .build();

        ResponseEntity responseEntity = customerController.postCustomer(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertNotNull(responseEntity.getHeaders().getLocation());

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer savedCustomer = customerRepository.findById(savedUUID).get();
        assertNotNull(savedCustomer);
    }

    //---------------------- READ TESTS ------------------------//
    @Test
    void testGetAllCustomers() {
        List<CustomerDTO> dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(5);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertNotNull(customerDTO);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyCustomerList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    //---------------------- UPDATE TESTS ----------------------//

    @Rollback
    @Transactional
    @Test
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        final String customerName = "Updated";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerById(
                customer.getId(), customerDTO
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().getFirst();

        // adhoc JSON for testing
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890 ");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest());
    }

    //---------------------- DELETE TESTS ----------------------//
    @Rollback
    @Transactional
    @Test
    void testDeleteByIdFound() {
        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

}