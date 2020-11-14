package infra.models.repo.impl;

import infra.Model;
import infra.models.util.JPAUtil;
import infra.models.repo.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static infra.UtilCollections.isVazia;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

public abstract class JPARepository<T extends Model, ID> implements Repository<T,ID> {

    private final Class<T> modelClass;

    @Inject
    private JPAUtil jpaUtil;

    @SuppressWarnings("unchecked")
    public JPARepository() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        this.modelClass = (Class) pt.getActualTypeArguments()[0];
    }

    protected EntityManager getEm() {
        return this.jpaUtil.getEm();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void save( T model ) {
        beforeSave( model );
        getEm().persist( model );
        postSave( model );
    }

    /**
     * {@inheritDoc}
     */
    protected void beforeSave( T model ) {

    }

    /**
     * {@inheritDoc}
     */
    protected void postSave( T model ) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll( List<T> models ) {
        if ( isVazia( models ) ) return;
        models.forEach( this::save );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(
        final List<T> models,
        boolean executeListener
    ) {
        if ( isVazia( models ) ) return;
        if ( executeListener )
            saveAll( models );
        else
            models.forEach( ( model ) -> getEm( ).persist( model ) );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T update( T model ) {
        return getEm().merge( model );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( T model ) {
        getEm().remove( model );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIfPresent( ID id ) {
        findById( id ).ifPresent( this::delete );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteById( ID id ) {
        return getEm()
                .createQuery( format( "delete from %s where id = :id ", modelClass ) )
                .setParameter( "id", id )
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findById( final ID id ) {
        return ofNullable( getEm().find( modelClass, id ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findByIdEx( final ID id ) {
        return findById( id ).orElseThrow( () -> new PersistenceException( format(
            "[ %s ] Não localizou registro ID: %s", modelClass.getSimpleName(), id )
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return getEm().createQuery( format( "from %s", modelClass.getSimpleName() ), modelClass ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll( List<T> models ) {
        if ( isVazia( models ) ) return;
        models.forEach( this::delete );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteAll() {
        return getEm().createQuery( format( "delete %s bean", modelClass.getSimpleName() ) ).executeUpdate();
    }

    /**
     * Retorna data atual do banco.
     *
     * @return Data com hora, minuto e segundos.
     */
    protected LocalDateTime getHoje() {
        return requireNonNull(
            getEm().createNamedQuery("select current_timestamp()", Timestamp.class).getSingleResult()
        ).toLocalDateTime();
    }

    /**
     * Retorna data atual do banco.
     *
     * @return Data sem hora/minuto.
     */
    protected LocalDate getHojeHora() {
        return getHoje().toLocalDate();
    }

    /**
     * Converte para lista de Long.
     *
     * <pre>{@code
     *
     *      List<Tuple> dados = getEm().createNamedQuery( .... );
     *      List<Long> dadosL = toListLong( dados );
     *
     * }</pre>
     *
     * @param lista Lista de dados.
     *
     * @return Lista de dados.
     */
    protected List<Long> toListLong( final List<Tuple> lista ) {
        return  Arrays
            .stream( emptyIfNull(lista).toArray() )
            .mapToLong( value -> ((Number) value).longValue() )
            .boxed()
            .collect( toList() );
    }

}
