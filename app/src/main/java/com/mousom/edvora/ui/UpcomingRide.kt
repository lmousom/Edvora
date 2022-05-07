package com.mousom.edvora.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mousom.edvora.R
import com.mousom.edvora.data.adapter.RidesAdapter
import com.mousom.edvora.data.model.RideDataItem
import com.mousom.edvora.data.model.UserData
import com.mousom.edvora.data.repository.BaseRepository
import com.mousom.edvora.utils.ConnectionCheckUtil
import com.mousom.edvora.utils.FindNearestRides
import com.mousom.edvora.utils.ParseDateUtil.stringToDate
import com.mousom.edvora.viewmodel.UpcomingRideFactory
import com.mousom.edvora.viewmodel.UpcomingRideViewModel
import java.time.LocalDate

class UpcomingRide : Fragment() {
    private lateinit var connectionCheckUtil: ConnectionCheckUtil
    private var isActiveConnection: Boolean = false
    private val upComingRideDataList: MutableList<RideDataItem> = mutableListOf()
    private val upComingUserDataList: MutableList<UserData> = mutableListOf()

    companion object {
        fun newInstance() = UpcomingRide()
    }

    private lateinit var viewModel: UpcomingRideViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.upcoming_ride_fragment, container, false)
        val upcomingRecyclerview = root.findViewById<RecyclerView>(R.id.upcoming_ride_recyclerview)

        connectionCheckUtil = ConnectionCheckUtil(application = requireActivity().application)

        connectionCheckUtil.observe(viewLifecycleOwner) { isActive ->
            isActiveConnection = isActive
            if (isActive) {
                viewModel.getUserResponse("/user")


            } else {
                Snackbar.make(root, "No Internet Connection 😪", Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        viewModel.getUserResponse.observe(requireActivity()) { response ->
            if (response.isSuccessful && isActiveConnection) {
                viewModel.getRidesResponse("/rides")
                response.body()?.let {
                    UserData(
                        it.name,
                        it.station_code,
                        it.url
                    )
                }?.let {
                    upComingUserDataList.add(
                        it
                    )
                }
            }
        }


        viewModel.getRidesResponse.observe(requireActivity()) { response ->
            val date = LocalDate.now()
            if (response.isSuccessful && isActiveConnection) {
                if (upComingUserDataList.isNotEmpty()) {
                    response.body()?.forEach { ride ->
                        val closestRide = FindNearestRides.findClosestRide(
                            ride.station_path,
                            upComingUserDataList[0].station_code,
                            ride.id
                        )
                        closestRide.forEach {
                            if (date.isBefore(stringToDate(ride.date))) {
                                upComingRideDataList.add(
                                    RideDataItem(
                                        ride.city,
                                        ride.date,
                                        ride.destination_station_code,
                                        ride.id,
                                        ride.map_url,
                                        ride.origin_station_code,
                                        ride.state,
                                        ride.station_path,
                                        it.closestStation,
                                        kotlin.math.abs(upComingUserDataList[0].station_code - it.closestStation)

                                    )
                                )
                            }

                        }

                    }
                }
                //Dummy Data Due to unavailability of Upcoming ride data
                upComingRideDataList.add(
                    RideDataItem(
                        "Kolkata",
                        "06/23/2022 06:20 PM",
                        34,
                        88,
                        "https://picsum.photos/200",
                        67,
                        "West Bengal",
                        listOf(23, 40, 34, 60),
                        39,
                        kotlin.math.abs(upComingUserDataList[0].station_code - 39)

                    )
                )

                val adapter = RidesAdapter(upComingRideDataList)
                upcomingRecyclerview.adapter = adapter

                adapter.setOnItemClickListener(object : RidesAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                    }

                })
            }
        }

        upcomingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        return root
    }

    override fun onAttach(context: Context) {
        val repository = BaseRepository()
        val viewModelFactory = UpcomingRideFactory(repository)
        activity?.lifecycleScope?.launchWhenCreated {
            viewModel = ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[UpcomingRideViewModel::class.java]
        }

        super.onAttach(context)
    }


}