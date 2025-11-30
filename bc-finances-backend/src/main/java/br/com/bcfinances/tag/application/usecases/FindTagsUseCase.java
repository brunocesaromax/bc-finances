package br.com.bcfinances.tag.application.usecases;

import br.com.bcfinances.tag.domain.contracts.TagRepository;
import br.com.bcfinances.tag.domain.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTagsUseCase {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<Tag> execute() {
        return tagRepository.findAll();
    }
}
