package infra.models.util;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class JPAUtil {

    private static final ThreadLocal<EntityManager> EM = new ThreadLocal<>();

    private final JPAApi jpaApi;

    @Inject
    public JPAUtil(final JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public EntityManager getEm() {
        return requireNonNull(EM.get(), "Entity Manager não carregado!");
    }

    public <T> T withTransaction(final Function<EntityManager, T> action) {
        return withTransaction(action, false);
    }

    public <T> T withTransaction(
        final Function<EntityManager, T> action,
        final Boolean readOnly
    ) {
        return jpaApi.withTransaction("default", readOnly, entityManager -> {
            try {
                EM.set(entityManager);
                return action.apply(entityManager);
            } finally {
                EM.remove();
            }
        });
    }
}
