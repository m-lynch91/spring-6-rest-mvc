package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.hamcrest.core.IsNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static guru.springframework.spring6restmvc.model.BeerStyle.IPA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	BeerMapper beerMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		// setting up mockMvc environment with spring data repositories injected into
		// service
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	// ---------------------- CREATE TESTS ----------------------//
	@Transactional
	@Rollback
	@Test
	void testSaveNewBeer() {
		BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();

		ResponseEntity<BeerDTO> responseEntity = beerController.handlePost(beerDTO);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertNotNull(responseEntity.getHeaders().getLocation());

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
		UUID savedUUID = UUID.fromString(locationUUID[4]);

		Beer savedBeer = beerRepository.findById(savedUUID).get();
		assertNotNull(savedBeer);
	}

	// ---------------------- READ TESTS ------------------------//
	@Test
	void testGetAllBeers() {
		Page<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 2413);
		assertThat(dtos.getContent().size()).isEqualTo(1000);
	}

	@Test
	void testGetBeerById() {
		Beer beer = beerRepository.findAll().getFirst();
		BeerDTO beerDTO = beerController.getBeerById(beer.getId());
		assertNotNull(beerDTO);
	}

	@Test
	void testGetBeersByName() throws Exception {
		mockMvc.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA").queryParam("pageSize", "800"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(336)));
	}

	@Test
	void testGetBeersByStyle() throws Exception {
		mockMvc
			.perform(get(BeerController.BEER_PATH).queryParam("beerStyle", BeerStyle.PALE_ALE.name())
				.queryParam("pageSize", "800"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(12)));
	}

	@Test
	void testGetBeersByStyleAndName() throws Exception {
		mockMvc
			.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA")
				.queryParam("beerStyle", IPA.name())
				.queryParam("pageSize", "800"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)));
	}

	@Test
	void testGetBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
		mockMvc
			.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "true")
				.queryParam("pageNumber", "2")
				.queryParam("pageSize", "50"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(50)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
	}

	@Test
	void testGetBeersByStyleAndNameShowInventoryTrue() throws Exception {
		mockMvc
			.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "true")
				.queryParam("pageSize", "800"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
	}

	@Test
	void testGetBeersByStyleAndNameShowInventoryFalse() throws Exception {
		mockMvc
			.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "false")
				.queryParam("pageSize", "800"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
	}

	// SpringBootTest doesn't automatically make this test transactional like with
	// DataJpaTest would
	@Transactional
	@Rollback
	@Test
	void testEmptyBeerList() {
		beerRepository.deleteAll();
		Page<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 25);
		assertThat(dtos.getContent().size()).isEqualTo(0);
	}

	@Test
	void testGetBeerByIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.getBeerById(UUID.randomUUID());
		});
	}

	// ---------------------- UPDATE TESTS ----------------------//
	@Rollback
	@Transactional
	@Test
	void testUpdateExistingBeer() {
		Beer beer = beerRepository.findAll().getFirst();
		BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
		beerDTO.setId(null);
		beerDTO.setVersion(null);

		final String beerName = "Updated";
		beerDTO.setBeerName(beerName);

		ResponseEntity<BeerDTO> responseEntity = beerController.updateById(beer.getId(), beerDTO);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

		Beer updatedBeer = beerRepository.findById(beer.getId()).get();
		assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
	}

	@Test
	void testUpdateNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
		});
	}

	@Test
	void testPatchBeerBadName() throws Exception {
		Beer beer = beerRepository.findAll().getFirst();

		// adhoc JSON for testing
		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890123456789012345678901234567890 ");

		MvcResult result = mockMvc
			.perform(patch(BeerController.BEER_PATH_ID, beer.getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerMap)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()", is(1)))
			.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

	@Disabled // just for demo purposes
	@Test
	void testUpdateBeerBadVersion() throws Exception {
		Beer beer = beerRepository.findAll().get(0);

		BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);

		beerDTO.setBeerName("Updated Name");

		MvcResult result = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(beerDTO)))
				.andExpect(status().isNoContent())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());

		beerDTO.setBeerName("Updated Name 2");

		MvcResult result2 = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(beerDTO)))
				.andExpect(status().isNoContent())
				.andReturn();

		System.out.println(result2.getResponse().getStatus());
	}

	// ---------------------- DELETE TESTS ----------------------//
	@Rollback
	@Transactional
	@Test
	void testDeleteByIdFound() {
		Beer beer = beerRepository.findAll().getFirst();
		ResponseEntity<BeerDTO> responseEntity = beerController.deleteBeerById(beer.getId());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
		assertThat(beerRepository.findById(beer.getId()).isEmpty());
	}

	@Test
	void testDeleteByIDNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.deleteBeerById(UUID.randomUUID());
		});
	}

}
