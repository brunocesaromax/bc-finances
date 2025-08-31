package br.com.bcfinances.category.application.usecases;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    
    public Category execute(Category category) {
        if (!category.isValid()) {
            throw new IllegalArgumentException("Category name must be between 3 and 50 characters");
        }
        return categoryRepository.save(category);
    }
}
