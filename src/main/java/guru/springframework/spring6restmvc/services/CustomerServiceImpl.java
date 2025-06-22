package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        CustomerDTO cust1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .version(1)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO cust2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .version(1)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO cust3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("John Smith")
                .version(1)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(cust1.getId(), cust1);
        customerMap.put(cust2.getId(), cust2);
        customerMap.put(cust3.getId(), cust3);
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(CustomerDTO.builder()
                .id(id)
                .name("Uncle Bob")
                .version(1)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build());
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(customer.getVersion())
                .createdDate(customer.getCreatedDate())
                .modifiedDate(customer.getModifiedDate())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);
        existing.setName(customer.getName());
        existing.setModifiedDate(customer.getModifiedDate());
        existing.setVersion(customer.getVersion());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);
        if (StringUtils.hasText(customer.getName())) { existing.setName(customer.getName()); }

        return Optional.of(existing);
    }
}
