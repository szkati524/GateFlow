package com.gateflow.GateFlow.model;

import com.gateflow.GateFlow.dto.VisitDto;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class VisitSpecification {

    public static Specification<Visit> search(String reg, String brand, String name, String surname, String company, LocalDate exitTime) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (reg != null && !reg.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("car").get("registrationNumber")), "%" + reg.toLowerCase() + "%"));
            }
            if (brand != null && !brand.isEmpty()){
                predicates.add(cb.like(cb.lower(root.get("car").get("brand")),"%" + brand.toLowerCase() + "%"));
            }
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("driver").get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (surname != null && !surname.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("driver").get("surname")), "%" + surname.toLowerCase() + "%"));
            }
            if (company != null && !company.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("company").get("name")), "%" + company.toLowerCase() + "%"));
            }
            if (exitTime != null) {
                predicates.add(cb.equal(
                        cb.function("DATE",LocalDate.class,root.get("exitTime")),
                        exitTime
                ));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

