package com.kost.kostapi.repository;

import com.kost.kostapi.entity.Kost;
import com.kost.kostapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KostRepository extends JpaRepository<Kost, Long> {

    Page<Kost> findByOwner(User owner, Pageable pageable);

}