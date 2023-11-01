package com.educa.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TopicResponseArray(
    @SerializedName("content")
    val topic: List<TopicResponse>?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TopicResponse)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(topic)
    }

    override fun describeContents(): Int {
        return 0
    }

    operator fun get(position: Int) {
        topic?.get(position)
    }

    companion object CREATOR : Parcelable.Creator<TopicResponseArray> {
        override fun createFromParcel(parcel: Parcel): TopicResponseArray {
            return TopicResponseArray(parcel)
        }

        override fun newArray(size: Int): Array<TopicResponseArray?> {
            return arrayOfNulls(size)
        }
    }
}

data class TopicResponse(
    val idTopico: Int,
    var titulo: String?,
    var descricao: String?,
    val dataCriacao: String?,
    val usuario: User?,
    var respostas: List<AnswerResponse>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.createTypedArrayList(AnswerResponse)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idTopico)
        parcel.writeString(titulo)
        parcel.writeString(descricao)
        parcel.writeString(dataCriacao)
        parcel.writeParcelable(usuario, flags)
        parcel.writeTypedList(respostas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopicResponse> {
        override fun createFromParcel(parcel: Parcel): TopicResponse {
            return TopicResponse(parcel)
        }

        override fun newArray(size: Int): Array<TopicResponse?> {
            return arrayOfNulls(size)
        }
    }
}