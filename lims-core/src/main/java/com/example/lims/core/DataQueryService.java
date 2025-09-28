package com.example.lims.core;

import com.example.lims.core.Sample;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

@Service
public class DataQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Sample> findSamplesByCriteria(Map<String, String> criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sample = cq.from(Sample.class);

        Predicate predicate = cb.conjunction();
        if (criteria.containsKey("type")) {
            predicate = cb.and(predicate, cb.equal(sample.get("type"), criteria.get("type")));
        }
        if (criteria.containsKey("status")) {
            predicate = cb.and(predicate, cb.equal(sample.get("status"), criteria.get("status")));
        }
        // More criteria can be added here for other fields

        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }
}
