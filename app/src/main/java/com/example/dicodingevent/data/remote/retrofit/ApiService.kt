package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.ResponseDetail
import com.example.dicodingevent.data.remote.response.ResponseList
import retrofit2.http.*

interface ApiService {
    @GET("/events")
    suspend fun getFinished(@Query("active") active: Int = 0): ResponseList

    @GET("/events")
    suspend fun getUpcoming(@Query("active") active: Int = 1): ResponseList

    @GET("/events")
    suspend fun getUpcoming5(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 5
    ): ResponseList

    @GET("/events")
    suspend fun getFinished5(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 5
    ): ResponseList

    @GET("/events/{id}")
    suspend fun getDetailEvent(@Path("id") id: Int): ResponseDetail

    @GET("/events")
    suspend fun searchEvents(@Query("q") query: String): ResponseList
}