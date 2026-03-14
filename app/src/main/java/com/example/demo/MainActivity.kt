package com.example.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * BroadcastReceiver 示例程序
 *
 * 展示如何在 Android 中使用 BroadcastReceiver 接收系统广播
 * BroadcastReceiver 用于接收来自系统或其他应用发送的广播消息
 */
class MainActivity : AppCompatActivity() {

    // 自定义广播动作
    companion object {
        const val CUSTOM_ACTION = "com.example.demo.CUSTOM_BROADCAST"
    }

    // 用于显示接收到的广播信息
    private lateinit var statusText: TextView
    private lateinit var sendButton: Button

    // 自定义广播接收器
    private val customReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                CUSTOM_ACTION -> {
                    val message = intent.getStringExtra("message") ?: "无消息"
                    statusText.text = "收到自定义广播: $message"
                }
                Intent.ACTION_BATTERY_CHANGED -> {
                    val level = intent.getIntExtra("level", -1)
                    val scale = intent.getIntExtra("scale", -1)
                    val batteryPct = if (level >= 0 && scale > 0) {
                        (level * 100 / scale)
                    } else {
                        -1
                    }
                    statusText.text = "电池电量: $batteryPct%"
                }
                Intent.ACTION_BATTERY_LOW -> {
                    statusText.text = "电池电量低！"
                }
                Intent.ACTION_BATTERY_OKAY -> {
                    statusText.text = "电池电量正常"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建根布局 - 使用 ConstraintLayout
        val rootLayout = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
        }

        // 创建标题
        val titleText = TextView(this).apply {
            text = "BroadcastReceiver 示例"
            textSize = 24f
        }

        // 创建状态显示文本
        statusText = TextView(this).apply {
            text = "等待广播..."
            textSize = 16f
        }

        // 创建发送自定义广播的按钮
        sendButton = Button(this).apply {
            text = "发送自定义广播"
            setOnClickListener {
                // 发送自定义广播
                val intent = Intent(CUSTOM_ACTION).apply {
                    putExtra("message", "你好，广播接收器！")
                }
                sendBroadcast(intent)
            }
        }

        // 创建按钮用于测试电池广播
        val batteryButton = Button(this).apply {
            text = "请求电池状态"
            setOnClickListener {
                // 发送请求电池状态的广播
                val intent = Intent(Intent.ACTION_BATTERY_CHANGED)
                sendBroadcast(intent)
            }
        }

        // 添加视图到根布局
        rootLayout.addView(titleText)
        rootLayout.addView(statusText)
        rootLayout.addView(sendButton)
        rootLayout.addView(batteryButton)

        // 设置约束布局参数
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        titleText.layoutParams = params.apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        }

        statusText.layoutParams = params.apply {
            topToBottom = titleText.id
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topMargin = 24
        }

        sendButton.layoutParams = params.apply {
            topToBottom = statusText.id
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topMargin = 32
        }

        batteryButton.layoutParams = params.apply {
            topToBottom = sendButton.id
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topMargin = 16
        }

        // 设置内容视图
        setContentView(rootLayout)
    }

    override fun onStart() {
        super.onStart()
        // 注册广播接收器
        val intentFilter = IntentFilter().apply {
            // 添加自定义广播动作
            addAction(CUSTOM_ACTION)
            // 添加电池相关广播动作
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
        }
        registerReceiver(customReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        // 注销广播接收器
        unregisterReceiver(customReceiver)
    }
}
