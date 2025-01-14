package com.marcomarchionni.strategistapi.accessservice;

import com.marcomarchionni.strategistapi.domain.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PortfolioAccessService {
    List<Portfolio> findAll();

    Page<Portfolio> findAll(Pageable pageable);

    boolean existsByName(String name);

    Optional<Portfolio> findById(Long id);

    Portfolio save(Portfolio portfolio);

    void delete(Portfolio portfolio);

    int count();
}
