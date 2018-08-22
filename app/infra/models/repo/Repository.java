package infra.models.repo;

import infra.BusinessException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository <T extends Serializable, ID> {

    void save( T model );

    void saveAll( List<T> models );

    void saveAll( List<T> models, boolean executeListener );

    T update( T model );

    void delete( T model );

    void delete( ID id );

    int deletePorId( ID id );

    Optional<T> findById(ID id );

    T findByIdEx( ID id ) throws BusinessException;

    List<T> findAll();

    void deleteAll( List<T> models );

    int deleteAll();
}