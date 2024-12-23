package com.koral.sockservice.repository;

import com.koral.sockservice.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {
    Optional<Socks> findById(Long id);
    Optional<Socks> findByColorAndCottonPart(String color, Integer cottonPart);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE (:color IS NULL OR s.color = :color) " + "AND (:operation IS NULL OR ((:operation = 'moreThan' AND s.cottonPart > :cottonPart) OR(:operation = 'lessThan' AND s.cottonPart < :cottonPart) OR(:operation = 'equal' AND s.cottonPart = :cottonPart)))")
    Long countByFilter(@Param("color") String color,
                       @Param("operation") String operation,
                       @Param("cottonPart") Integer cottonPart);
}
