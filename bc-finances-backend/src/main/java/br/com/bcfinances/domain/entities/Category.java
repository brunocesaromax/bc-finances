package br.com.bcfinances.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    
    public Category(String name) {
        this.name = name;
    }
    
    public boolean isValid() {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 50;
    }
}
