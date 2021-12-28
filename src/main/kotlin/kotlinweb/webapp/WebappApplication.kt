package kotlinweb.webapp

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.collections.HashSet


@SpringBootApplication
class WebappApplication

fun main(args: Array<String>) {
    runApplication<WebappApplication>(*args)
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)


@RestController
class MessageResource constructor(val service: MessageService, val userRepository: UserRepository) {
    private val logger: Logger = LoggerFactory.getLogger(MessageResource::class.java)
//	@GetMapping
//	fun index(): List<Message> = listOf(
//		Message("1", "Hello!"),
//		Message("2", "Bonjour!"),
//		Message("3", "Privet!"),
//	)

    @GetMapping
    fun index(): List<Message> = service.findMessages()

    @PostMapping
    fun post(@RequestBody message: Message) {
        service.post(message)

        val address1 = Address("Waldstrasse 1", "04105", "Leipzig")
        val address2 = Address("Christianstrasse 2", "04105", "Leipzig")
        val address3 = Address("Lindenauer Markt 21", "04177", "Leipzig")

        val user1 = User( UUID.randomUUID().toString(), "testFirstName", "testLastName1", listOf(address1, address2))
        val user2 = User( UUID.randomUUID().toString(), "testFirstName", "testLastName2", listOf(address2, address3))

        val hs = HashSet<User>()
        hs.add(user1)
        hs.add(user2)
        //userRepository.deleteAll()

        logger.info("Saving users : {}", user1)
        //userRepository.save(user1)
        userRepository.saveAll(hs)
    }
}


interface MessageRepository : CrudRepository<Message, String> {
    @Query("select * from messages")
    fun findMessages(): List<Message>
}


@Service
class MessageService(val db: MessageRepository) {

    fun findMessages(): List<Message> = db.findMessages()

    fun post(message: Message) {
        db.save(message)
    }
}
