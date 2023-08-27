package com.example.authenticationservice.dao.impl

import com.example.authenticationservice.dao.MusicianRepositoryCustom
import com.example.authenticationservice.dto.MusicianDto
import com.example.authenticationservice.dto.MusicianEventJobDto
import com.example.authenticationservice.dto.PageMusicianEventJobDto
import com.example.authenticationservice.model.*
import com.example.authenticationservice.parameters.FilterMusicianRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.criteria.Predicate
import javax.persistence.EntityManager
import javax.persistence.Tuple
import javax.persistence.criteria.Selection

@Repository
class MusicianRepositoryImpl (
    private val em: EntityManager
): MusicianRepositoryCustom {
    override fun findMusicianByIdAndEventLocation(instrumentId: Long, filterMusicianRequest: FilterMusicianRequest, page: Pageable): PageImpl<MusicianEventJobDto> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(MusicianEventJobDto::class.java)
        val root = cq.from(Musician::class.java)
        val joinMusicianInstrument = root.join<Musician, MusicianInstrument>("musicianInstruments")

        cq.select(
            cb.construct(
                MusicianEventJobDto::class.java,
                root.get<Long>("id"),
                root.get<String>("user").get<String>("name"),
                root.get<String>("cep"),
                root.get<String>("imageUrl"),
                joinMusicianInstrument.get<Instrument>("instrument").get<String>("name")
            )
        )

        val predicates = mutableListOf<Predicate>()

        if (filterMusicianRequest.date != null) {
            predicates.add(cb.equal(root.get<LocalDate>("eventDate"), filterMusicianRequest.date))
        }

        if (filterMusicianRequest.instrumentsId != null) {
            predicates.add(joinMusicianInstrument.get<Long>("instrument").`in`(filterMusicianRequest.instrumentsId))
        }

        predicates.add(cb.equal(joinMusicianInstrument.get<Instrument>("instrument").get<Long>("id"), instrumentId))

        cq.where(*predicates.toTypedArray())

        val typedQuery = em.createQuery(cq)
        val totalSize = typedQuery.resultList.size
        typedQuery.setFirstResult(page.pageNumber)
        typedQuery.setMaxResults(page.pageSize)

        val pageImpl = PageImpl(typedQuery.resultList, page, totalSize.toLong())

        return pageImpl
    }
}