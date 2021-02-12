package com.nanabell.nico.ruby.entitiy

import com.nanabell.nico.ruby.annotation.NoArgConstructor
import com.nanabell.nico.ruby.domain.ActivityMultiplier
import com.nanabell.nico.ruby.domain.ActivityMultiplierType
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "activity_multipler")
@IdClass(ActivityMultiplierEntity.ActivityMultiplierId::class)
data class ActivityMultiplierEntity(

    @Id
    @Column(name = "id")
    val id: Long,

    @Id
    @Column(name = "type")
    val type: ActivityMultiplierType,

    @Column(name = "multiplier")
    var multiplier: Float

) {
    @NoArgConstructor
    data class ActivityMultiplierId(
        var id: Long,
        var type: ActivityMultiplierType
    ) : Serializable

    fun domain() = ActivityMultiplier(this)
}
