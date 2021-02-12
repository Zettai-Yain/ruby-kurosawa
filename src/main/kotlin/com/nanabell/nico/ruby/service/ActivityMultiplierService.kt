package com.nanabell.nico.ruby.service

import com.nanabell.nico.ruby.domain.ActivityMultiplier
import com.nanabell.nico.ruby.domain.ActivityMultiplierAdd
import com.nanabell.nico.ruby.domain.ActivityMultiplierType
import com.nanabell.nico.ruby.entitiy.ActivityMultiplierEntity
import com.nanabell.nico.ruby.repository.ActivityMultiplierRepository
import javax.inject.Singleton
import javax.validation.ValidationException

@Singleton
class ActivityMultiplierService(private val repository: ActivityMultiplierRepository) {

    fun findAll(): List<ActivityMultiplier> {
        return repository.findAll().map { it.domain() }
    }

    fun find(id: Long): List<ActivityMultiplier> {
        return repository.findById(id).map { it.domain() }
    }

    fun find(id: Long, type: ActivityMultiplierType): ActivityMultiplier? {
        return repository.findByIdAndType(id, type)?.domain()
    }

    fun add(add: ActivityMultiplierAdd): ActivityMultiplier {
        if (find(add.id, add.type) != null)
            throw ValidationException("Multiplier with Id: ${add.id} and type: ${add.type} already exists!")

        validate(add.multiplier)
        return repository.save(ActivityMultiplierEntity(add.id, add.type, add.multiplier)).domain()
    }

    fun patch(multiplier: ActivityMultiplier, patch: Float): ActivityMultiplier {
        multiplier.multiplier = patch

        validate(multiplier.multiplier)
        return repository.update(multiplier.entity()).domain()
    }

    fun delete(multiplier: ActivityMultiplier) {
        repository.delete(multiplier.entity())
    }

    private fun validate(multiplier: Float) {
        if (multiplier < 0) throw ValidationException("Multiplier($multiplier) cannot be smaller than 0!")
    }
}