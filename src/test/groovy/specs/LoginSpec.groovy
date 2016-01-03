package specs

class LoginSpec extends ZdsClientSpec {

	def "When I fetch a user token, it should then be defined"() {
		expect:
		def token = client.tokens[username]
		token && token.access && token.refresh && !token.expired
	}


}
