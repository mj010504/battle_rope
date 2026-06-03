package com.choiminjun.makeitall.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("MissingPermission")
class BleGattClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
    }

    sealed interface ConnectionState {
        data object Disconnected : ConnectionState
        data object Connecting : ConnectionState
        data class Connected(val deviceName: String) : ConnectionState
    }

    interface Listener {
        fun onDeviceFound(result: ScanResult)
        fun onConnectionStateChanged(state: ConnectionState)
        fun onDataReceived(data: ByteArray)
    }

    var listener: Listener? = null

    private val bluetoothAdapter =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private var gatt: BluetoothGatt? = null
    private var writeChar: BluetoothGattCharacteristic? = null
    private var notifyChar: BluetoothGattCharacteristic? = null
    private val servicesDiscovered = AtomicBoolean(false)

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            listener?.onDeviceFound(result)
        }

        override fun onScanFailed(errorCode: Int) {
            Timber.e("스캔 실패 | errorCode=$errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Timber.d("연결 상태 변경 | status=$status | newState=$newState")
            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> {
                    Timber.d("연결 중...")
                    listener?.onConnectionStateChanged(ConnectionState.Connecting)
                }

                BluetoothProfile.STATE_CONNECTED -> {
                    Timber.d("연결 완료 → 서비스 탐색 시작 | 장치: ${gatt.device.address}")
                    gatt.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Timber.d("연결 끊김 | 장치: ${gatt.device.address} | GATT 닫음")
                    listener?.onConnectionStateChanged(ConnectionState.Disconnected)
                    gatt.close()
                    this@BleGattClient.gatt = null
                    writeChar = null
                    notifyChar = null
                    servicesDiscovered.set(false)
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (!servicesDiscovered.compareAndSet(false, true)) return
            Timber.d("서비스 탐색 완료 | status=%d | 장치: %s (%s)", status, gatt.device.name, gatt.device.address)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                assignCharacteristics(gatt)
                listener?.onConnectionStateChanged(
                    ConnectionState.Connected(gatt.device.name ?: gatt.device.address),
                )
            } else {
                Timber.w("서비스 탐색 실패 | status=%d", status)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
        ) {
            Timber.d("데이터 수신 | UUID: ${characteristic.uuid} | 데이터: ${value.toHexString()}")
            listener?.onDataReceived(value)
        }
    }

    private fun assignCharacteristics(gatt: BluetoothGatt) {
        gatt.services.flatMap { it.characteristics }.forEach { char ->
            val props = char.properties
            Timber.d("  [CHAR] %s | properties=%d", char.uuid, props)
            when {
                props and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0 -> {
                    notifyChar = char
                    Timber.d("    → notify 특성으로 지정")
                }
                props and BluetoothGattCharacteristic.PROPERTY_WRITE != 0 ||
                    props and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0 -> {
                    writeChar = char
                    Timber.d("    → write 특성으로 지정")
                }
            }
        }
        Timber.d("writeChar=%s | notifyChar=%s", writeChar?.uuid, notifyChar?.uuid)
    }

    fun startScan(filters: List<ScanFilter>? = null) {
        Timber.d("스캔 시작 | 필터: ${filters?.map { it.serviceUuid }}")
        bluetoothAdapter.bluetoothLeScanner?.startScan(filters, scanSettings, scanCallback)
    }

    fun stopScan() {
        Timber.d("스캔 중지")
        bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)
    }

    fun connect(device: BluetoothDevice) {
        Timber.d("연결 시도 | 이름: ${device.name} | 주소: ${device.address}")
        gatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
    }

    fun disconnect() {
        Timber.d("연결 해제 요청 | 주소: ${gatt?.device?.address}")
        gatt?.disconnect()
    }

    fun writeCommand(data: ByteArray) {
        val g = gatt ?: run {
            Timber.w("writeCommand 실패: GATT 연결 없음")
            return
        }
        val char = writeChar ?: run {
            Timber.w("writeCommand 실패: write 특성 없음")
            return
        }
        Timber.d("명령 전송 | uuid=%s | data=%s", char.uuid, data.toHexString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            g.writeCharacteristic(char, data, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
        } else {
            @Suppress("DEPRECATION")
            char.value = data
            @Suppress("DEPRECATION")
            g.writeCharacteristic(char)
        }
    }

    fun enableNotification(enable: Boolean) {
        val g = gatt ?: run {
            Timber.w("알림 설정 실패: GATT 연결 없음")
            return
        }
        val char = notifyChar ?: run {
            Timber.w("알림 설정 실패: notify 특성 없음")
            return
        }
        Timber.d("알림 %s | uuid=%s", if (enable) "활성화" else "비활성화", char.uuid)
        g.setCharacteristicNotification(char, enable)
        val descriptor = char.getDescriptor(CCCD_UUID) ?: run {
            Timber.w("알림 설정 실패: CCCD 디스크립터 없음")
            return
        }
        val value = if (enable) {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            g.writeDescriptor(descriptor, value)
        } else {
            @Suppress("DEPRECATION")
            descriptor.value = value
            @Suppress("DEPRECATION")
            g.writeDescriptor(descriptor)
        }
    }

    private fun ByteArray.toHexString() = joinToString(" ") { "%02X".format(it) }
}
