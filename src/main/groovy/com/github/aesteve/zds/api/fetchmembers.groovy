package com.github.aesteve.zds.api

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

builder = new HTTPBuilder('https://zestedesavoir.com')

def fetch(int page = 1) {
	builder.request(GET, JSON) {
		uri.path = '/api/membres'
		uri.query = [page: page]
		headers = [

		]

		response.success = { resp, json ->
			println json
		}
	}
}
