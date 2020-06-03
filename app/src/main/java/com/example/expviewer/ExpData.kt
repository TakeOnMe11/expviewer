package com.example.expviewer

import android.accounts.AuthenticatorDescription
import com.google.gson.annotations.SerializedName
import java.util.*

data class ExpData (
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("measdate") var measDate: String = "",
    @SerializedName("comment") var comment: String = "",
    @SerializedName("data") var data: String = "",
    @SerializedName("setid__name") var setName: String = "",
    @SerializedName("setid__description") var setDescription: String = "",
    @SerializedName("typeid__description") var typeName: String = ""
)