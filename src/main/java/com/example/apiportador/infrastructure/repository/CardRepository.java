package com.example.apiportador.infrastructure.repository;

import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    @Transactional
    @Modifying
    @Query("update CardEntity c set c.limit = ?1 where c.cardId = ?2")
    int updateLimitByCardId(BigDecimal limit, @NonNull UUID cardId);



    List<CardEntity> findByCardHolderId_CardHolderId(UUID id);

}
