package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(generator = "UUID")
	// @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	// deprecated
	@UuidGenerator
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	@JdbcTypeCode(SqlTypes.CHAR)
	private UUID id;

	private String name;

	@Column(length = 255)
	private String email;

	@Version
	private Integer version;

	private LocalDateTime createdDate;

	private LocalDateTime modifiedDate;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<BeerOrder> beerOrders;

}
