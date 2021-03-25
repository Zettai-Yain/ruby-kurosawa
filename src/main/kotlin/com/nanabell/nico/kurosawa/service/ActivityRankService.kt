package com.nanabell.nico.kurosawa.service

import com.nanabell.nico.kurosawa.domain.ActivityRank
import com.nanabell.nico.kurosawa.domain.ActivityRankAdd
import com.nanabell.nico.kurosawa.domain.ActivityRankPatch
import com.nanabell.nico.kurosawa.entitiy.ActivityRankEntity
import com.nanabell.nico.kurosawa.repository.ActivityRankRepository
import javax.inject.Singleton
import javax.validation.ValidationException

@Singleton
class ActivityRankService(private val repository: ActivityRankRepository) {

    fun findAll(): List<ActivityRank> {
        return repository.findAll().map { it.domain() }
    }

    fun find(id: Int): ActivityRank? {
        return repository.findById(id).orElse(null)?.domain()
    }

    fun add(add: ActivityRankAdd): ActivityRank {
        if (findAll().any { it.roleId == add.roleId })
            throw ValidationException("Role with the Id ${add.roleId} already present!")

        validate(add.roleId, add.score)
        return repository.save(ActivityRankEntity(0, add.roleId, add.score)).domain()
    }

    fun patch(rank: ActivityRank, patch: ActivityRankPatch): ActivityRank {
        rank.roleId = patch.roleId ?: rank.roleId
        rank.score = patch.score ?: rank.score

        validate(rank.roleId, rank.score)
        return repository.update(rank.entity()).domain()
    }

    fun delete(rank: ActivityRank) {
        repository.deleteById(rank.id)
    }

    private fun validate(roleId: Long, score: Long) {
        if (roleId < 0) throw ValidationException("Role ID($roleId) cannot be negative!")
        if (score < 0) throw ValidationException("Score($roleId) cannot be negative!")
    }

}
