package com.example.dgspractice.service;

import com.example.dgspractice.dto.AddShowInput;
import com.example.dgspractice.entity.Show;
import com.example.dgspractice.event.ShowEventPublisher;
import com.example.dgspractice.mapper.ShowMapper;
import com.example.dgspractice.repository.ShowRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ShowService {

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    private final ShowEventPublisher showEventPublisher;

    public ShowService(ShowRepository showRepository, ShowMapper showMapper,
                       ShowEventPublisher showEventPublisher) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
        this.showEventPublisher = showEventPublisher;
    }

    public Optional<Show> findById(int id) {
        return showRepository.findById(id);
    }

    public List<Show> findByTitleAndYear(String title, Integer year) {
        if (title != null && year != null) {
            return showRepository.findByTitleContainingIgnoreCaseAndReleaseYear(title, year);
        } else if (title != null) {
            return showRepository.findByTitleContainingIgnoreCase(title);
        } else if (year != null) {
            return showRepository.findByReleaseYear(year);
        }
        return showRepository.findAll();
    }

    public Show add(@Valid AddShowInput addShowInput) {
        Show saved = showRepository.save(showMapper.toShowEntity(addShowInput));
        showEventPublisher.publish(saved);
        return saved;
    }
}
