package br.com.bcfinances.person.infrastructure.persistence;

import br.com.bcfinances.person.domain.contracts.PersonRepository;
import br.com.bcfinances.person.domain.entities.Person;
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
public class PersonCachingRepository implements PersonRepository {

    private final PersonRepositoryImpl delegate;

    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = "persons:byId", key = "#result.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "persons:all", allEntries = true),
                    @CacheEvict(cacheNames = "persons:search", allEntries = true)
            }
    )
    public Person save(Person person) {
        return delegate.save(person);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "persons:byId", key = "#id"),
                    @CacheEvict(cacheNames = "persons:all", allEntries = true),
                    @CacheEvict(cacheNames = "persons:search", allEntries = true)
            }
    )
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "persons:byId", key = "#id")
    public Optional<Person> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    @Cacheable(cacheNames = "persons:all")
    public List<Person> findAll() {
        return delegate.findAll();
    }

    @Override
    @Cacheable(cacheNames = "persons:search", key = "(#name?:'') + ':' + #page + ':' + #size")
    public PagedResult<Person> findAllByName(String name, int page, int size) {
        return delegate.findAllByName(name, page, size);
    }
}

