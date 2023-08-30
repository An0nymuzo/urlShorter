package ca.notarius.shorter.url.database;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "URL")
@EnableAutoConfiguration
@Getter
@Setter
public class UrlConverter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "BASEURL", length = 2000)
	private String baseUrl;
	
	@Column(name = "SHORTURL")
	private String shortUrl;
}
