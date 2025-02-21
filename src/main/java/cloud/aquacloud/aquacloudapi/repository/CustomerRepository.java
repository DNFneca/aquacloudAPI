package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.DTO.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findByStripeCustomerId(String stripeCustomerId);
}