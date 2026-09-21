package com.example.dgspractice.mapper;

import com.example.dgspractice.dto.AddShowInput;
import com.example.dgspractice.dto.ShowDto;
import com.example.dgspractice.entity.Show;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowMapper {

    public ShowDto toShowDto(Show show) {
        return new ShowDto(show.getId(), show.getTitle(), show.getReleaseYear(), show.getSeasons());
    }

    public List<ShowDto> toShowDtoList(List<Show> shows) {
        return shows.stream().map(this::toShowDto).toList();
    }

    public Show toShowEntity(AddShowInput show) {
        return new Show(null, show.title(), show.releaseYear(), show.seasons());
    }
}
