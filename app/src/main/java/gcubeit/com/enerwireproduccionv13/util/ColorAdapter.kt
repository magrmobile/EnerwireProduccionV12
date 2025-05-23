package gcubeit.com.enerwireproduccionv13.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import gcubeit.com.enerwireproduccionv13.R
import gcubeit.com.enerwireproduccionv13.data.network.response.color.Color

class ColorAdapter(context: Context?, colorList: ArrayList<Color>) :
    ArrayAdapter<Color?>(context!!, 0, colorList as List<Color?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cView: View? = convertView
        if (cView == null) {
            cView = LayoutInflater.from(context).inflate(
                R.layout.spinner_item_color, parent, false
            )
        }
        val imageViewColor: ImageView = cView!!.findViewById(R.id.image_view_color)
        val textViewColorName = cView.findViewById<TextView>(R.id.text_view_color_name)
        val currentItem = getItem(position)

        if(currentItem != null) {
            if(currentItem.id == -1) {
                textViewColorName.text = currentItem.name
                textViewColorName.setPadding(16, 0, 0, 0)
                imageViewColor.visibility = View.GONE
            }else {
                imageViewColor.setColorFilter(android.graphics.Color.parseColor(currentItem.hexCode))
                textViewColorName.text = currentItem.name
                textViewColorName.setTextColor(android.graphics.Color.parseColor(currentItem.hexCode))
            }
        }

        return cView
    }
}