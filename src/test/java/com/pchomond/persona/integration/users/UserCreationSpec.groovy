package com.pchomond.persona.integration.users

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
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

        and: "the body contains the created User"
        result.requestHeaders.contentType == MediaType.APPLICATION_JSON
        def document = convertResponseToDocumentContext(result.responseBodyContent)
        verifyAll(document) {
            read("\$.user_id") != null
            read("\$.idp_id") == payload.idp_id
            read("\$.given_name") == payload.given_name
            read("\$.surname") == payload.surname
            read("\$.email") == payload.email
            read("\$.date_of_birth") != null
            read("\$.date_of_birth.day") == payload.date_of_birth.day
            read("\$.date_of_birth.month") == payload.date_of_birth.month
            read("\$.date_of_birth.year") == payload.date_of_birth.year
            read("\$.address") != null
            read("\$.address.line1") == payload.address.line1
            read("\$.address.line2") == payload.address.line2
            read("\$.address.city") == payload.address.city
            read("\$.address.postal_code") == payload.address.postal_code
            read("\$.address.region") == payload.address.region
            read("\$.address.country") == payload.address.country
        }
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

        and: "the body contains the created User"
        result.requestHeaders.contentType == MediaType.APPLICATION_JSON
    }

    private static DocumentContext convertResponseToDocumentContext(byte[] responseBody) {
        def json = new String(responseBody)
        return JsonPath.parse(json)
    }
}
