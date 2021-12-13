package com.example.springsecuritytest.domain.map;

import com.example.springsecuritytest.domain.map.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Long> {

    Optional<MapEntity> findByLatitudeAndLongitude(double latitude, double longitude);
    boolean existsByLatitudeAndLongitude(double latitude, double longitude);
}
