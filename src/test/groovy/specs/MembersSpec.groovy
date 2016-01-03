package specs

class MembersSpec extends ZdsClientSpec {

	def "Fetching members should return all the members"() {
		def members

		given:
		members = client.fetchMembers()

		expect:
		def me = members.find { it.username == 'Javier' }
		println me
		members.size() > 4000 && me
	}

}
