package ca.notarius.shorter.url.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import ca.notarius.shorter.url.database.UrlConverterRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SqlGroup({
    @Sql(value = "classpath:empty/reset.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "classpath:init/user-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
})
public class UrlShortenerControllerTest {

    @Autowired
    private UrlConverterRepository repository;

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void should_find_existing_shorter_by_baseUrl() throws Exception {
        this.mockMvc.perform(get("/baseurl?id=http://www.google.com"))
                .andDo(print())
                .andExpect(status().isOk());
        assertThat(this.repository.findBybaseUrl("http://www.google.com")).hasSize(1);
    }
    
    @Test
    void should_have_error_with_bad_url_baseUrl() throws Exception {
        this.mockMvc.perform(get("/baseurl?id=google"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        assertThat(this.repository.findBybaseUrl("google")).hasSize(0);
    }
    
    @Test
    void should_have_error_with_damaged_database_baseUrl() throws Exception {
        this.mockMvc.perform(get("/baseurl?id=http://www.notarius.com/fr/"))
                .andDo(print())
                .andExpect(status().isConflict());
        assertThat(this.repository.findBybaseUrl("http://www.notarius.com/fr/")).hasSize(2);
    }
    
    @Test
    void should_find_existing_shorter_by_shortUrl() throws Exception {
        this.mockMvc.perform(get("/HAPPYPATH1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
        assertThat(this.repository.findByshortUrl("HAPPYPATH1")).hasSize(1);
    }
    
    @Test
    void should_have_error_with_not_find_existing_shorter_by_shortUrl() throws Exception {
        this.mockMvc.perform(get("/ERROR"))
                .andDo(print())
                .andExpect(status().isNotFound());
        assertThat(this.repository.findByshortUrl("ERROR")).hasSize(0);
    }
    
    @Test
    void should_find_existing_shorter_by_fullUrl() throws Exception {
        this.mockMvc.perform(get("/fullUrl?id=http://localhost:8080/HAPPYPATH1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void should_have_error_with_not_find_existing_shorter_by_fullUrl() throws Exception {
        this.mockMvc.perform(get("/fullUrl?id=http://localhost:8080/000000008f"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
