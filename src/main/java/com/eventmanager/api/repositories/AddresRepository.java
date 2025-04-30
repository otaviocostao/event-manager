package com.eventmanager.api.repositories;

import com.eventmanager.api.domain.addres.Addres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddresRepository extends JpaRepository<Addres, UUID> {
}
