package com.learn.controller

import cn.hutool.http.HttpUtil
import com.learn.util.FileUpload
import com.learn.util.Qrcode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.Enumeration
import java.util.HashMap
import java.util.logging.Logger

/**
 * Created by fengshaomin on 2018/7/20 0020.
 */

@Controller
@RequestMapping("/")
class MainController {
    @Autowired
    private val qrcode: Qrcode? = null
    @Autowired
    private val fileupload: FileUpload? = null
    @Autowired
    private val mail: JavaMailSender? = null

    @Value("\${web.file-path}")
    private val upload_path: String? = null

    private val logger = Logger.getLogger(MainController::class.toString())

    @RequestMapping("")
    fun hello(): String {
        return "index"
    }

    @RequestMapping("/index")
    fun index(): String {
        return "index"
    }

    @GetMapping("/qrcode")
    fun Qrcode(): String {
        return "index"
    }


    @RequestMapping("/qrcode", method = [RequestMethod.POST])
    @Throws(IOException::class)
    fun qrcode(map: Model, request: HttpServletRequest): String {

        val url = request.getParameter("url")

        val fire_name = qrcode!!.create_qrcode(url)

        map.addAttribute("file", fire_name)
        return "qrcode"
    }

    @RequestMapping("/download/{file_name}")
    @Throws(UnsupportedEncodingException::class)
    fun download_img(@PathVariable("file_name") file_name: String, res: HttpServletResponse) {
        qrcode!!.download(res, file_name)
    }

    @GetMapping("/upload")
    fun upload_file_get(): String {
        return "upload"
    }

    @PostMapping("/upload")
    @Throws(IOException::class)
    fun upload_file_post(@RequestParam("file") file: MultipartFile): String {

        val file_name = file.originalFilename
        val tmp_file = File(file_name!!)
        val real_name = tmp_file.name
        logger.info("upload file -----------------------$real_name")
        fileupload!!.save_upload_file(file.bytes, real_name)


        return "redirect:/file"
    }

    @RequestMapping("/file")
    fun list_file(map: Model): String {

        val result = fileupload!!.get_filelist(false)
        map.addAttribute("file", result)
        return "file"
    }

    @RequestMapping(value = "/zhuang")
    fun zhuang(map: Model): String {
        map.addAttribute("name", "冯文韬")
        map.addAttribute("tel", "15110202919")
        map.addAttribute("addr", "海淀区厢黄旗东路柳浪家园南里26号楼1单元701")
        return "zhuang"
    }

    @RequestMapping(value = "/list")
    fun list_header(request: HttpServletRequest, map: Model): String {
        val headerList = request.headerNames
        val map_tmp = HashMap<String, String>()
        while (headerList.hasMoreElements()) {
            val name = headerList.nextElement().toString()
            val `val` = request.getHeader(name)
            map_tmp[name] = `val`
        }
        map.addAttribute("headers", map_tmp)
        return "list"

    }

    @ResponseBody
    @RequestMapping(value = "/test")
    fun get_folder(request: HttpServletRequest, map: Model): String {


        return HttpUtil.get("http://web.bjsasc.com")

    }

}
