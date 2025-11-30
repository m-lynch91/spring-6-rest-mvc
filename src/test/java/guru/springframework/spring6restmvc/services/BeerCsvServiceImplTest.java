package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BeerCsvServiceImplTest {
    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCsv() throws FileNotFoundException {
        // Assign
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        // Act
        List<BeerCSVRecord> records = beerCsvService.convertCsv(file);
        System.out.println(records.size());

        // Assert
        assertThat(records.size()).isGreaterThan(0);
    }
}
