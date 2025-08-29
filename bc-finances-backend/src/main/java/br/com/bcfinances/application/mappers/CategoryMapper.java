package br.com.bcfinances.application.mappers;

import br.com.bcfinances.application.dto.CategoryRequest;
import br.com.bcfinances.application.dto.CategoryResponse;
import br.com.bcfinances.domain.entities.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    
    public Category toEntity(CategoryRequest request) {
        return new Category(request.getName());
    }
    
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
    
    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .toList();
    }
}
