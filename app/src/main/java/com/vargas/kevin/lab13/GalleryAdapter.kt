import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vargas.kevin.lab13.GalleryActivity
import com.vargas.kevin.lab13.R

class GalleryAdapter(private val images: GalleryActivity) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.local_img) // Asegúrate de que el ID coincida con el del XML
    }

    // Crear la vista para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_img, parent, false)
        return GalleryViewHolder(view)
    }

    // Asignar los valores a cada imagen
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUrl = images[position]

        // Usar Glide para cargar la imagen desde la URL en el ImageView
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Aquí va la URL de la imagen o recurso
            .into(holder.imageView)
    }

    // Retornar el número de elementos en la lista
    override fun getItemCount(): Int {
        return images.size
    }
}
