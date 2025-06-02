package guru.springframework.spring6restmvc.util;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    public void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("Miller Genuine Draught")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123457")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(22)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Stella Artois")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123457")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(22)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Heineken")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123457")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(22)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer4 = Beer.builder()
                    .beerName("Bud Light")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("111111")
                    .price(new BigDecimal("9.99"))
                    .quantityOnHand(1000)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer5 = Beer.builder()
                    .beerName("Corona Extra")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("222222")
                    .price(new BigDecimal("10.49"))
                    .quantityOnHand(800)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer6 = Beer.builder()
                    .beerName("Guinness Draught")
                    .beerStyle(BeerStyle.STOUT)
                    .upc("333333")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(500)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer7 = Beer.builder()
                    .beerName("Sierra Nevada Pale Ale")
                    .beerStyle(BeerStyle.ALE)
                    .upc("444444")
                    .price(new BigDecimal("12.49"))
                    .quantityOnHand(600)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer8 = Beer.builder()
                    .beerName("Budwesier")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("55555")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(500)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3, beer4, beer5, beer6, beer7, beer8));
        }
    }

    public void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer cust1 = Customer.builder()
                    .name("John Doe")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            Customer cust2 = Customer.builder()
                    .name("Jane Smith")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            Customer cust3 = Customer.builder()
                    .name("Mike Johnson")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            Customer cust4 = Customer.builder()
                    .name("Emily Davis")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            Customer cust5 = Customer.builder()
                    .name("Robert Martinez")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(cust1, cust2, cust3, cust4, cust5));
        }
    }
}
