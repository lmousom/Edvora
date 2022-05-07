package com.mousom.edvora.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mousom.edvora.R
import com.mousom.edvora.data.adapter.RidesAdapter
import com.mousom.edvora.data.model.RideDataItem
import com.mousom.edvora.data.model.UserData
import com.mousom.edvora.data.repository.BaseRepository
import com.mousom.edvora.utils.ConnectionCheckUtil
import com.mousom.edvora.utils.FindNearestRides.findClosestRide
import com.mousom.edvora.viewmodel.NearestRideFactory
import com.mousom.edvora.viewmodel.NearestRideViewModel

class NearestRide : Fragment() {

    companion object {
        fun nearestRideInstance() = NearestRide()

    }

    private lateinit var connectionCheckUtil: ConnectionCheckUtil
    private var isActiveConnection: Boolean = false
    private val rideDataList: MutableList<RideDataItem> = mutableListOf()
    private val userDataList: MutableList<UserData> = mutableListOf()
    private lateinit var viewModel: NearestRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.nearest_ride_fragment, container, false)
        val recyclerview = root.findViewById<RecyclerView>(R.id.ride_recycler_view)

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

        //Error Handling
        viewModel.operationErrorLiveDate.observe(viewLifecycleOwner) { error ->

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(error.messageTitle)
                .setMessage(error.message)
                .setPositiveButton(resources.getString(R.string.retry)) { dialog, which ->

                }
                .show()
        }

        viewModel.getRidesResponse.observe(requireActivity()) { response ->
            if (response.isSuccessful && isActiveConnection) {
                if (userDataList.isNotEmpty()) {
                    response.body()?.forEach { ride ->
                        val closestRide = findClosestRide(
                            ride.station_path,
                            userDataList[0].station_code,
                            ride.id
                        )
                        closestRide.forEach {
                            if (it.id == ride.id) {
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

                rideDataList.sortBy { it.closestStation }
                val adapter = RidesAdapter(rideDataList)
                recyclerview.adapter = adapter

                adapter.setOnItemClickListener(object : RidesAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                    }

                })
            }
        }

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        return root
    }


    override fun onAttach(context: Context) {
        val repository = BaseRepository()
        val viewModelFactory = NearestRideFactory(repository)

        activity?.lifecycleScope?.launchWhenCreated {
            viewModel = ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[NearestRideViewModel::class.java]
        }

        super.onAttach(context)
    }


}