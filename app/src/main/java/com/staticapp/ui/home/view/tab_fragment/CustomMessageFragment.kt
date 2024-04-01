package com.staticapp.ui.home.view.tab_fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.staticapp.R
import com.staticapp.databinding.FragmentCustomMessageFinalBinding
import com.staticapp.roomDb.MessageData
import com.staticapp.ui.home.view.adapter.MessageAdapter
import com.staticapp.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CustomMessageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var ctx:Context
    var allowedCount=30
    lateinit var adapter:MessageAdapter
    lateinit var adapterForAll:MessageAdapter
     lateinit var msgList:ArrayList<MessageData>
     lateinit var msgAllList:ArrayList<MessageData>

     val homeViewModel:HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx=context
    }

    lateinit var fragmentCustomMessageBinding: FragmentCustomMessageFinalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCustomMessageBinding= FragmentCustomMessageFinalBinding.inflate(layoutInflater)
        characterCounter()
        setupMsgData()
        getMessageData()
        clickEvent()
        attachObserver()
        return fragmentCustomMessageBinding.root
    }

    private fun getMessageData() {
        homeViewModel.getAllMessage()
    }

    private fun attachObserver() {
  lifecycleScope.launch {

      homeViewModel._statusGetMessageFlow.collect{
//          when(it){
//          is DbResponseCallback.Loading-> {
//
//          }
//
//              is DbResponseCallback.Failed -> {
//
//              }
//              is DbResponseCallback.Success -> {
                   Log.d("222","~~getMesg~~"+ Gson().toJson(it))
                   Log.d("222","~~getMesg~7~"+ it?.size)
                  updateList(it)
//              }
//
//              else -> {}
//          }

      }



      }

  }

    private fun updateList(updatedList: List<MessageData>?) {
        Log.d("222","~~updatedList~~"+ updatedList?.size)

        msgList.clear()
        msgAllList.clear()
        updatedList?.let {

            msgAllList.addAll(it.reversed())
            adapterForAll.notifyDataSetChanged()

            updateRecentList(it.reversed())
            fragmentCustomMessageBinding.tvAllMsg.setText("All ("+msgAllList.size+")")
        }
        Log.d("222","~~updatedList~~"+ msgList?.size)


    }

    private fun updateRecentList(messageData: List<MessageData>) {
       for(i in 0 until messageData.size) {
           if (msgList.size < 5) {

               msgList.add(messageData.get(i))
           }
       }

        adapter.notifyDataSetChanged()
    }


    private fun clickEvent() {
        fragmentCustomMessageBinding.tvAddCustomMessage.setOnClickListener {
            if(!fragmentCustomMessageBinding.etAddNewMsg.text.toString().isBlank()){
                callInsertApi()
            }else{
                Toast.makeText(ctx,"Please enter message",Toast.LENGTH_SHORT).show()
            }
        }
    }
    var uid=5

    private fun callInsertApi() {

        var msgData=MessageData(uid=0,message = fragmentCustomMessageBinding.etAddNewMsg.text.toString(), timestamp = System.currentTimeMillis().toString()/*, isSelected = false*/)
        homeViewModel.insertMessage(msgData)

        fragmentCustomMessageBinding.etAddNewMsg.setText("")
//        uid=6
    }

    private fun setupMsgData() {
        msgList=ArrayList<MessageData>()
        msgAllList=ArrayList<MessageData>()


        adapter= MessageAdapter(msgList,{ setMsgSelection(it) },true)

        adapterForAll= MessageAdapter(msgAllList, { setMsgSelection(it) }, false)

        fragmentCustomMessageBinding.rvRecentMsg.layoutManager=LinearLayoutManager(ctx)
        fragmentCustomMessageBinding.rvRecentMsg.adapter=adapter
        fragmentCustomMessageBinding.rvRecentMsg.isNestedScrollingEnabled=false

        fragmentCustomMessageBinding.rvAllMsg.layoutManager=LinearLayoutManager(ctx)
        fragmentCustomMessageBinding.rvAllMsg.adapter=adapterForAll
        fragmentCustomMessageBinding.rvAllMsg.isNestedScrollingEnabled=false

        fragmentCustomMessageBinding.tvAllMsg.setText("All ("+msgAllList.size+")")


    }

    private fun setMsgSelection(pos: Int) {

        Log.d("222","~~setMsgSelection~~~"+pos)

    }

    private fun characterCounter() {

        fragmentCustomMessageBinding.etAddNewMsg.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setCounterAndButtonDisable()
            }


        })

    }


    private fun setCounterAndButtonDisable() {

   if(fragmentCustomMessageBinding.etAddNewMsg.text.toString().length > 0){

       var msgLength=  fragmentCustomMessageBinding.etAddNewMsg.text.toString().length
//       var finalCount=allowedCount - msgLength
       fragmentCustomMessageBinding.tvCount.setText(""+msgLength+"/"+allowedCount);
       fragmentCustomMessageBinding.tvAddCustomMessage.setBackgroundTintList(
           ContextCompat.getColorStateList(ctx, R.color.white));
       fragmentCustomMessageBinding.tvAddCustomMessage.alpha=1f
       fragmentCustomMessageBinding.tvAddCustomMessage.setTextColor(ContextCompat.getColor(ctx,R.color.tab_border))
   }else{
       fragmentCustomMessageBinding.tvCount.setText("0/"+allowedCount);
       fragmentCustomMessageBinding.tvAddCustomMessage.setBackgroundTintList(
           ContextCompat.getColorStateList(ctx, R.color.blue_custom_));
       fragmentCustomMessageBinding.tvAddCustomMessage.setTextColor(ContextCompat.getColor(ctx,R.color.white))
       fragmentCustomMessageBinding.tvAddCustomMessage.alpha=0.3f
   }

    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomMessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}