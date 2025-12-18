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

    val history: DatabaseReference = Db.reference.child("sensor").child("history")

    private var historyListener: ValueEventListener? = null
    private var sensorListener: ValueEventListener? = null
    private var settingListener: ValueEventListener? = null

    fun read_db(user: String, pass: String,onClick: (Dbresult<String>) -> Unit){
        val query = login.orderByChild("username").equalTo(user)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(User::class.java)
                        if(userData?.password == pass){
                            onClick(Dbresult.Success("Ok"))
                            UserManager.currentUsername = userData.username
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

        val listener = sensorListener
        if (listener != null) {
            sensor.removeEventListener(listener)
        }
        sensorListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.child("temperature").value.toString().toFloatOrNull() ?: 0f
                val hum = snapshot.child("humidity").value.toString().toFloatOrNull() ?: 0f
                val connect  = snapshot.child("connect").getValue(Boolean::class.java)?: false
                val data = SensorData(
                    humidity = hum,
                    temperature = temp,
                    isconnect = connect
                )
                if (data.isValid()) onClick(Dbresult.Success(data))
                else onClick(Dbresult.Error("Giá trị không hợp lệ"))
            }
            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error("Không lấy được giá trị"))
            }
        }
        sensor.addValueEventListener(sensorListener!!)
    }


    fun setting_sensor(onClick: (Dbresult<Setting>) -> Unit){
        val listener = settingListener
        if (listener != null) {
            setting.removeEventListener(listener)
        }
        settingListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val thresholdHum = snapshot.child("thresholdHum").getValue(Float::class.java)?.toFloat()?: 0f
                val thresholdTemp = snapshot.child("thresholdTemp").getValue(Float::class.java)?.toFloat()?: 0f
                val data = Setting(
                    thresholdHum = thresholdHum,
                    thresholdTemp = thresholdTemp,
                )
                if (data.isValid()) onClick(Dbresult.Success(data))
                else onClick(Dbresult.Error("Giá trị không hợp lệ"))
            }
            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error("Không lấy được thông tin"))
            }
        }
        setting.addValueEventListener(settingListener!!)
    }

    fun write_db_setting(hum: Float,temp: Float,onClick: (Dbresult<String>) -> Unit){
        val data = Setting(
            thresholdTemp = temp,
            thresholdHum = hum
        )
        setting.setValue(data).addOnSuccessListener {
            onClick(Dbresult.Success("ghi thành công"))
        }.addOnFailureListener {
            onClick(Dbresult.Error("ghi thất bại"))
        }
    }

    fun changePass( pass: changePass, onClick: (Dbresult<String>) -> Unit){
        val user = UserManager.currentUsername

        if (user == null) {
            onClick(Dbresult.Error("Lỗi: Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại."))
            return
        }

        if(pass.newPass == pass.continuePass){
            val query = login.orderByChild("username").equalTo(user)
            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val userData = userSnapshot.getValue(User::class.java)
                            if(userData?.password == pass.currentPass){
                                userSnapshot.ref.child("password").setValue(pass.newPass)
                                    .addOnSuccessListener { onClick(Dbresult.Success("Đổi Mật khẩu thành công")) }
                                    .addOnFailureListener { onClick(Dbresult.Error("Không ghi được mật khẩu")) }
                            }else{
                                onClick(Dbresult.Error("Mật khẩu không khớp"))
                            }
                        }
                    }
                    onClick(Dbresult.Error("Không phải mật khẩu hiện tại"))
                }

                override fun onCancelled(error: DatabaseError) {
                    onClick(Dbresult.Error(error.message))
                }
            })
        }else{
            onClick(Dbresult.Error("Mật khẩu chưa trùng khớp"))
        }
    }

    fun read_history(
        limit: Int = 50,
        onClick: (Dbresult<HistoryData>) -> Unit
    ) {
        val listener = historyListener
        if (listener != null) {
            history.removeEventListener(listener)
        }

        historyListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val humidityList = mutableListOf<HistoryPoint>()
                    val temperatureList = mutableListOf<HistoryPoint>()

                    // Đọc humidity history
                    val humiditySnapshot = snapshot.child("humidity")
                    for (child in humiditySnapshot.children) {
                        val value = child.child("val").getValue(Float::class.java) ?: 0f
                        val timestamp = child.child("ts").getValue(Long::class.java) ?: 0L
                        humidityList.add(HistoryPoint(value, timestamp))
                    }

                    // Đọc temperature history
                    val temperatureSnapshot = snapshot.child("temperature")
                    for (child in temperatureSnapshot.children) {
                        val value = child.child("val").getValue(Float::class.java) ?: 0f
                        val timestamp = child.child("ts").getValue(Long::class.java) ?: 0L
                        temperatureList.add(HistoryPoint(value, timestamp))
                    }

                    // Sắp xếp theo timestamp và lấy limit điểm mới nhất
                    val sortedHumidity = humidityList
                        .sortedByDescending { it.timestamp }
                        .take(limit)
                        .reversed()

                    val sortedTemperature = temperatureList
                        .sortedByDescending { it.timestamp }
                        .take(limit)
                        .reversed()

                    val historyData = HistoryData(
                        humidityHistory = sortedHumidity,
                        temperatureHistory = sortedTemperature
                    )

                    onClick(Dbresult.Success(historyData))

                } catch (e: Exception) {
                    onClick(Dbresult.Error("Lỗi đọc history: ${e.message}"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onClick(Dbresult.Error("Không lấy được history: ${error.message}"))
            }
        }

        history.addValueEventListener(historyListener!!)
    }

    fun removeSensorListener() {
        sensorListener?.let {
            sensor.removeEventListener(it)
            sensorListener = null
        }
    }

    fun removeHistoryListener() {
        historyListener?.let {
            history.removeEventListener(it)
            historyListener = null
        }
    }

    fun removeSettingListener() {
        settingListener?.let {
            setting.removeEventListener(it)
            settingListener = null
        }
    }
}