package com.t3h.doantotngiep2021.camera

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.t3h.doantotngiep2021.R
import com.t3h.doantotngiep2021.data.SellerActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_seller: Button = findViewById(R.id.btn_seller)
        val btn_customer: Button = findViewById(R.id.btn_customer)
        btn_customer.setOnClickListener(this)
        btn_seller.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_seller -> {
                val intent1 = Intent()
                intent1.setClass(this, SellerActivity::class.java)
                startActivity(intent1)
            }
        }
        when (v!!.id) {
            R.id.btn_customer -> {
                val intent2 = Intent()
                intent2.setClass(this, DemoCameraActivity::class.java)
                startActivity(intent2)
            }
        }

    }


}