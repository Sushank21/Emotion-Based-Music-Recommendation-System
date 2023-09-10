package com.example.myspotify.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Log the raw response body
        val responseBody = response.body?.string()
        if (responseBody != null) {
            Log.i("RawResponseBody", responseBody)
        }

        // Re-create the response for further processing
        return response.newBuilder()
            .body(responseBody?.toResponseBody(response.body?.contentType()))
            .build()
    }
}
