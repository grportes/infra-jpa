package infra.controllers;

import infra.models.util.JPAUtil;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class Controller extends play.mvc.Controller {

    @Inject
    private JPAUtil jpaUtil;

    public Result withTransaction(final Supplier<Result> action) {
        return withTransaction(false, action);
    }

    public Result withTransaction(
        final Boolean readOnly,
        final Supplier<Result> action
    ) {
        return jpaUtil.withTransaction(em -> {
            try {
                return action.get();
            } catch (final Throwable e) {
                if (nonNull(em) && em.getTransaction().isActive()) em.getTransaction().rollback();
                return badRequest();
            }
        }, readOnly);
    }

}
