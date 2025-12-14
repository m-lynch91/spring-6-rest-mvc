package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

	private final Map<UUID, BeerDTO> beerMap;

	public BeerServiceImpl() {
		this.beerMap = new HashMap<>();

		BeerDTO beer1 = BeerDTO.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Miller Genuine Draught")
			.beerStyle(BeerStyle.LAGER)
			.upc("123457")
			.price(new BigDecimal("12.99"))
			.quantityOnHand(22)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		BeerDTO beer2 = BeerDTO.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Stella Artois")
			.beerStyle(BeerStyle.LAGER)
			.upc("123457")
			.price(new BigDecimal("13.99"))
			.quantityOnHand(22)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		BeerDTO beer3 = BeerDTO.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Heineken")
			.beerStyle(BeerStyle.LAGER)
			.upc("123457")
			.price(new BigDecimal("11.99"))
			.quantityOnHand(22)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		beerMap.put(beer1.getId(), beer1);
		beerMap.put(beer2.getId(), beer2);
		beerMap.put(beer3.getId(), beer3);
	}

	@Override
	public List<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
			Integer pageSize) {
		return new ArrayList<>(beerMap.values());
	}

	@Override
	public Optional<BeerDTO> getBeerById(UUID id) {
		log.debug("Get Beer by id - in service. Id: " + id.toString());

		return Optional.of(BeerDTO.builder()
			.id(id)
			.version(1)
			.beerName("Sleeman's Original Draught")
			.beerStyle(BeerStyle.LAGER)
			.upc("123456")
			.price(new BigDecimal("9.99"))
			.quantityOnHand(150)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build());
	}

	@Override
	public BeerDTO saveNewBeer(BeerDTO beer) {
		BeerDTO savedBeer = BeerDTO.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName(beer.getBeerName())
			.upc(beer.getUpc())
			.price(beer.getPrice())
			.quantityOnHand(beer.getQuantityOnHand())
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		beerMap.put(savedBeer.getId(), savedBeer);

		return savedBeer;
	}

	@Override
	public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
		BeerDTO existing = beerMap.get(beerId);
		existing.setBeerName(beer.getBeerName());
		existing.setUpc(beer.getUpc());
		existing.setPrice(beer.getPrice());
		existing.setQuantityOnHand(beer.getQuantityOnHand());
		return Optional.of(existing);
	}

	@Override
	public Boolean deleteBeerById(UUID beerId) {
		beerMap.remove(beerId);
		return true;
	}

	@Override
	public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
		BeerDTO existing = beerMap.get(beerId);

		if (StringUtils.hasText(beer.getBeerName())) {
			existing.setBeerName(beer.getBeerName());
		}
		if (StringUtils.hasText(beer.getUpc())) {
			existing.setUpc(beer.getUpc());
		}
		if (beer.getBeerStyle() != null) {
			existing.setBeerStyle(beer.getBeerStyle());
		}
		if (beer.getPrice() != null) {
			existing.setPrice(beer.getPrice());
		}
		if (beer.getQuantityOnHand() != null) {
			existing.setQuantityOnHand(beer.getQuantityOnHand());
		}

		return Optional.of(existing);
	}

}
