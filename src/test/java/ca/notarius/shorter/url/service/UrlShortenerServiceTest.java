package ca.notarius.shorter.url.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.notarius.shorter.url.database.UrlConverter;
import ca.notarius.shorter.url.database.UrlConverterRepository;

@ExtendWith(MockitoExtension.class)     
public class UrlShortenerServiceTest {

    private UrlShortenerService service;
    
    @Mock
    private UrlConverterRepository urlConverterRepository; 
	
    
	@Test
	void should_validate_generated_shorter_url() throws Exception {
		service = new UrlShortenerService();
		UrlConverter urlConverters = new UrlConverter();
		List<String> listAssert = Arrays.asList("0000000000","0000000001","0000000002","0000000003","0000000004","0000000005","0000000006","0000000007","0000000008","0000000009","000000000a","000000000b","000000000c","000000000d","000000000e","000000000f","000000000g","000000000h","000000000i","000000000j","000000000k","000000000l","000000000m","000000000n","000000000o","000000000p","000000000q","000000000r","000000000s","000000000t","000000000u","000000000v","000000000w","000000000x","000000000y","000000000z","0000000010","0000000011","0000000012","0000000013","0000000014","0000000015","0000000016","0000000017","0000000018","0000000019","000000001a","000000001b","000000001c","000000001d","000000001e","000000001f","000000001g","000000001h","000000001i","000000001j","000000001k","000000001l","000000001m","000000001n","000000001o","000000001p","000000001q","000000001r","000000001s","000000001t","000000001u","000000001v","000000001w","000000001x","000000001y","000000001z","0000000020","0000000021","0000000022","0000000023","0000000024","0000000025","0000000026","0000000027","0000000028","0000000029","000000002a","000000002b","000000002c","000000002d","000000002e","000000002f","000000002g","000000002h","000000002i","000000002j","000000002k","000000002l","000000002m","000000002n","000000002o","000000002p","000000002q","000000002r");
        for(int i=0;i<100;i++){
        	urlConverters.setId(Long.valueOf(i).longValue());
        	String output = service.generateshorterUrlCode(urlConverters);
        	assertEquals(output, listAssert.get(i));
        }
    }
	
	@Test
	void should_generateShorterURL() throws Exception{
		service = new UrlShortenerService(urlConverterRepository);
		UrlConverter urlConverter = new UrlConverter();
		urlConverter.setId(1l);
		urlConverter.setBaseUrl("http://www.baseurl.com");
		urlConverter.setShortUrl("0000000001");
		when(urlConverterRepository.findBybaseUrl("http://www.baseurl.com")).thenReturn(new ArrayList<UrlConverter>());
		when(urlConverterRepository.getmaxId()).thenReturn(2l);
		when(urlConverterRepository.save(Mockito.any())).thenReturn(urlConverter);
		service.generateShorterURL("http://www.baseurl.com");
	}
	
	@Test
	void should_generateShorterURL_existing() throws Exception{
		service = new UrlShortenerService(urlConverterRepository);
		List<UrlConverter> list = new ArrayList<UrlConverter>();
		UrlConverter urlConverter = new UrlConverter();
		urlConverter.setId(1l);
		urlConverter.setBaseUrl("http://www.baseurl.com");
		urlConverter.setShortUrl("0000000001");
		list.add(urlConverter);
		when(urlConverterRepository.findBybaseUrl("http://www.baseurl.com")).thenReturn(list);
		service.generateShorterURL("http://www.baseurl.com");
	}
	
	@Test
	void should_generateShorterURL_malformed_url() throws Exception{
		service = new UrlShortenerService(urlConverterRepository);
		assertThrows(MalformedURLException.class, () -> {service.generateShorterURL("wwwbaseurl.com");});
		
	}
	
	@Test
	void should_extractExistingShorterUrl() throws Exception{
		service = new UrlShortenerService(urlConverterRepository);
		List<UrlConverter> list = new ArrayList<UrlConverter>();
		UrlConverter urlConverter = new UrlConverter();
		urlConverter.setId(1l);
		urlConverter.setBaseUrl("http://www.baseurl.com");
		urlConverter.setShortUrl("0000000001");
		list.add(urlConverter);
		when(urlConverterRepository.findByshortUrl("http://www.baseurl.com")).thenReturn(list);
		service.extractExistingShorterUrl("http://www.baseurl.com");
	}
	@Test
	void should_extractExistingShorterUrl_unexisting() throws Exception{
		service = new UrlShortenerService(urlConverterRepository);
		when(urlConverterRepository.findByshortUrl("http://www.baseurl.com")).thenReturn(new ArrayList<UrlConverter>());
		assertThrows(Exception.class, () -> {service.extractExistingShorterUrl("http://www.baseurl.com");});
	}

	@Test
	void toto()
	{
		int i=0;
		System.err.println(i++);
	}
}
