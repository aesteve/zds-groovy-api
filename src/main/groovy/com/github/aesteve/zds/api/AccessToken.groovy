package com.github.aesteve.zds.api

import groovy.time.TimeCategory

class AccessToken {

	String access
	String refresh
	Date expires
	String scope
	String tokenType

	static fromJSON(Map map) {
		Date expirationDate
		use(TimeCategory) {
			expirationDate = new Date() + map.expires_in.milliseconds
		}
		new AccessToken(access: map.access_token, refresh: map.refresh_token, scope: map.scope, tokenType: map.token_type, expires: expirationDate)
	}

	boolean isExpired() {
		return new Date() > expires
	}
}
