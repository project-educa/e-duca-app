package com.educa.api.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val idUsuario: Int,
    val nome: String?,
    val sobrenome: String?,
    val dataNasc: String?,
    val email: String?,
    val senha: String?,
    val areaAtuacao: String?,
    val inicioAtuacao: String?,
    val perfis: List<Profile>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Profile)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idUsuario)
        parcel.writeString(nome)
        parcel.writeString(sobrenome)
        parcel.writeString(dataNasc)
        parcel.writeString(email)
        parcel.writeString(senha)
        parcel.writeString(areaAtuacao)
        parcel.writeString(inicioAtuacao)
        parcel.writeTypedList(perfis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}