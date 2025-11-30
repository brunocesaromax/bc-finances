package br.com.bcfinances.category.infrastructure.persistence;

import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.category.infrastructure.mappers.CategoryEntityMapper;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final CategoryJpaRepository jpaRepository;
    private final CategoryEntityMapper mapper;
    
    @Override
    public Category save(Category category) {
        CategoryEntity saved = jpaRepository.save(mapper.toEntity(category));
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findByTransactionType(TransactionType transactionType) {
        if (transactionType == null) {
            return findAll();
        }

        return jpaRepository.findByTransactionType(
                        br.com.bcfinances.transaction.infrastructure.persistence.TransactionTypeEntity.valueOf(transactionType.name()))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
