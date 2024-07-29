package org.example.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1652268521L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> follower = createNumber("follower", Integer.class);

    public final NumberPath<Integer> following = createNumber("following", Integer.class);

    public final ComparablePath<Character> gender = createComparable("gender", Character.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberInfo = createString("memberInfo");

    public final StringPath nickName = createString("nickName");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImage = createString("profileImage");

    public final StringPath role = createString("role");

    public final NumberPath<Integer> socialType = createNumber("socialType", Integer.class);

    public final StringPath userName = createString("userName");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

