package org.example.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFollow is a Querydsl query type for Follow
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFollow extends EntityPathBase<Follow> {

    private static final long serialVersionUID = -1843457234L;

    public static final QFollow follow = new QFollow("follow");

    public final NumberPath<Long> followerId = createNumber("followerId", Long.class);

    public final NumberPath<Long> followId = createNumber("followId", Long.class);

    public final NumberPath<Long> followingId = createNumber("followingId", Long.class);

    public QFollow(String variable) {
        super(Follow.class, forVariable(variable));
    }

    public QFollow(Path<? extends Follow> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFollow(PathMetadata metadata) {
        super(Follow.class, metadata);
    }

}

