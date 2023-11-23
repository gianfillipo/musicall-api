import javax.persistence.Column
import javax.persistence.*

@Entity
data class EventBi (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String?,

    @Column(nullable = false)
    var dayOfWeek: String?,

    @Column(nullable = false)
    var region: String?,

    @Column(nullable = false)
    var state: String?
)