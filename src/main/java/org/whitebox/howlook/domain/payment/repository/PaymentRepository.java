package org.whitebox.howlook.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;

@Repository
public interface PaymentRepository extends JpaRepository <PaymentInfo,Long> {
}
