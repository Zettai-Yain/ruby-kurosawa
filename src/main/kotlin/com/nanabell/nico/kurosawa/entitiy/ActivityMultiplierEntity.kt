package com.nanabell.nico.kurosawa.entitiy

import com.nanabell.nico.kurosawa.annotation.NoArgConstructor
import com.nanabell.nico.kurosawa.domain.ActivityMultiplier
import com.nanabell.nico.kurosawa.domain.ActivityMultiplierType
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "activity_multiplier")
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
