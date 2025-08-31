package br.com.bcfinances.category.application.usecases;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
