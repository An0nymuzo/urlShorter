package ca.notarius.shorter.url.controller;


import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.notarius.shorter.url.database.*;
import ca.notarius.shorter.url.service.UrlShortenerService;

@RestController
public class UrlShortenerController {
	
	@Autowired
	UrlShortenerService UrlShortenerService;

 private static String HOMEURL="http://localhost:8080/"; 
 /**
  * 
  * @param id the url to be shorted
  * @return
  */
	@GetMapping(value = "/baseurl", params = {"id"})
	ResponseEntity<String> baseurl(@RequestParam("id") String id) {
		
		UrlConverter urlConverters;
		try {
			urlConverters = UrlShortenerService.generateShorterURL(id);
			urlConverters.setShortUrl(HOMEURL+urlConverters.getShortUrl());
	        return new ResponseEntity<>("Shorturl : " +urlConverters.getShortUrl(), HttpStatus.OK);

		} catch (MalformedURLException e) {
			e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} 
		catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
    }
	
	/**
	 * 
	 * @param id : generated code for shorter url
	 * @return 
	 */
	@GetMapping(value = "/{id}")
	ResponseEntity<String> shorturl(@PathVariable("id") String id) {
		try {
		UrlConverter urlConverters = UrlShortenerService.extractExistingShorterUrl(id);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlConverters.getBaseUrl())).build();
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
    }
	
	/**
	 * 
	 * @param id : generated code for shorter url
	 * @return 
	 */
	@GetMapping(value = "/fullUrl", params = {"id"})
	ResponseEntity<String> getFullurl(@RequestParam("id") String id) {
		try {
			if(id.startsWith(HOMEURL)) {
				id = id.substring(HOMEURL.length());
			}
			UrlConverter urlConverters = UrlShortenerService.extractExistingShorterUrl(id);
	        return new ResponseEntity<>("Original Url : " +urlConverters.getBaseUrl(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
    }

}
