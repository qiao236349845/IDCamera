package com.gamerole.orcamera

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_car.*

class CarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)


//        var carRuleView = CarRuleView(this)
//        val parames = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100)
////        parames.height = 100
////        parames.width = ViewGroup.LayoutParams.MATCH_PARENT
//        carRuleView.layoutParams = parames
//        horizon.addView(carRuleView)

//        horizon.setRange(0,90)

//        List<String> list = new ArrayList();
        val list = ArrayList<String>()

        list.add("2020-02-01")
        list.add("2020-04-01")
        list.add("2020-05-01")
        list.add("2020-06-01")
//        list.add("2018-10-01")
        carview.setRecoverListDays(list)
    }

}
