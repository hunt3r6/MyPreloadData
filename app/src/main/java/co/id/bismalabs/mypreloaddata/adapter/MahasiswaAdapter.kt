package co.id.bismalabs.mypreloaddata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.id.bismalabs.mypreloaddata.R
import co.id.bismalabs.mypreloaddata.model.MahasiswaModel
import kotlinx.android.synthetic.main.item_mahasiswa_row.view.*

class MahasiswaAdapter : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {
    private val listMahasiswa = ArrayList<MahasiswaModel>()

    fun setData(listMahasiswa: ArrayList<MahasiswaModel>) {
        if (listMahasiswa.size > 0) {
            this.listMahasiswa.clear()
        }

        this.listMahasiswa.addAll(listMahasiswa)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa_row, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        holder.bind(listMahasiswa[position])
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = listMahasiswa.size

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswa: MahasiswaModel) {
            with(itemView) {
                txt_name.text = mahasiswa.name
                txt_nim.text = mahasiswa.nim
            }
        }
    }

}