package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderShipment;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BeerOrderRepositoryTest {

	@Autowired
	BeerOrderRepository beerOrderRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BeerRepository beerRepository;

	Customer testCustomer;

	Beer testBeer;

	@BeforeEach
	void setUp() {
		testCustomer = customerRepository.findAll().getFirst();
		testBeer = beerRepository.findAll().getFirst();
	}

	@Transactional
	@Test
	void testGetBeerOrders() {
		BeerOrder beerOrder = BeerOrder.builder()
			.customerRef("Test Order")
			.customer(testCustomer)
			.beerOrderShipment(BeerOrderShipment.builder().trackingNumber("12345r").build())
			.build();

		// save and flush to ensure it's written to the db
		BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

		System.out.println(savedBeerOrder.getCustomerRef());
	}

}
