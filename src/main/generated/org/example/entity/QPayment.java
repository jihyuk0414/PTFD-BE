package org.example.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -1416279511L;

    public static final QPayment payment = new QPayment("payment");

    public final StringPath orderName = createString("orderName");

    public final StringPath paymentId = createString("paymentId");

    public final DateTimePath<java.sql.Timestamp> purchaseAt = createDateTime("purchaseAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> purchaseId = createNumber("purchaseId", Integer.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public final StringPath userEmail = createString("userEmail");

    public QPayment(String variable) {
        super(Payment.class, forVariable(variable));
    }

    public QPayment(Path<? extends Payment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPayment(PathMetadata metadata) {
        super(Payment.class, metadata);
    }

}

