package infra.models.repo.impl;

import infra.BusinessException;
import infra.Model;
import infra.models.repo.Repository;
import org.hibernate.Query;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static infra.UtilCollections.isVazia;
import static infra.UtilString.isVazia;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.hibernate.transform.Transformers.aliasToBean;

public abstract class JPARepository<T extends Model, ID> implements Repository<T,ID> {

    private Class<T> modelClass;

    @Inject
    private JPAApi jpaApi;

    @SuppressWarnings("unchecked")
    public JPARepository() {

        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        this.modelClass = (Class) pt.getActualTypeArguments()[0];
    }

    protected EntityManager getEm() {

        return this.jpaApi.em();
    }

    /**
     * JPA : persist
     */
    @Override
    public void save( T model ) {

        try {
            beforeSave( model );
            getEm().persist( model );
            postSave( model );
        } catch ( BusinessException e ) {
            throw new PersistenceException( e );
        }
    }

    /**
     * JPA: antes de aplicar persist
     *
     * @param model
     * @throws BusinessException
     */
    protected void beforeSave( T model ) throws BusinessException {

    }

    /**
     * JPA: após de aplicar persist
     *
     * @param model
     * @throws BusinessException
     */
    protected void postSave( T model ) throws BusinessException {

    }

    /**
     * Persiste lista de objetos
     *
     * @param models
     */
    @Override
    public void saveAll( List<T> models ) {

        if ( isVazia( models ) )
            return;

        models.forEach( this::save );
    }

    /**
     * Persiste lista de objetos
     *
     * @param models
     */
    @Override
    public void saveAll( List<T> models,
                         boolean executeListener ) {

        if ( isVazia( models ) )
            return;

        if ( executeListener )
            saveAll( models );
        else
            models.forEach( ( model ) -> getEm( ).persist( model ) );
    }


    /**
     * JPA : merge
     */
    @Override
    public T update( T model ) {

        return getEm().merge( model );
    }

    /**
     * JPA : remove
     */
    @Override
    public void delete( T model ) {

        getEm().remove( model );
    }

    /**
     * JPA : remove
     */
    @Override
    public void delete( ID id ) {

        findById( id ).ifPresent( this::delete );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deletePorId( ID id ) {

        return getEm()
                .createQuery( format( "delete from %s where id = :id ", modelClass ) )
                .setParameter( "id", id )
                .executeUpdate();
    }

    /**
     * Pesquisa pelo ID.
     *
     * @param id
     *
     * @return
     */
    @Override
    public Optional<T> findById(ID id ) {

        return ofNullable( getEm().find( modelClass, id ) );
    }

    /**
     * Pesquisa pelo ID. Caso não localize registro lança Business
     *
     * @param id
     *
     * @return
     * @throws BusinessException
     */
    @Override
    public T findByIdEx( ID id ) throws BusinessException {

        return findById( id )
                .orElseThrow( () -> new BusinessException( format( "[ %s ] Não localizou registro ID: %s", modelClass.getSimpleName(), id ) ) );
    }

    /**
     * Pesquisa todos.
     *
     * @return
     */
    @Override
    public List<T> findAll() {

        return getEm().createQuery( format( "from %s", modelClass.getSimpleName() ), modelClass ).getResultList();
    }

    /**
     * Exclui todos.
     *
     * @param models
     */
    @Override
    public void deleteAll( List<T> models ) {

        if ( isVazia( models ) )
            return;

        models.forEach( this::delete );
    }

    /**
     * Exclui todos
     *
     * @return Qtde de registros excluidos
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
    protected Date getHoje() {

        return (Date) getEm().createNativeQuery( "select getdate()" ).getSingleResult();
    }

    /**
     * Retorna data atual do banco.
     *
     * @return Data sem hora/minuto.
     */
    protected Date getHojeSemHora() {

        return (Date) getEm().createNativeQuery( "select convert(date,getdate())" ).getSingleResult();
    }

//    protected StatelessSession getStatelessSession() {
//
//        return ( (Session) getEm().unwrap( HibernateEntityManager.class ).getDelegate() ).getSessionFactory().getCurrentSession().getSessionFactory().openStatelessSession();
//    }
//
    /**
     * Create an instance of Query for executing a named query.
     *
     * @param namedQuery    the name of a query defined in metadata
     * @param clazz         the class of the object to be returned
     *
     * @return  the new query instance
     */
    protected Query createNamedQueryResultTrans(final String namedQuery,
                                                final Class<?> clazz ) {

        if ( isVazia( namedQuery ) )
            throw new IllegalArgumentException( "É necessário informar [ namedQuery ]" );

        if ( clazz == null )
            throw new IllegalArgumentException( "É necessário informar [ clazz ]" );

        return getEm()
            .createNamedQuery( namedQuery )
            .unwrap( Query.class )
            .setResultTransformer( aliasToBean( clazz ) );
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
            .stream( lista.toArray() )
            .mapToLong( value -> ((Number) value).longValue() )
            .boxed()
            .collect( toList() );
    }

}
