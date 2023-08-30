package ca.notarius.shorter.url.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UrlConverterRepository extends JpaRepository<UrlConverter, Long> {
	
	List<UrlConverter> findBybaseUrl(String baseUrl);
	List<UrlConverter> findByshortUrl(String shortUrl);
	@Query(value = "select IFNULL(max(id)+1,0) from UrlConverter")
	Long getmaxId();
}
