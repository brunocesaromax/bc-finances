package br.com.bcfinances.category.infrastructure.persistence;

import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class CategoryCachingRepository implements CategoryRepository {

    private final CategoryRepositoryImpl delegate;

    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = "categories:byId", key = "#result.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "categories:all", allEntries = true),
                    @CacheEvict(cacheNames = "categories:byType", allEntries = true)
            }
    )
    public Category save(Category category) {
        return delegate.save(category);
    }

    @Override
    @Cacheable(cacheNames = "categories:byId", key = "#id")
    public Optional<Category> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    @Cacheable(cacheNames = "categories:all")
    public List<Category> findAll() {
        return delegate.findAll();
    }

    @Override
    @Cacheable(cacheNames = "categories:byType", key = "#transactionType.name()", condition = "#transactionType != null")
    public List<Category> findByTransactionType(TransactionType transactionType) {
        if (transactionType == null) {
            return delegate.findAll();
        }

        return delegate.findByTransactionType(transactionType);
    }
}
