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
import com.mousom.edvora.viewmodel.PastRideFactory
import com.mousom.edvora.viewmodel.PastRideViewModel
import java.time.LocalDate

class PastRide : Fragment() {

    private lateinit var connectionCheckUtil: ConnectionCheckUtil
    private var isActiveConnection: Boolean = false
    private val rideDataList: MutableList<RideDataItem> = mutableListOf()
    private val userDataList: MutableList<UserData> = mutableListOf()

    companion object {
        fun newInstance() = PastRide()
    }

    private lateinit var viewModel: PastRideViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.past_ride_fragment, container, false)
        val pastRideRecyclerview = root.findViewById<RecyclerView>(R.id.past_rides_recyclerview)

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
                    userDataList.add(
                        it
                    )
                }
            }
        }


        viewModel.getRidesResponse.observe(requireActivity()) { response ->
            val date = LocalDate.now()
            if (response.isSuccessful && isActiveConnection) {
                if (userDataList.isNotEmpty()) {
                    response.body()?.forEach { ride ->
                        if (date.isAfter(stringToDate(ride.date))
                        ) {
                            val closestRide = FindNearestRides.findClosestRide(
                                ride.station_path,
                                userDataList[0].station_code,
                                ride.id
                            )
                            closestRide.forEach {
                                rideDataList.add(
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
                                        kotlin.math.abs(userDataList[0].station_code - it.closestStation)

                                    )
                                )
                            }
                        }
                    }
                }


                val pastAdapter = RidesAdapter(rideDataList)
                pastRideRecyclerview.adapter = pastAdapter

                pastAdapter.setOnItemClickListener(object : RidesAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                    }

                })
            }
        }

        pastRideRecyclerview.layoutManager = LinearLayoutManager(requireContext())


        return root
    }

    override fun onAttach(context: Context) {
        val repository = BaseRepository()
        val viewModelFactory = PastRideFactory(repository)

        activity?.lifecycleScope?.launchWhenCreated {
            viewModel = ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[PastRideViewModel::class.java]
        }

        super.onAttach(context)
    }

}