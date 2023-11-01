package com.educa.api.service

import com.educa.api.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface MainApiService {

    @GET("api/conteudos")
    fun getAllContent() : Call<ContentResponseArray>

    @POST("api/usuarios/estudantes/")
    fun registerStudent(@Body student: Student) : Call<Student>

    @GET("api/topicos/usuario-secao")
    fun getMyTopics() : Call<TopicResponseArray>

    @GET("api/topicos")
    fun getAllTopics() : Call<TopicResponseArray>

    @POST("api/topicos")
    fun registerTopic(@Body topic: Topic) : Call<Topic>

    @PUT("api/topicos/{id}")
    fun updateTopic(@Path("id") topicId: Int, @Body topic: Topic): Call<Topic>

    @DELETE("api/topicos/{id}")
    fun deleteTopic(@Path("id") topicId: Int) : Call<Topic>

    @POST("api/topicos/respostas")
    fun registerAnswer(@Body answer: Answer) : Call<Answer>

    @PUT("api/topicos/respostas/{id}")
    fun updateAnswer(@Path("id") topicId: Int, @Body answer: Answer) : Call<Answer>

    @DELETE("api/topicos/respostas/{id}")
    fun deleteAnswer(@Path("id") answerId: Int) : Call<Answer>

    @POST("api/conteudos/avaliacoes")
    fun registerRating(@Body rating: Rating) : Call<Rating>
}