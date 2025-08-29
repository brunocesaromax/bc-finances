package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.domain.entities.Category;
import br.com.bcfinances.domain.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final CategoryJpaRepository jpaRepository;
    
    @Override
    public Category save(Category category) {
        CategoryEntity entity = new CategoryEntity(category.getName());
        CategoryEntity saved = jpaRepository.save(entity);
        return new Category(saved.getId(), saved.getName());
    }
    
    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id)
                .map(entity -> new Category(entity.getId(), entity.getName()));
    }
    
    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> new Category(entity.getId(), entity.getName()))
                .toList();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
