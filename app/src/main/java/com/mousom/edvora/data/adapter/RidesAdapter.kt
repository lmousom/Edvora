package com.mousom.edvora.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mousom.edvora.R
import com.mousom.edvora.data.model.RideDataItem
import com.mousom.edvora.utils.ListToStringUtil.listToString
import com.squareup.picasso.Picasso

class RidesAdapter(
    private val ridesList: List<RideDataItem>
) :
    RecyclerView.Adapter<RidesAdapter.ViewHolder>() {
    private lateinit var mlistener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ride_list_view, parent, false)

        val holder = ViewHolder(view, mlistener)

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val city = ridesList[position].city
        val date = ridesList[position].date
        val destinationStationCode = ridesList[position].destination_station_code.toString()
        val id = ridesList[position].id.toString()
        val mapUrl = ridesList[position].map_url
        val originStationCode = ridesList[position].origin_station_code.toString()
        val state = ridesList[position].state
        val stationPathList = ridesList[position].station_path
        val distance = ridesList[position].distance.toString()


        holder.apply {
            cityName.text = city
            stateName.text = state
            rideId.text = "Ride Id: $id"
            originStation.text = "Origin Station: $originStationCode"
            stationPath.text = "Station Path: ${listToString(stationPathList)}"
            dateView.text = "Date: $date"
            distanceView.text = "Distance: $distance"
            Picasso.get().load(mapUrl).into(mapView)
        }


    }

    override fun getItemCount(): Int {
        return ridesList.size
    }

    class ViewHolder(ItemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(ItemView) {
        val cityName: Button = itemView.findViewById(R.id.filters)
        val stateName: Button = itemView.findViewById(R.id.state_name)
        val rideId: TextView = itemView.findViewById(R.id.ride_id_txt)
        val originStation: TextView = itemView.findViewById(R.id.origin_station_txt)
        val stationPath: TextView = itemView.findViewById(R.id.station_path_txt)
        val dateView: TextView = itemView.findViewById(R.id.date_txt)
        val distanceView: TextView = itemView.findViewById(R.id.distance_txt)
        val mapView: ImageView = itemView.findViewById(R.id.map_img)


        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


}
