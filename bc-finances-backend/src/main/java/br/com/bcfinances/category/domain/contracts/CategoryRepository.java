package br.com.bcfinances.category.domain.contracts;

import br.com.bcfinances.category.domain.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
}
