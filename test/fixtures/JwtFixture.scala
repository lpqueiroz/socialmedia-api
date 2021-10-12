package fixtures

import play.api.http.HeaderNames
import play.api.mvc.AnyContentAsEmpty
import play.api.test.{FakeHeaders, FakeRequest}

object JwtFixture {



}

object MockJwtRequest {
  def withToken(token: Option[String]): FakeRequest[AnyContentAsEmpty.type] = token.fold(apply())(t => apply(HeaderNames.AUTHORIZATION -> s"Bearer $t"))

  def apply(headers: (String, String)*): FakeRequest[AnyContentAsEmpty.type] = FakeRequest.apply(method = "GET", uri = "/", headers = FakeHeaders(Seq(HeaderNames.HOST -> "localhost") ++ headers), body = AnyContentAsEmpty)
}
