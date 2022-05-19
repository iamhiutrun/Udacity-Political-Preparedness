package com.example.android.politicalpreparedness.election.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent,clickListener)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election)
    }
}

class ElectionViewHolder(val binding: ItemElectionBinding, private val clickListener: ElectionListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(election: Election){
        Log.d("TAG", "bind: $election")
        binding.election = election
        binding.listener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup,clickListener: ElectionListener): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemElectionBinding.inflate(layoutInflater)
            return ElectionViewHolder(binding, clickListener)
        }
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>(){
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

interface ElectionListener {
    fun onClick(election: Election)
}