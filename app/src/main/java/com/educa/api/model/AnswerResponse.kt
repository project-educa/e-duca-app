package com.educa.api.model

import android.os.Parcel
import android.os.Parcelable

data class AnswerResponse(
    val idResposta: Int,
    var resposta: String?,
    val dataCriacao: String?,
    val usuario: User?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(User::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idResposta)
        parcel.writeString(resposta)
        parcel.writeString(dataCriacao)
        parcel.writeParcelable(usuario, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnswerResponse> {
        override fun createFromParcel(parcel: Parcel): AnswerResponse {
            return AnswerResponse(parcel)
        }

        override fun newArray(size: Int): Array<AnswerResponse?> {
            return arrayOfNulls(size)
        }
    }
}
