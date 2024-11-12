package com.hello.springbatch.job.core.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByPriceGreaterThanEqual(Integer price);
}
