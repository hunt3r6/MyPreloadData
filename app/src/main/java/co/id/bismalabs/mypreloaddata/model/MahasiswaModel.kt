package co.id.bismalabs.mypreloaddata.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MahasiswaModel(
    var id: Int = 1,
    var name: String? = null,
    var nim: String? = null
) : Parcelable