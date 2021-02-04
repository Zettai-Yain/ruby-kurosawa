package com.nanabell.nico.ruby.entitiy

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nanabell.nico.ruby.annotation.NoArgConstructor
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "activity_score")
@IdClass(ActivityScoreEntity.ActivityScoreId::class)
data class ActivityScoreEntity(

    @Id
    @Column(name = "id")
    val id: Long,

    @Id
    @Column(name = "source")
    var source: String = "unknown",

    @Column(name = "score")
    var score: Long = 0,

    @Transient
    @JsonIgnore
    var new: Boolean = false
) {
    @NoArgConstructor
    data class ActivityScoreId(
        var id: Long,
        var source: String,
    ) : Serializable

}