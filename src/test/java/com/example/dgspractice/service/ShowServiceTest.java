package com.example.dgspractice.service;

import com.example.dgspractice.entity.Show;
import com.example.dgspractice.repository.ShowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock
    ShowRepository showRepository;
    @InjectMocks
    ShowService showService;

    @Test
    void filtersByTitleCaseInsensitively() {
        when(showRepository.findByTitleContainingIgnoreCase("STRANGER"))
                .thenReturn(List.of(new Show(1, "Stranger Things", 2016)));

        assertThat(showService.findByTitleAndYear("STRANGER", null))
                .extracting(Show::getTitle)
                .containsExactly("Stranger Things");
    }
}
