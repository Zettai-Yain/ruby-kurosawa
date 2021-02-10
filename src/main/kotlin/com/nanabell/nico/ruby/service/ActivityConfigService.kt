package com.nanabell.nico.ruby.service

import com.nanabell.nico.ruby.domain.ActivityConfig
import com.nanabell.nico.ruby.domain.ActivityConfigPatch
import com.nanabell.nico.ruby.entitiy.ActivityConfigEntity
import com.nanabell.nico.ruby.repository.ActivityConfigRepository
import javax.inject.Singleton
import javax.validation.ValidationException

@Singleton
class ActivityConfigService(
    private val repository: ActivityConfigRepository
) {

    fun findAll(): List<ActivityConfig> {
        return repository.findAll().map { ActivityConfig(it) }
    }

    fun find(id: Long): ActivityConfig? {
        val entity = repository.findById(id).orElse(null) ?: return null
        return ActivityConfig(entity)
    }

    fun exists(id: Long): Boolean {
        return repository.existsById(id)
    }

    fun create(config: ActivityConfig): ActivityConfig {
        validate(config)

        return ActivityConfig(repository.save(config.entity()))
    }

    fun patch(config: ActivityConfig, patch: ActivityConfigPatch): ActivityConfig {
        config.minGain = patch.minGain ?: config.minGain
        config.maxGain = patch.maxGain ?: config.maxGain

        validate(config)
        return repository.update(config.entity()).domain()
    }

    fun delete(config: ActivityConfig) {
        repository.deleteById(config.id)
    }

    private fun validate(config: ActivityConfig) {
        if (config.minGain > config.maxGain) throw ValidationException("min_gain(${config.minGain}) cannot be larger than max_gain(${config.maxGain})!")
        if (config.minGain < 0) throw ValidationException("min_gain(${config.minGain}) cannot be less than 0!")
        if (config.maxGain < 0) throw ValidationException("max_gain(${config.minGain}) cannot be less than 0!")
    }

}