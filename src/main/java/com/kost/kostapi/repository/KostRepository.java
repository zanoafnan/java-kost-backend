package com.kost.kostapi.repository;

import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KostRepository
        extends JpaRepository<Kost, Long>,
        JpaSpecificationExecutor<Kost> {

    Page<Kost> findByOwner(
            User owner,
            Pageable pageable);

}