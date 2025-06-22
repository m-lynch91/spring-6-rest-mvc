package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        assertNotNull(beerDTO);
    }

    @Test
    void testDeleteByIDNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Beer beer = beerRepository.findAll().getFirst();
        ResponseEntity<BeerDTO> responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void testBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetAllBeers() {
        List<BeerDTO> dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(8);
    }

    // SpringBootTest doesn't automatically make this test transactional like with DataJpaTest would
    @Transactional
    @Rollback
    @Test
    void testEmptyBeerList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Transactional
    @Rollback
    @Test
    void testSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<List<BeerDTO>> responseEntity = beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertNotNull(responseEntity.getHeaders().getLocation());

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer savedBeer = beerRepository.findById(savedUUID).get();
        assertNotNull(savedBeer);
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateExistingBeer() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO  = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        final String beerName = "Updated";
        beerDTO.setBeerName(beerName);

        ResponseEntity<BeerDTO> responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }
}