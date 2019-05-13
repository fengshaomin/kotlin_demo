package com.learn.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

@Component
class FileUpload {
    @Value("\${web.file-path}")
    private val upload_path: String? = null

    @Throws(IOException::class)
    fun save_upload_file(file: ByteArray, file_name: String) {

        val out = FileOutputStream(upload_path + File.separator + file_name)
        out.write(file)
        out.flush()
        out.close()
    }

    fun get_filelist(isAddDirectory: Boolean): List<String> {

        val directoryPath = upload_path
        val list = ArrayList<String>()
        val baseFile = File(directoryPath!!)
        if (baseFile.isFile || !baseFile.exists()) {
            return list
        }
        val files = baseFile.listFiles()
        for (file in files!!) {
            if (file.isDirectory) {
                if (isAddDirectory) {
                    list.add(file.absolutePath)
                }
                list.addAll(get_filelist(isAddDirectory))
            } else {
                list.add(file.name)
            }
        }
        return list
    }

    companion object {

        // 过滤特殊字符 p
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        @Throws(PatternSyntaxException::class)
        fun StringFilter(str: String): String {
            val regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(str)
            return m.replaceAll("").trim { it <= ' ' }
        }
    }

}
