package cloud.matheus.curtin.repository;

import cloud.matheus.curtin.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, Long>{
}
