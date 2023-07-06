package com.example.apiportador.infrastructure.repository;

import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.util.StatusEnum;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    @Transactional
    @Modifying
    @Query("update CardHolderEntity c set c.availableLimit = ?1 where c.cardHolderId = ?2")
    int updateAvailableLimitByCardHolderId(BigDecimal availableLimit, UUID cardHolderId);




    boolean existsByClientId(UUID id);

    List<CardHolderEntity> findByStatus(StatusEnum status);
}
