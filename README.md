# android-broadcastreceiver-demo

## 简介

本 demo 展示了如何在 Android 应用中使用 BroadcastReceiver 接收系统广播和自定义广播。BroadcastReceiver 是 Android 的四大组件之一，用于接收来自系统或其他应用发送的广播消息。

## 基本原理

BroadcastReceiver（广播接收器）是一种组件，用于接收广播消息：

- **系统广播**：如电池电量变化、网络状态变化、开机完成等
- **自定义广播**：应用自定义的广播消息
- **注册方式**：
  - 静态注册：在 AndroidManifest.xml 中声明
  - 动态注册：在代码中注册和注销

## 启动和使用

### 环境要求
- Android Studio Arctic Fox 或更高版本
- JDK 11 或更高版本
- Android SDK 34

### 安装和运行
1. 使用 Android Studio 打开本项目
2. 连接 Android 设备或启动模拟器
3. 点击 Run 按钮运行应用

## 教程

### 什么是 BroadcastReceiver？

BroadcastReceiver 是 Android 四大组件之一，用于接收广播消息。广播是一种发布-订阅机制，发送方不需要知道接收方是谁，接收方也不需要知道发送方是谁。

### 系统广播示例

| 广播动作 | 说明 |
|----------|------|
| `Intent.ACTION_BATTERY_CHANGED` | 电池电量变化 |
| `Intent.ACTION_BATTERY_LOW` | 电池电量低 |
| `Intent.ACTION_BATTERY_OKAY` | 电池电量恢复正常 |
| `Intent.ACTION_BOOT_COMPLETED` | 系统启动完成 |
| `Intent.ACTION_PACKAGE_ADDED` | 应用安装完成 |
| `Intent.ACTION_NETWORK_STATE_CHANGED` | 网络状态变化 |

### 使用步骤

1. **创建广播接收器**：
```kotlin
val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 处理广播
    }
}
```

2. **注册广播接收器**：
```kotlin
override fun onStart() {
    super.onStart()
    val filter = IntentFilter().apply {
        addAction(CUSTOM_ACTION)
        addAction(Intent.ACTION_BATTERY_CHANGED)
    }
    registerReceiver(receiver, filter)
}
```

3. **注销广播接收器**：
```kotlin
override fun onStop() {
    super.onStop()
    unregisterReceiver(receiver)
}
```

4. **发送自定义广播**：
```kotlin
val intent = Intent(CUSTOM_ACTION).apply {
    putExtra("message", "你好！")
}
sendBroadcast(intent)
```

### 静态注册（AndroidManifest.xml）

```xml
<receiver android:name=".MyReceiver">
    <intent-filter>
        <action android:name="com.example.demo.CUSTOM_ACTION" />
    </intent-filter>
</receiver>
```

### 本地广播（LocalBroadcastManager）

为了安全起见，应用内部通信建议使用 LocalBroadcastManager：

```kotlin
// 发送本地广播
val intent = Intent(CUSTOM_ACTION)
LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

// 接收本地广播
LocalBroadcastManager.getInstance(this).registerReceiver(
    receiver,
    IntentFilter(CUSTOM_ACTION)
)
```

### 注意事项

1. **生命周期**：onReceive() 方法必须在 10 秒内完成
2. **线程**：onReceive() 在主线程执行，不要进行耗时操作
3. **内存泄漏**：动态注册必须在 onStop() 中注销
4. **安全性**：敏感广播使用权限保护
5. **有序广播**：可以使用 sendOrderedBroadcast() 控制接收顺序
