package xyz.chz.bfm.util.terminal

import android.util.Log
import org.yaml.snakeyaml.LoaderOptions
import xyz.chz.bfm.BuildConfig
import org.yaml.snakeyaml.Yaml
import xyz.chz.bfm.BFRApp
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStream

object TerminalHelper {
    private const val TAG = "BoxForRoot.Terminal"
    // 设置 LoaderOptions 并创建 Yaml 实例
    private val options: LoaderOptions = LoaderOptions().apply {
        codePointLimit = 5000000  // 设置更高的字符点数限制
    }
    private val yaml: Yaml = Yaml(options)

    fun getYaml(rootFilePath: String): MutableMap<String, Any> {
        val context = BFRApp.appContext
        val file = File(context.filesDir, "tempYamlFile.yaml")

        // 执行命令复制文件
        execRootCmd("cp $rootFilePath ${file.absolutePath}")

        val yamlStream: InputStream = FileInputStream(file)
        return yaml.load(yamlStream)
    }

    fun saveYamlToFile(data: MutableMap<String, Any>, rootFilePath: String) {
        val yaml = Yaml()
        val context = BFRApp.appContext
        val file = File(context.filesDir, "tempYamlFile.yaml")

        try {
            // 使用 FileWriter 将 YAML 数据写入文件
            FileWriter(file).use { writer ->
                yaml.dump(data, writer)
            }

            // 执行命令需要 root 权限，将临时文件复制到目标路径
            execRootCmd("cp ${file.absolutePath} $rootFilePath")

        } catch (e: Exception) {
            Log.e(TAG, "Error saving YAML file", e)
        }
    }

    fun execRootCmd(cmd: String): String {
        return try {
            val process: Process = Runtime.getRuntime().exec("su -c $cmd")
            process.waitFor()
            val output = process.inputStream.bufferedReader().lineSequence().joinToString("\n")
            if (BuildConfig.DEBUG) Log.d(TAG, output)
            output
        } catch (e: Exception) {
            ""
        }
    }

    fun execRootCmdSilent(cmd: String): Int {
        return try {
            val process: Process = Runtime.getRuntime().exec("su -c $cmd")
            process.waitFor()
            process.exitValue()
        } catch (e: Exception) {
            -1
        }
    }

    fun execRootCmdVoid(cmd: String, callback: (Boolean) -> Unit) {
        try {
            val process = Runtime.getRuntime().exec("su -c $cmd")
            process.waitFor()
            callback(process.exitValue() == 0)
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }
}