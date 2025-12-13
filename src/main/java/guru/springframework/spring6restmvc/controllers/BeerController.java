package guru.springframework.spring6restmvc.controllers;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beers";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    //---------------------- CREATE ROUTES ----------------------//
    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handlePost(@Validated @RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //---------------------- READ ROUTES ------------------------//
    @GetMapping(BEER_PATH)
    public List<BeerDTO> getAllBeers(@RequestParam(required = false) String beerName, @RequestParam(required = false) BeerStyle beerStyle) {
        return beerService.getBeers(beerName, beerStyle);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get Beer by id - in controller.");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    //---------------------- UPDATE ROUTES ----------------------//
    // Put mapping will limit the method to responding to HTTP PUT requests only
    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> updateById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) {
        if (beerService.updateBeerById(beerId, beer).isEmpty())
            throw new NotFoundException();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        beerService.patchBeerById(beerId, beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //---------------------- DELETE ROUTES ----------------------//

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        if (!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        ;

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
