package main.kotlin.sound

import javax.sound.sampled.*
import java.io.File
import java.io.IOException

fun recordMicrophone(outputFile: String, durationMillis: Long) {
    val format = AudioFormat(44100.0f, 16, 2, true, true) // 44.1 kHz, 16-bit, Stereo
    val info = DataLine.Info(TargetDataLine::class.java, format)

    if (!AudioSystem.isLineSupported(info)) {
        println("Микрофон не поддерживается")
        return
    }

    val microphone = AudioSystem.getLine(info) as TargetDataLine
    microphone.open(format)
    microphone.start()

    val audioStream = AudioInputStream(microphone)

    val outFile = File(outputFile)

    println("🎙️ Запись микрофона...")

    Thread {
        try {
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }.start()

    Thread.sleep(durationMillis)
    microphone.stop()
    microphone.close()

    println("✅ Запись сохранена: $outputFile")
}

fun main() {
    recordMicrophone("microphone_audio.wav", 10000) // 10 секунд записи
}
