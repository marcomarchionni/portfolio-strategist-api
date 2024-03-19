package com.marcomarchionni.strategistapi.repositories;

import com.marcomarchionni.strategistapi.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByAccountIdAndName(String accountId, String name);

    boolean existsByAccountIdAndName(String accountId, String portfolioName);

    List<Portfolio> findAllByAccountId(String accountId);

    Optional<Portfolio> findByIdAndAccountId(Long id, String accountId);

    void deleteByAccountId(String accountId);
}