package br.com.bcfinances.repository;

import br.com.bcfinances.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
