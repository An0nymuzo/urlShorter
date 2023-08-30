package ca.notarius.shorter.url.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.notarius.shorter.url.database.*;

@Service
public class UrlShortenerService {

	@Autowired
	UrlConverterRepository urlConverterRepository;
	
	//for mocking test
	public UrlShortenerService(UrlConverterRepository urlConverterRepository) {
		super();
		this.urlConverterRepository = urlConverterRepository;
	}

	public UrlShortenerService() {
	}

	//function is synchronized because I have to protect against concurrent access
	// (Deux URL complètes identiques doivent donner la même URL raccourcie)
	public synchronized UrlConverter generateShorterURL(String baseURL) throws Exception {
		
		try{
			new URL(baseURL);
		}catch (MalformedURLException e) {
			throw new MalformedURLException("Invalid URL");
		}
		
		List<UrlConverter> urlConverters = urlConverterRepository.findBybaseUrl(baseURL);
		if (urlConverters != null && urlConverters.size() == 1) {
			return urlConverters.get(0);
		} else if (urlConverters.size() > 1) {
			throw new Exception("Database Integrity Damaged");
		}
		
		UrlConverter converter = new UrlConverter();
		long lastUpDate = urlConverterRepository.getmaxId();
		converter.setId(lastUpDate);
		converter.setBaseUrl(baseURL);
		converter.setShortUrl(generateshorterUrlCode(converter));

		return urlConverterRepository.save(converter);

	}
	
	public UrlConverter extractExistingShorterUrl(String shortUrl) throws Exception {
		List<UrlConverter> urlConverters = urlConverterRepository.findByshortUrl(shortUrl);
		if (urlConverters != null && urlConverters.size() == 1) {
			return urlConverters.get(0);
		} 
		throw new Exception("URL NOT FOUND");
		
	}
	
	public String generateshorterUrlCode(UrlConverter urlConverters) {
		String output = Integer.toString(Long.valueOf(urlConverters.getId()).intValue(),36);
		return org.apache.commons.lang3.StringUtils.leftPad(output, 10, "0");
	}
	
	
}
