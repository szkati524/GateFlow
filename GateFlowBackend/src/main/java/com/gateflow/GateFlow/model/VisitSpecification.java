package com.gateflow.GateFlow.model;

import com.gateflow.GateFlow.dto.VisitDto;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class VisitSpecification {

    public static Specification<Visit> search(String reg, String brand, String name, String surname, String company,LocalDate entryDate,LocalTime entryTime, LocalTime exitTime) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            var carJoin = root.join("car", JoinType.INNER);
            var driverJoin = root.join("driver",JoinType.INNER);
            var companyJoin = root.join("company",JoinType.INNER);

            if (reg != null && !reg.isEmpty()) {
                predicates.add(cb.like(cb.lower(carJoin.get("registrationNumber")), "%" + reg.toLowerCase() + "%"));
            }
            if (brand != null && !brand.isEmpty()) {
                predicates.add(cb.like(cb.lower(carJoin.get("brand")), "%" + brand.toLowerCase() + "%"));
            }
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(driverJoin.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (surname != null && !surname.isEmpty()) {
                predicates.add(cb.like(cb.lower(driverJoin.get("surname")), "%" + surname.toLowerCase() + "%"));
            }
            if (company != null && !company.isEmpty()) {
                predicates.add(cb.like(cb.lower(companyJoin.get("name")), "%" + company.toLowerCase() + "%"));
            }


            if (entryDate != null) {
                predicates.add(cb.equal(cb.function("FORMATDATETIME", String.class, root.get("entryTime"), cb.literal("yyyy-MM-dd")), entryDate.toString()));
            }
            if (entryTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(cb.function("FORMATDATETIME", String.class, root.get("entryTime"), cb.literal("HH:mm:ss")), entryTime.toString()));
            }
            if (exitTime != null) {
                predicates.add(cb.lessThanOrEqualTo(cb.function("FORMATDATETIME", String.class, root.get("exitTime"), cb.literal("HH:mm:ss")), exitTime.toString()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

