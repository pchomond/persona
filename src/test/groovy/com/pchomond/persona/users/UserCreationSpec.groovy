package com.pchomond.persona.users

import com.pchomond.persona.testconfig.DatabaseCleaner
import com.pchomond.persona.testconfig.EnablePostgresTestContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient
import spock.lang.Specification
import tools.jackson.databind.ObjectMapper

@EnablePostgresTestContainer
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class UserCreationSpec extends Specification {

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private RestTestClient restTestClient

    @Autowired
    private DatabaseCleaner databaseCleaner;

    def setup() {
        databaseCleaner.clearDatabase()
    }

    def "Should successfully create a user and persist to database"() {
        given: "a valid user creation request payload"
        def payload = [
                idp_id: UUID.randomUUID().toString(),
                email: "foo@gmail.com",
                given_name: "Foo",
                surname: "Bar",
                date_of_birth: [
                        "day": 2,
                        "month": 10,
                        "year": 1990
                ],
                address: [
                        line1: "Navarinou 25",
                        line2: "Acheloou 3",
                        city: "Athens",
                        postal_code: "261 93",
                        region: "Attica",
                        country: "Greece"
                ]
        ]

        when: "the client sends a POST request to the users endpoint"
        def result = restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange()
                .returnResult()

        then: "the response status is 201 Created"
        result.status.value() == 201
        result.responseHeaders.contentType == MediaType.APPLICATION_JSON

        // TODO: Update this when GET endpoint is implemented to assert correct persistence
    }

    def "Should reject invalid request with 400 Bad Request"() {
        given: "an invalid user creation request payload"
        def payload = [
                idp_id: UUID.randomUUID().toString(),
                email: "1foo@.gmail.com",
                given_name: "Foo",
                surname: "Bar"
        ]

        when: "the client sends a POST request to the users endpoint"
        def result = restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange()
                .returnResult()

        then: "the response status is 400 Bad Request"
        result.status.value() == 400
        result.responseHeaders.contentType == MediaType.APPLICATION_PROBLEM_JSON
    }

    def "Should reject duplicate request with 409 Resource Conflict"() {
        given: "a user creation request payload is submitted"
        def payload = [
                idp_id: UUID.randomUUID().toString(),
                email: "foo@gmail.com",
                given_name: "Foo",
                surname: "Bar"
        ]

        restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .exchange()
                .expectStatus().isCreated()

        when: "the same request payload is submitted again"
        def result = restTestClient.post().uri("/internal/users")
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .body(payload)
                                   .exchange()
                                   .returnResult()

        then: "the response status is 409 Bad Request"
        result.status.value() == 409
        result.responseHeaders.contentType == MediaType.APPLICATION_PROBLEM_JSON
    }
}
