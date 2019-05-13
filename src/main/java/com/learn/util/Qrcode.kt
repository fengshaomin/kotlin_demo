package com.learn.util

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletResponse
import java.io.*
import java.net.URLEncoder
import java.nio.file.Path
import java.util.Hashtable

/**
 * Created by fengshaomin on 2018/7/20 0020.
 */
@Component
class Qrcode {

    @Value("\${web.qrcode-path}")
    private val qrcode_path: String? = null


    @Throws(IOException::class)
    fun create_qrcode(text: String): String {
        var file_name = (Math.random() * 1000000).toString()
        val width = 300
        val height = 300
        val format = "png"
        val hints = Hashtable()
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8")
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
        hints.put(EncodeHintType.MARGIN, 2)
        try {
            val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            file_name = "$file_name.png"
            val file_path = qrcode_path + File.separator + file_name

            val file = File(file_path).toPath()
            MatrixToImageWriter.writeToPath(bitMatrix, format, file)
            return file_name
        } catch (e: WriterException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return "error"
    }

    @Throws(UnsupportedEncodingException::class)
    fun download(res: HttpServletResponse, file_name: String) {

        res.setHeader("content-type", "application/octet-stream")
        res.contentType = "application/octet-stream"
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file_name, "UTF-8"))

        val buff = ByteArray(1024)
        var bis: BufferedInputStream? = null
        var os: OutputStream? = null
        try {
            os = res.outputStream
            bis = BufferedInputStream(FileInputStream(File(qrcode_path + File.separator + file_name
            )))
            var i = bis.read(buff)
            while (i != -1) {
                os!!.write(buff, 0, buff.size)
                os.flush()
                i = bis.read(buff)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

}
