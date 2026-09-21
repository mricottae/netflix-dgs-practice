package com.example.dgspractice.service;

import com.example.dgspractice.dto.AddShowInput;
import com.example.dgspractice.entity.Show;
import com.example.dgspractice.mapper.ShowMapper;
import com.example.dgspractice.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    public ShowService(ShowRepository showRepository,  ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
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

    public Show add(AddShowInput addShowInput) {
        return showRepository.save(showMapper.toShowEntity(addShowInput));
    }
}
