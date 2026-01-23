package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.config.SpringSecConfig;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(SpringSecConfig.class)
class CustomerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	CustomerService customerService;

	CustomerServiceImpl customerServiceImpl;

	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();

	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;

	@Captor
	ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

	@BeforeEach
	void setUp() {
		customerServiceImpl = new CustomerServiceImpl();
	}

	// ---------------------- CREATE TESTS ----------------------//
	@Test
	void testCreateCustomer() throws Exception {
		CustomerDTO customer = customerServiceImpl.getCustomers().getFirst();
		customer.setId(null);
		customer.setVersion(null);

		given(customerService.saveNewCustomer(any(CustomerDTO.class)))
			.willReturn(customerServiceImpl.getCustomers().get(1));

		mockMvc
			.perform(post(CustomerController.CUSTOMERS_PATH)
					.with(BeerControllerTest.jwtRequestPostProcessor)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer)))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	// ---------------------- READ TESTS ------------------------//
	@Test
	void testGetAllCustomers() throws Exception {
		given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());

		mockMvc.perform(get(CustomerController.CUSTOMERS_PATH)
						.with(BeerControllerTest.jwtRequestPostProcessor)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(customerServiceImpl.getCustomers().size())));
	}

	@Test
	void testGetCustomerById() throws Exception {
		CustomerDTO testCustomer = customerServiceImpl.getCustomers().getFirst();

		given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

		mockMvc
			.perform(get(CustomerController.CUSTOMERS_PATH_ID, testCustomer.getId())
					.with(BeerControllerTest.jwtRequestPostProcessor)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
			.andExpect(jsonPath("$.name", is(testCustomer.getName())));
	}

	@Test
	void getCustomerByIdNotFound() throws Exception {
		given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
		mockMvc.perform(get(CustomerController.CUSTOMERS_PATH_ID, UUID.randomUUID())
				.with(BeerControllerTest.jwtRequestPostProcessor))
				.andExpect(status().isNotFound());
	}

	// ---------------------- UPDATE TESTS ----------------------//
	@Test
	void testUpdateCustomer() throws Exception {
		CustomerDTO customer = customerServiceImpl.getCustomers().getFirst();

		given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(customer));

		mockMvc
			.perform(put(CustomerController.CUSTOMERS_PATH_ID, customer.getId())
					.with(BeerControllerTest.jwtRequestPostProcessor)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer)))
			.andExpect(status().isNoContent());

		verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
		assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

	@Test
	void testPatchCustomer() throws Exception {
		CustomerDTO customer = customerServiceImpl.getCustomers().getFirst();

		Map<String, Object> customerMap = new HashMap<>();
		customerMap.put("name", "New Customer");

		mockMvc
			.perform(patch(CustomerController.CUSTOMERS_PATH_ID, customer.getId())
					.with(BeerControllerTest.jwtRequestPostProcessor)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerMap)))
			.andExpect(status().isNoContent());

		verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
		assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
	}

	// ---------------------- DELETE TESTS ----------------------//
	@Test
	void testDeleteCustomer() throws Exception {
		CustomerDTO customer = customerServiceImpl.getCustomers().getFirst();

		given(customerService.deleteCustomerById(any())).willReturn(true);

		mockMvc
			.perform(delete(CustomerController.CUSTOMERS_PATH_ID, customer.getId())
					.with(BeerControllerTest.jwtRequestPostProcessor)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
		assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}