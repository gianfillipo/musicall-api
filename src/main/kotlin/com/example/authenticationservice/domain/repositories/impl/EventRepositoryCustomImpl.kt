package com.example.authenticationservice.domain.repositories.impl

import com.example.authenticationservice.domain.repositories.EventRepositoryCustom
import com.example.authenticationservice.domain.entities.Event
import com.example.authenticationservice.domain.entities.EventJob
import com.example.authenticationservice.domain.entities.Musician
import com.example.authenticationservice.application.web.dto.request.FilterEventsRequest
import com.example.authenticationservice.application.web.dto.response.EventSearchDto
import org.springframework.stereotype.Repository
import java.util.Date
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate

@Repository
class EventRepositoryCustomImpl (
   private val em: EntityManager
) : EventRepositoryCustom {

    override fun findUnfinalizedEventsAfterOrEqual(
        filterEventsRequest: com.example.authenticationservice.application.web.dto.request.FilterEventsRequest,
        instrumentsId: List<Long>
    ): List<EventSearchDto> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(EventSearchDto::class.java)
        val root = cq.from(Event::class.java)
        cq.select(
            cb.construct(
                EventSearchDto::class.java,
                root.get<Long>("id"),
                root.get<String>("name"),
                root.get<String>("imageUrl"),
                root.get<LocalDate>("eventDate"),
                root.get<Date>("startHour"),
                root.get<String>("cep")
            )
        ).distinct(true)

        val joinExec = root.join<Event, EventJob>("eventJob")

        val predicates = mutableListOf<Predicate>()
        predicates.add(cb.equal(root.get<Boolean>("finalized"), false))
        predicates.add(cb.greaterThan(root.get<LocalDate>("eventDate"), LocalDate.now()))
        predicates.add(cb.isNull(joinExec.get<Musician>("musician")))

        if (filterEventsRequest.date != null) {
            predicates.add(cb.equal(root.get<LocalDate>("eventDate"), filterEventsRequest.date))
        }

        if (filterEventsRequest.payment != null) {
            predicates.add(cb.greaterThanOrEqualTo(joinExec.get<Double>("payment"), filterEventsRequest.payment))
        }

        if (filterEventsRequest.instrumentsId != null) {
            predicates.add(joinExec.get<Long>("instrument").`in`(filterEventsRequest.instrumentsId))
        }

        else {
            predicates.add(joinExec.get<Long>("instrument").`in`(instrumentsId))
        }

        cq.where(*predicates.toTypedArray())

        val result = em.createQuery(cq).resultList

        return result.toList()
    }
}