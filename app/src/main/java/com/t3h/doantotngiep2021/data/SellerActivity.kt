package com.t3h.doantotngiep2021.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.t3h.doantotngiep2021.R
import kotlinx.android.synthetic.main.activity_seller.*

class SellerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)

        val context = this
        var db = DataBaseHandler(context)
        btn_insert.setOnClickListener({
            if (edtName.text.toString().length > 0 &&
                edtAge.text.toString().length > 0
//                &&
//                edtPhone.text.toString().length > 0
            ) {
                var user = UserInformation(
                    edtName.text.toString(),
                    edtAge.text.toString().toInt()
//                    ,
//                    edtPhone.text.toString().toInt()
                )
                db.insertData(user)
            } else {
                Toast.makeText(context, "Please Fill All Data", Toast.LENGTH_SHORT).show()
            }
        })

        btn_read.setOnClickListener({
            var data = db.readData()
            tvResult.text = ""
            for (i in 0..(data.size - 1)) {
                tvResult.append(
                    data.get(i).id.toString() + "---" +
                            data.get(i).name + "---" +
                            data.get(i).age + "---" +
//                            data.get(i).phone + " " +
                            "\n"
                )
            }
        })

        btn_update.setOnClickListener({
            db.updateData()
            btn_read.performClick()
        })

        btn_delete.setOnClickListener({
            db.deleteData()
            btn_read.performClick()
        })
    }
}