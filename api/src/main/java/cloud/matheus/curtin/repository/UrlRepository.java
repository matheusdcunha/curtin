package cloud.matheus.curtin.repository;

import cloud.matheus.curtin.entity.UrlEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long>{

    Optional<UrlEntity> findByUrlCode(String urlCode);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UrlEntity u SET  u.updatedAt = :data, u.accessCount = u.accessCount + 1 WHERE u.id = :id")
    void incrementarAcesso(@Param("id") Long id,  @Param("data") LocalDateTime data);

}
