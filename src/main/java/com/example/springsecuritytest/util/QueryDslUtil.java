package com.example.springsecuritytest.util;

import com.example.springsecuritytest.domain.member.QMemberEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.example.springsecuritytest.domain.member.QMemberEntity.memberEntity;
import static org.springframework.util.ObjectUtils.isEmpty;

public class QueryDslUtil {

    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<QMemberEntity> fieldPath = Expressions.path(QMemberEntity.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }
    public static List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "id" :
                        OrderSpecifier orderId = QueryDslUtil.getSortedColumn(direction, memberEntity, "id");
                        ORDERS.add(orderId);
                        break;
                    case "username" :
                        OrderSpecifier orderUsername = QueryDslUtil.getSortedColumn(direction, memberEntity, "username");
                        ORDERS.add(orderUsername);
                    case "nickname" :
                        OrderSpecifier orderNickname = QueryDslUtil.getSortedColumn(direction, memberEntity, "nickname");
                        ORDERS.add(orderNickname);
                        break;
                    case "birth" :
                        OrderSpecifier orderBirth = QueryDslUtil.getSortedColumn(direction, memberEntity, "birth");
                        ORDERS.add(orderBirth);
                        break;
                    case "gender" :
                        OrderSpecifier orderGender = QueryDslUtil.getSortedColumn(direction, memberEntity, "gender");
                        ORDERS.add(orderGender);
                        break;
                    case "regdate" :
                        OrderSpecifier orderRegDate = QueryDslUtil.getSortedColumn(direction, memberEntity, "regDate");
                        ORDERS.add(orderRegDate);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
