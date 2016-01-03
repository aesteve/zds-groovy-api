package specs

import com.github.aesteve.zds.api.ZdsApiClient
import spock.lang.Specification

class ZdsClientSpec extends Specification {

	protected static ZdsApiClient client
	protected static String username = 'Javier'

	def setup() {
		client = new ZdsApiClient('sMf1vgBpsWnv8GygBSGkqtjwp4h4t2SsLB11wzA4', System.getenv('ZDS_CLIENT_SECRET'))
		client.login username, System.getenv('ZDS_PASSWORD')
	}

}
