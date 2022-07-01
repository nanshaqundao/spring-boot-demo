package com.example.ch03.repo;

import com.example.ch03.model.Ingredient;
import com.example.ch03.model.IngredientRef;
import com.example.ch03.model.Taco;
import com.example.ch03.model.TacoOrder;
import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {


        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order (delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, created_at)" +
                        "values (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );

        pscf.setReturnGeneratedKeys(true);

        //tacoOrder.setCreatedAt(new Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        tacoOrder.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tacoOrder.getDeliveryName(),
                        tacoOrder.getDeliveryStreet(),
                        tacoOrder.getDeliveryCity(),
                        tacoOrder.getDeliveryState(),
                        tacoOrder.getDeliveryZip(),
                        tacoOrder.getCcNumber(),
                        tacoOrder.getCcExpiration(),
                        tacoOrder.getCcCVV(),
                        tacoOrder.getCreatedAt()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        tacoOrder.setId(orderId);

        List<Taco> tacos = tacoOrder.getTacos();
        int tacoIndexOfOrder = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, tacoIndexOfOrder++, taco);
        }

        return tacoOrder;
    }

    private long saveTaco(long orderId, int tacoIndexOfOrder, Taco taco) {
        taco.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco (name, created_at, taco_order, taco_order_key)" +
                        "values (?,?,?,?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatedAt(),
                        orderId,
                        tacoIndexOfOrder
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        //long tacoId = keyHolder.getKeyAs(Long.class);
        //long tacoId = keyHolder.getKey().longValue();
        long tacoId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        taco.setId(tacoId);
        saveIngredientRefs(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;

        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key)" +
                            "values (?,?,?)",
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
