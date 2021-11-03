package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Long> {

    Optional<MapEntity> findByLatitudeAndLongitude(double latitude, double longitude);
    boolean existsByLatitudeAndLongitude(double latitude, double longitude);
}
