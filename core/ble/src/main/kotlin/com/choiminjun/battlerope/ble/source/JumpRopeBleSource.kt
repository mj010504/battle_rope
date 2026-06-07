package com.choiminjun.battlerope.ble.source

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import com.choiminjun.battlerope.ble.BleGattClient
import com.choiminjun.battlerope.domain.model.BleConnectionState
import com.choiminjun.battlerope.domain.model.JumpRopeSnapshot
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("MissingPermission")
class JumpRopeBleSource @Inject constructor(
    private val bleGattClient: BleGattClient,
) : BleGattClient.Listener {

    companion object {
        const val DEVICE_NAME: String = "JIKKO_TEST_BLE"

        val CMD_START: ByteArray = byteArrayOf(0x10, 0x00, 0x10)
        val CMD_STOP: ByteArray = byteArrayOf(0x11, 0x00, 0x11)

        const val ACK_CMD_ID: Byte = 0x90.toByte()
        const val EXERCISE_CMD_ID: Byte = 0xA1.toByte()
        const val EXERCISE_PAYLOAD_LENGTH: Byte = 0x06

        data class AckResult(val requestCmdId: Byte, val resultCode: Byte) {
            val isSuccess: Boolean get() = resultCode == 0x00.toByte()
        }
    }

    private val _scanResults = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scanResults = _scanResults.asStateFlow()

    private val _ackResult = MutableSharedFlow<AckResult>(extraBufferCapacity = 1)
    val ackResult = _ackResult.asSharedFlow()

    private val _connectionState = MutableStateFlow<BleConnectionState>(BleConnectionState.Disconnected)
    val connectionState = _connectionState.asStateFlow()

    private val _snapshot = MutableStateFlow<JumpRopeSnapshot?>(null)
    val snapshot = _snapshot.asStateFlow()

    init {
        registerListener()
    }

    private fun registerListener() {
        bleGattClient.listener = this
        Timber.d("BleGattClient 리스너 등록 완료")
    }

    fun startScan() {
        Timber.d("줄넘기 장치 스캔 시작 | DEVICE_NAME: $DEVICE_NAME")
        _scanResults.value = emptyList()
        val filter = ScanFilter.Builder()
            .setDeviceName(DEVICE_NAME)
            .build()
        bleGattClient.startScan(listOf(filter))
    }

    fun stopScan() {
        Timber.d("스캔 중지")
        bleGattClient.stopScan()
    }

    fun connect(device: BluetoothDevice) {
        Timber.d("장치 연결 요청 | 이름: ${device.name} | 주소: ${device.address}")
        bleGattClient.connect(device)
    }

    fun disconnect() {
        Timber.d("장치 연결 해제 요청")
        bleGattClient.disconnect()
    }

    fun subscribeToExerciseData() {
        Timber.d("운동 데이터 알림 구독")
        bleGattClient.enableNotification(true)
    }

    fun unsubscribeFromExerciseData() {
        Timber.d("운동 데이터 알림 구독 해제")
        bleGattClient.enableNotification(false)
    }

    fun startExercise() {
        Timber.d("START 명령 전송")
        bleGattClient.writeCommand(CMD_START)
    }

    fun stopExercise() {
        Timber.d("STOP 명령 전송")
        bleGattClient.writeCommand(CMD_STOP)
    }

    override fun onDeviceFound(result: ScanResult) {
        val device = result.device
        if (_scanResults.value.none { it.address == device.address }) {
            val record = result.scanRecord
            Timber.d(
                "새 장치 발견 | name=%s | recordName=%s | address=%s | rssi=%d | serviceUuids=%s",
                device.name,
                record?.deviceName,
                device.address,
                result.rssi,
                record?.serviceUuids,
            )
            _scanResults.value += device
        }
    }

    override fun onConnectionStateChanged(state: BleGattClient.ConnectionState) {
        val domainState = when (state) {
            BleGattClient.ConnectionState.Disconnected -> BleConnectionState.Disconnected
            BleGattClient.ConnectionState.Connecting -> BleConnectionState.Connecting
            is BleGattClient.ConnectionState.Connected -> BleConnectionState.Connected(state.deviceName)
        }
        Timber.d("연결 상태 업데이트: %s", domainState)
        _connectionState.value = domainState
        if (domainState is BleConnectionState.Disconnected) {
            Timber.d("연결 끊김 → 스냅샷 초기화")
            _snapshot.value = null
        }
    }

    override fun onDataReceived(data: ByteArray) {
        Timber.d("원시 데이터 수신 | ${data.size} bytes")
        if (data.isEmpty()) return
        when (data[0]) {
            ACK_CMD_ID -> parseAckPacket(data)
            EXERCISE_CMD_ID -> parseExercisePacket(data)
            else -> Timber.w("알 수 없는 CMD_ID | 0x${"%02X".format(data[0])}")
        }
    }

    private fun parseAckPacket(data: ByteArray) {
        // [0x90 | 0x02 | request_cmd_id | result_code | checksum] = 5 bytes
        if (data.size < 5) {
            Timber.w("ACK 패킷 크기 오류 | 기대: 5 bytes | 수신: ${data.size} bytes")
            return
        }
        val requestCmdId = data[2]
        val resultCode = data[3]
        val expected = (
            (data[0].toInt() and 0xFF) + (data[1].toInt() and 0xFF) +
                (data[2].toInt() and 0xFF) + (data[3].toInt() and 0xFF)
            ) and 0xFF
        if (expected != (data[4].toInt() and 0xFF)) {
            Timber.w("ACK 체크섬 오류 | 기대: 0x${"%02X".format(expected)} | 수신: 0x${"%02X".format(data[4])}")
            return
        }
        Timber.d("ACK 수신 | requestCmdId=0x${"%02X".format(requestCmdId)} | resultCode=0x${"%02X".format(resultCode)}")
        _ackResult.tryEmit(AckResult(requestCmdId, resultCode))
    }

    private fun parseExercisePacket(data: ByteArray) {
        // [CMD_ID(1) | LENGTH(1) | Payload(6) | CHECKSUM(1)] = 9 bytes, Little Endian
        if (data.size < 9) {
            Timber.w("패킷 크기 오류 | 기대: 9 bytes | 수신: ${data.size} bytes")
            return
        }
        if (data[1] != EXERCISE_PAYLOAD_LENGTH) {
            Timber.w("LENGTH 불일치 | 기대: 0x${"%02X".format(EXERCISE_PAYLOAD_LENGTH)} | 수신: 0x${"%02X".format(data[1])}")
            return
        }

        val payloadSum = (2 until 8).sumOf { data[it].toInt() and 0xFF }
        val checksum = ((data[0].toInt() and 0xFF) + (data[1].toInt() and 0xFF) + payloadSum) and 0xFF
        if (checksum != (data[8].toInt() and 0xFF)) {
            Timber.w("체크섬 오류 | 기대: 0x${"%02X".format(checksum)} | 수신: 0x${"%02X".format(data[8])}")
            return
        }

        val elapsedSec = (data[2].toInt() and 0xFF) or ((data[3].toInt() and 0xFF) shl 8)
        val userACount = (data[4].toInt() and 0xFF) or ((data[5].toInt() and 0xFF) shl 8)
        val userBCount = (data[6].toInt() and 0xFF) or ((data[7].toInt() and 0xFF) shl 8)

        Timber.d("운동 데이터 파싱 완료 | elapsed=${elapsedSec}s | A=$userACount | B=$userBCount")
        _snapshot.value = JumpRopeSnapshot(elapsedSec, userACount, userBCount)
    }
}
