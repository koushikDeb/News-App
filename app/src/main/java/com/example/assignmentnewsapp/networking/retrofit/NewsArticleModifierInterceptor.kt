package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.utils.Constants.Companion.newsID
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.nio.charset.Charset

class NewsArticleModifierInterceptor : Interceptor {

  override fun intercept(chain: Chain): Response {
    val response = chain.proceed(chain.request())
    val rawJson = response.body
if(response.code==200) {
  var source = response.body?.source()
  source?.request(Long.MAX_VALUE);
  var buffer = source?.buffer()

  val newJSON = buffer?.clone()?.let { addIdtoResponse(it?.readString(Charset.forName("UTF-8"))) }

  Timber.i("=============== Modified RESPONSE===============${newJSON}")
  return response.newBuilder().body(newJSON?.toResponseBody(rawJson?.contentType())).build()
}
return response
  }

  private fun addIdtoResponse(rawRes: String): String {

    val res: com.example.assignmentnewsapp.networking.model.Response =
      Gson().fromJson(rawRes, com.example.assignmentnewsapp.networking.model.Response::class.java)
    for (i in 0 until (res?.articles?.size)) {
      res.articles[i].id = newsID + i;
    }
    return Gson().toJson(res)
  }
  }
