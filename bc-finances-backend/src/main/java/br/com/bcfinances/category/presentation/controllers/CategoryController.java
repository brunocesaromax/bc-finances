package br.com.bcfinances.category.presentation.controllers;

import br.com.bcfinances.category.application.dto.CategoryRequest;
import br.com.bcfinances.category.application.dto.CategoryResponse;
import br.com.bcfinances.category.application.mappers.CategoryMapper;
import br.com.bcfinances.category.application.usecases.CreateCategoryUseCase;
import br.com.bcfinances.category.application.usecases.FindCategoryUseCase;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.infrastructure.event.ResourceCreatedEvent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindCategoryUseCase findCategoryUseCase;
    private final CategoryMapper categoryMapper;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CATEGORY')")
    public List<CategoryResponse> list() {
        List<Category> categories = findCategoryUseCase.findAll();
        return categoryMapper.toResponseList(categories);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE_CATEGORY')")
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryRequest request, HttpServletResponse response) {
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = createCategoryUseCase.execute(category);
        
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedCategory.getId()));
        
        CategoryResponse categoryResponse = categoryMapper.toResponse(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CATEGORY')")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return findCategoryUseCase.findById(id)
                .map(category -> ResponseEntity.ok(categoryMapper.toResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }
}
