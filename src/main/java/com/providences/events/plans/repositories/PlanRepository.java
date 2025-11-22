package com.providences.events.plans.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.entities.PlanEntity.PlanType;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, String> {
    @Query("""
                Select obj
                FROM  PlanEntity obj
                LEFT JOIN FETCH  obj.subscriptions
                WHERE obj.id = :id
            """)
    Optional<PlanEntity> fetchById(@Param("id") String id);

    @Query("""
                Select obj
                FROM  PlanEntity obj
                WHERE obj.id = :id AND obj.planType=:planType
            """)
    Optional<PlanEntity> getByIdAndType(@Param("id") String id, @Param("planType") PlanType planType);

    
    Set<PlanEntity> findAllByPlanType(PlanType planType);

}