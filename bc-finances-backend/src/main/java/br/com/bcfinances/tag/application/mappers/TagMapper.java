package br.com.bcfinances.tag.application.mappers;

import br.com.bcfinances.tag.application.dto.TagResponse;
import br.com.bcfinances.tag.domain.entities.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagMapper {

    public TagResponse toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagResponse(tag.getId(), tag.getName());
    }

    public List<TagResponse> toResponseList(List<Tag> tags) {
        return tags.stream()
                .map(this::toResponse)
                .toList();
    }
}
