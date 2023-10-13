package com.example.authenticationservice.domain.repositories.impl

import com.example.authenticationservice.domain.repositories.MusicianRepositoryCustom
import com.example.authenticationservice.domain.entities.Instrument
import com.example.authenticationservice.domain.entities.Musician
import com.example.authenticationservice.domain.entities.MusicianInstrument
import com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest
import com.example.authenticationservice.application.web.dto.response.MusicianEventJobDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.criteria.Predicate
import javax.persistence.EntityManager

@Repository
class MusicianRepositoryImpl (
    private val em: EntityManager
): MusicianRepositoryCustom {
    override fun findMusicianByIdAndEventLocation(filterMusicianRequest: com.example.authenticationservice.application.web.dto.request.FilterMusicianRequest, page: Pageable): PageImpl<MusicianEventJobDto> {
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

        cq.where(*predicates.toTypedArray())

        val typedQuery = em.createQuery(cq)
        val totalSize = typedQuery.resultList.size
        typedQuery.setFirstResult(page.pageNumber)
        typedQuery.setMaxResults(page.pageSize)

        val pageImpl = PageImpl(typedQuery.resultList, page, totalSize.toLong())

        return pageImpl
    }
}