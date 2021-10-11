package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.data.model.allnews.Response
import com.example.assignmentnewsapp.utils.Constants.Companion.TOPIC_EVERYTHING
import com.example.assignmentnewsapp.utils.Constants.Companion.TOPIC_TOP_HEADLINES
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.nio.charset.Charset

class NewsArticleModifierInterceptor() : Interceptor {

  override fun intercept(chain: Chain): okhttp3.Response {
    val request = chain.request()
    val response = chain.proceed(request)
    Timber.i("${request.url.encodedPath}")
    val rawJson = response.body
    if (response.code == 200) {
      var source = response.body?.source()
      source?.request(Long.MAX_VALUE);
      var buffer = source?.buffer()
      val newJSON = buffer?.clone()
        ?.let { addIdtoResponse(it?.readString(Charset.forName("UTF-8")), getTopic(request.url)) }
      Timber.i("=============== Modified RESPONSE===============${newJSON}")
      return response.newBuilder().body(newJSON?.toResponseBody(rawJson?.contentType())).build()
    }
    return response
  }

  fun getTopic(url: HttpUrl): String {
    return if (url.encodedPath.equals(TOPIC_EVERYTHING))
      TOPIC_EVERYTHING
    else
      TOPIC_TOP_HEADLINES + url.queryParameterValues("country")
  }

  private fun addIdtoResponse(
    rawRes: String?,
    topic: String?
  ): String {

    val res: Response = Gson().fromJson(rawRes, Response::class.java)

    for (i in 0 until (res?.articles?.size)) {
      res.articles[i].topic = topic
    }
    return Gson().toJson(res)
  }
}
