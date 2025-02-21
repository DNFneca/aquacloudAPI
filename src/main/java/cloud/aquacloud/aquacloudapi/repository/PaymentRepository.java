package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUserEmail(String userEmail);
}
