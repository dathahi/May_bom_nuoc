package com.example.demo.data

import androidx.compose.animation.core.snap
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.database


class firebase {
    val DbUrl: String  = "https://fir-ad53c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    val  Db = Firebase.database(DbUrl)
    val login: DatabaseReference = Db.reference.child("login")
    val sensor: DatabaseReference = Db.reference.child("sensor")

    val setting: DatabaseReference = Db.reference.child("setting")


    fun read_db(user: String, pass: String,onClick: (Dbresult<String>) -> Unit){
        val query = login.orderByChild("username").equalTo(user)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(User::class.java)
                        if(userData?.password == pass){
                            onClick(Dbresult.Success("Ok"))
                            return
                        }
                    }
                }
                onClick(Dbresult.Error("Sai tên đăng nhập hoặc mật khẩu"))
            }

            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error(error.message))
            }
        })
    }
    fun reg_db(user: String, pass : String, onClick:(Dbresult<String>) -> Unit){
        val query = login.orderByChild("username").equalTo(user)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    onClick(Dbresult.Error("trùng tên đăng nhập"))
                }else{
                    try {
                        val newRef = login.push()
                        val uid = newRef.key
                        if(uid != null){
                            val newUser= User(user,pass)
                            newRef.setValue(newUser)
                                .addOnSuccessListener {
                                    onClick(Dbresult.Success("Đăng ký thành công"))

                                }.addOnFailureListener { onClick(Dbresult.Error("Lỗi ghi dữ liệu: ${it.message}")) }
                        }else{
                            onClick(Dbresult.Error("KHông tạo được ID"))
                        }
                    }catch (e: Exception){
                        onClick(Dbresult.Error(e.message ?: "Lỗi không xác định"))
                    }
                }
                onClick(Dbresult.Error("Sai tên đăng nhập hoặc mật khẩu"))
            }

            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error(error.message))
            }
        })
    }

    fun info_sensor(onClick: (Dbresult<SensorData>) -> Unit){
        sensor.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.child("temperature").getValue(Double::class.java)?.toFloat() ?: 0f
                val hum = snapshot.child("humidity").getValue(Double::class.java)?.toFloat() ?: 0f
                val ts = snapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                val connect  = snapshot.child("connect").getValue(Boolean::class.java)?: false
                val data = SensorData(
                    humidity = hum,
                    temperature = temp,
                    timestamp = ts,
                    isconnect = connect
                )

                if (data.isValid()) onClick(Dbresult.Success(data))
                else onClick(Dbresult.Error("Giá trị không hợp lệ"))
            }
            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error("Không lấy được giá trị"))
            }
        })
    }


//    fun setting_sensor(Temp: Float,Hum: Float,onClick: (Dbresult<String>) -> Unit){
//        setting.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val settingData = snapshot.
//            }
//        })
//    }
}