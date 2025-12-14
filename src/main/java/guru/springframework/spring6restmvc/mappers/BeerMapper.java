package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

	// Mapstruct annotation generates all of this code upon Maven.Compile
	// see /target/generated-sources
	// ensure that the compiled class has @Component annotation
	// if there is not @Component annotation, it could be because of a missing
	// <compilerArg> in the pom.xml
	// <compilerArg>-Amapstruct.defaultComponentModel=spring</compilerArg>
	Beer beerDtoToBeer(BeerDTO dto);

	BeerDTO beerToBeerDTO(Beer beer);

}
