package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import guru.springframework.spring6restmvc.util.BootstrapData;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static guru.springframework.spring6restmvc.model.BeerStyle.PALE_ALE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ BootstrapData.class, BeerCsvServiceImpl.class })
class BeerRepositoryTest {

	@Autowired
	BeerRepository beerRepository;

	@Test
	void testGetBeersByBeerName() {
		Page<Beer> beerList = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);

		assertThat(beerList.getContent().size()).isEqualTo(336);
	}

	@Test
	void testGetBeersByBeerStyle() {
		Page<Beer> beerList = beerRepository.findAllByBeerStyle(PALE_ALE, null);

		assertThat(beerList.getContent().size()).isEqualTo(12);
	}

	@Test
	void testSaveBeerNameTooLong() {

		assertThrows(ConstraintViolationException.class, () -> {
			Beer savedBeer = beerRepository.save(Beer.builder()
				.beerName("Beer Name Too Long 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890")
				.beerStyle(PALE_ALE)
				.upc("51132525")
				.price(new BigDecimal("11.99"))
				.build());

			beerRepository.flush();
		});
	}

	@Test
	void testSaveBeer() {
		Beer savedBeer = beerRepository.save(Beer.builder()
			.beerName("Beer")
			.beerStyle(PALE_ALE)
			.upc("12345")
			.price(new BigDecimal("11.99"))
			.build());

		beerRepository.flush();

		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}

}