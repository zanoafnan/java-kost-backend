package com.kost.kostapi.specification;

import com.kost.kostapi.dto.kost.SearchKostRequest;
import com.kost.kostapi.entity.Kost;
import org.springframework.data.jpa.domain.Specification;

public final class KostSpecification {

    private KostSpecification() {
    }

    public static Specification<Kost> search(
            SearchKostRequest request
    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (request.name() != null && !request.name().isBlank()) {

                predicate.getExpressions().add(

                        cb.like(

                                cb.lower(root.get("name")),

                                "%" + request.name().toLowerCase() + "%"
                        )
                );
            }

            if (request.location() != null && !request.location().isBlank()) {

                predicate.getExpressions().add(

                        cb.like(

                                cb.lower(root.get("location")),

                                "%" + request.location().toLowerCase() + "%"
                        )
                );
            }

            if (request.minPrice() != null) {

                predicate.getExpressions().add(

                        cb.greaterThanOrEqualTo(

                                root.get("price"),

                                request.minPrice()
                        )
                );
            }

            if (request.maxPrice() != null) {

                predicate.getExpressions().add(

                        cb.lessThanOrEqualTo(

                                root.get("price"),

                                request.maxPrice()
                        )
                );
            }

            return predicate;
        };
    }

}