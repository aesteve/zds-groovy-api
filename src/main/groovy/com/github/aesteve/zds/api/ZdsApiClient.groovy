package com.github.aesteve.zds.api

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

class ZdsApiClient {

	private HTTPBuilder http
	String clientId
	String clientSecret
	Map<String, AccessToken> tokens = [:] // by member
	String currentUser

	public ZdsApiClient(String clientId, String clientSecret) {
		this.clientId = clientId
		this.clientSecret = clientSecret
		http = new HTTPBuilder('https://zestedesavoir.com')
		def respHandler = { resp, json ->
			[response: resp, json: json]
		}
		http.handler.success = respHandler
		http.handler.failure = respHandler
	}

	def az(String username) {
		currentUser = username
	}

	def login(String username, String password) {

		def map = http.request(POST) {
			uri.path = '/oauth2/token/'
			requestContentType = URLENC
			headers.Accept = 'application/json'
			body = [
				client_id: clientId,
				client_secret: clientSecret,
				grant_type: 'password',
				username: username,
				password: password
			]

		}

		if(map.response.success) {
			tokens[username] = AccessToken.fromJSON(map.json)
			az(username)
		} else {
			throw new RuntimeException("Error $map.response.statusLine . $map.json")
		}

	}

	List fetchMembers() {
		fetchMembersPage(1, [])
	}

	String getCurrentToken() {
		tokens[currentUser].access
	}

	private List fetchMembersPage(int page, List members) {
		def token = currentToken
		def map = http.request(GET, JSON) {
			uri.path = '/api/membres'
			uri.query = [page: page, page_size: 100]
			headers.Authorization = "Bearer $token"
		}

		if(map.response.success) {
			println "adding ${map.json.results.size()} members"
			members += map.json.results
			Integer nextPage = readNext(map.json.next)
			println "next page = $nextPage"
			if (!nextPage) return members
			else return fetchMembersPage(nextPage, members)
		} else {
			throw new RuntimeException("Error $map.response.statusLine . $map.json")
		}

	}

	private static Integer readNext(String next) {
		if (!next) return
		Integer.valueOf getParams(next)['page']
	}

	private static Map getParams(String urlStr) {
		URL url = new URL(urlStr)
		url.query.split('&').collectEntries { param ->
			param.split('=').collect { URLDecoder.decode(it, 'UTF-8') }
		}
	}
}
