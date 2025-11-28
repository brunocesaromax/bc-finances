package br.com.bcfinances.category.application.usecases;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
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

    public List<Category> findByTransactionType(TransactionType transactionType) {
        return categoryRepository.findByTransactionType(transactionType);
    }
}
