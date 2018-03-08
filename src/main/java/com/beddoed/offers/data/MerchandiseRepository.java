package com.beddoed.offers.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MerchandiseRepository extends JpaRepository<MerchandiseDTO, UUID> {
}
