package br.com.bcfinances.tag.presentation.controllers;

import br.com.bcfinances.tag.application.dto.TagResponse;
import br.com.bcfinances.tag.application.mappers.TagMapper;
import br.com.bcfinances.tag.application.usecases.FindTagsUseCase;
import br.com.bcfinances.tag.domain.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final FindTagsUseCase findTagsUseCase;
    private final TagMapper tagMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public List<TagResponse> list(@RequestParam(value = "query", required = false) String query) {
        List<Tag> tags = findTagsUseCase.execute();

        if (query != null && !query.isBlank()) {
            String normalized = query.trim().toLowerCase();
            tags = tags.stream()
                    .filter(tag -> tag.getName() != null && tag.getName().toLowerCase().contains(normalized))
                    .toList();
        }

        return tagMapper.toResponseList(tags);
    }
}
