package infra.models.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository <T extends Serializable, ID> {

    void save( T model );

    void saveAll( List<T> models );

    void saveAll( List<T> models, boolean executeListener );

    T update( T model );

    void delete( T model );

    void deleteIfPresent( ID id );

    int deleteById( ID id );

    Optional<T> findById(ID id );

    T findByIdEx( ID id );

    List<T> findAll();

    void deleteAll( List<T> models );

    int deleteAll();
}