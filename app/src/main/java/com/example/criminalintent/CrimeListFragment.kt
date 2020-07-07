package com.example.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private var callbacks: Callbacks? = null
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    companion object {
        fun newInstance() : CrimeListFragment {
            return CrimeListFragment()
        }
    }

    /**
     * Required interface for activities that would host the fragment
     */
    interface Callbacks {
        fun onCrimeSelected(crimeID: UUID)
    }

    /**
        Since CrimeListFragment is hosted in an activity, the
        Context object passed to onAttach(…) is the activity
        instance hosting the fragment.
        We cast the hosting activity to a callback
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimesLiveData.observe(
            viewLifecycleOwner,
            Observer {crimes ->
                crimes.let {
                    Log.i("alele", crimes.size.toString())
                    updateIU(crimes)
                }
            }
        )
    }

    private fun updateIU(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
//        adapter.submitList(crimes)
        crimeRecyclerView.adapter!!.notifyItemMoved(0, 5)
    }

    /**
     * This is the custom layout used to display the crimes
     */
    private inner class CrimeAdapter(var crimeList: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimeList[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimeList.size
        }

//        private inner class DiffCallback : DiffUtil.ItemCallback<Crime>() {
//            override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
//                return oldItem == newItem
//            }
//        }
    }

     inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime
        private val titleTextView: TextView = view.findViewById(R.id.crime_title)
        private val dateTextView: TextView = view.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = view.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility =  if (crime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crime.id)
            Toast.makeText(context, crime.title, Toast.LENGTH_SHORT).show()
        }
    }
}
