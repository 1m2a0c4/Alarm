package com.example.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView as TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ボタンを定義
        val buttonR = findViewById<Button>(R.id.R) as Button
        val buttonL = findViewById<Button>(R.id.L) as Button
        val buttonU = findViewById<Button>(R.id.U) as Button
        val buttonD = findViewById<Button>(R.id.D) as Button
        val buttonE = findViewById<Button>(R.id.E) as Button

        var TextView = findViewById<TextView>(R.id.TV) as TextView

        //必要な変数を定義
        var flag = "null"

        var ctl = "null"

        /*ボタンが押された時の処理
            --ここから--*/

        //右が押されたら
        buttonR.setOnClickListener(){
            flag = "right"
        }

        //左が押されたら
        buttonL.setOnClickListener(){
            flag = "left"
        }

        buttonD.setOnClickListener() {
            flag = "down"
        }

        buttonU.setOnClickListener(){
            flag = "up"
        }

        buttonE.setOnClickListener(){
            flag = "end"
        }

        //押された結果により処理分岐
       when(flag){
           "right" ->{
                TV.text = "→"
           }
           "left" ->{
               TV.text = "←"
           }
           "up" ->{
               TV.text = "↑"
           }
           "down" ->{
               TV.text = "↓"
           }
           "end" ->{
               TV.text = "おしまい"
           }
           "else" ->{

           }
       }
    }


}
