package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.config.SpringSecConfig;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// just need a test splice, not a full spring boot test suite
@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
class BeerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();

	@MockitoBean
	BeerService beerService;

	BeerServiceImpl beerServiceImpl;

	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;

	@Captor
	ArgumentCaptor<BeerDTO> beerArgumentCaptor;

	public static final String USERNAME = "user1";
	public static final String PASSWORD= "password";

	@BeforeEach
	void setUp() {
		beerServiceImpl = new BeerServiceImpl();
	}

	// ---------------------- CREATE TESTS ----------------------//
	@Test
	void testCreateNewBeer() throws Exception {
		BeerDTO beer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();
		beer.setVersion(null);
		beer.setId(null);

		given(beerService.saveNewBeer(any(BeerDTO.class)))
			.willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().get(1));

		mockMvc
			.perform(post(BeerController.BEER_PATH)
					.with(httpBasic(USERNAME, PASSWORD))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void testCreateBeerNullBeerName() throws Exception {
		BeerDTO beer = BeerDTO.builder().build();

		given(beerService.saveNewBeer(any(BeerDTO.class)))
			.willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst());

		MvcResult mvcResult = mockMvc
			.perform(post(BeerController.BEER_PATH)
					.with(httpBasic(USERNAME, PASSWORD))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()", is(6)))
			.andReturn();

		System.out.println(mvcResult.getResponse().getContentAsString());
	}

	// ---------------------- READ TESTS ------------------------//
	@Test
	void testGetAllBeers() throws Exception {
		given(beerService.getBeers(any(), any(), any(), any(), any()))
			.willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25));

		mockMvc.perform(get(BeerController.BEER_PATH)
						.with(httpBasic(USERNAME, PASSWORD))
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()", is(3)));
	}

	@Test
	void testGetBeerById() throws Exception {
		BeerDTO testBeer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();

		given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

		mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
						.with(httpBasic(USERNAME, PASSWORD))
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
	}

	@Test
	void getBearByIdNotFound() throws Exception {
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
		mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
				.with(httpBasic(USERNAME, PASSWORD))).andExpect(status().isNotFound());
	}

	// ---------------------- UPDATE TESTS ----------------------//
	@Test
	void testUpdateBeer() throws Exception {
		BeerDTO beer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();

		given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

		mockMvc
			.perform(put(BeerController.BEER_PATH_ID, beer.getId())
					.with(httpBasic(USERNAME, PASSWORD))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
			.andExpect(status().isNoContent());

		// verifies that the services update method was called 1 time (by default), using
		// a UUID and
		// BEER object
		verify(beerService).updateBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

	@Test
	void testPatchBeer() throws Exception {
		BeerDTO beer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();

		// adhoc JSON for testing
		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName", "New Name");

		mockMvc
			.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
					.with(httpBasic(USERNAME, PASSWORD))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerMap)))
			.andExpect(status().isNoContent());

		// uuidArgumentCaptor will capture the argument(s) that are passed to
		// .patchBeerById()
		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
	}

	@Test
	void testUpdateBeerNullBeerName() throws Exception {
		BeerDTO beer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();
		beer.setBeerName("");

		given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

		mockMvc
			.perform(put(BeerController.BEER_PATH_ID, beer.getId())
					.with(httpBasic(USERNAME, PASSWORD))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()", is(1)));
	}

	// ---------------------- DELETE TESTS ----------------------//
	@Test
	void testDeleteBeer() throws Exception {
		BeerDTO beer = beerServiceImpl.getBeers(null, null, false, 1, 25).getContent().getFirst();

		given(beerService.deleteBeerById(any())).willReturn(true);

		mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
						.with(httpBasic(USERNAME, PASSWORD))
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
		assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
