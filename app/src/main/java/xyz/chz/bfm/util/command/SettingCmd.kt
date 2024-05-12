package xyz.chz.bfm.util.command

import android.util.Log
import xyz.chz.bfm.util.terminal.TerminalHelper.execRootCmd
import xyz.chz.bfm.util.terminal.TerminalHelper.getYaml
import xyz.chz.bfm.util.terminal.TerminalHelper.saveYamlToFile

object SettingCmd {
    private const val TAG = "BoxForRoot.SettingCmd"
    private const val ROOTYAMLPATH = "/data/adb/box/clash/config.yaml"

    private val CLASHDATA: MutableMap<String, Any> by lazy { getYaml(ROOTYAMLPATH) }

    val networkMode: String
        get(): String{
            return execRootCmd("grep 'network_mode=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")
        }

    fun setNetworkMode(mode: String): String {
        return execRootCmd("sed -i 's/network_mode=.*/network_mode=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val proxyMode: String
        get() = execRootCmd("grep 'proxy_mode=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setProxyMode(mode: String): String {
        return execRootCmd("sed -i 's/proxy_mode=.*/proxy_mode=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val cron: String
        get() = execRootCmd("grep 'run_crontab=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setCron(mode: String): String {
        return execRootCmd("sed -i 's/run_crontab=.*/run_crontab=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val geo: String
        get() = execRootCmd("grep 'update_geo=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setGeo(mode: String): String {
        return execRootCmd("sed -i 's/update_geo=.*/update_geo=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val memcg: String
        get() = execRootCmd("grep 'cgroup_memcg=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setMemcg(mode: String): String {
        return execRootCmd("sed -i 's/cgroup_memcg=.*/cgroup_memcg=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val blkio: String
        get() = execRootCmd("grep 'cgroup_blkio=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setBlkio(mode: String): String {
        return execRootCmd("sed -i 's/cgroup_blkio=.*/cgroup_blkio=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val cpuset: String
        get() = execRootCmd("grep 'cgroup_cpuset=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setCpuset(mode: String): String {
        return execRootCmd("sed -i 's/cgroup_cpuset=.*/cgroup_cpuset=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val subs: String
        get() = execRootCmd("grep 'update_subscription=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setSubs(mode: String): String {
        return execRootCmd("sed -i 's/update_subscription=.*/update_subscription=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val redirHost: Boolean
        get() = "redir-host" == CLASHDATA["enhanced-mode"].toString()

    fun setRedirHost(mode: String) {
        CLASHDATA["enhanced-mode"] = mode
        saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
    }

    val quic: Boolean
        get() = "enable" == execRootCmd("grep 'quic=' /data/adb/box/scripts/box.iptables | sed 's/^.*=//' | sed 's/\"//g'")

    fun setQuic(mode: String): String {
        return execRootCmd("sed -i 's/quic=.*/quic=\"$mode\"/;' /data/adb/box/scripts/box.iptables")
    }

    val unified: Boolean
        get() = "true" == CLASHDATA["unified-delay"].toString()

    fun setUnified(mode: String) {
        CLASHDATA["unified-delay"] = mode
        saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
    }

    val geodata: Boolean
        get() = "true" == CLASHDATA["geodata-mode"].toString()

    fun setGeodata(mode: String) {
        CLASHDATA["geodata-mode"] = mode
        saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
    }

    val tcpCon: Boolean
        get() = "true" == CLASHDATA["tcp-concurrent"].toString()

    fun setTcpCon(mode: String) {
        CLASHDATA["tcp-concurrent"] = mode
        saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
    }

    val sniffer: Boolean
        get() = "true" == (CLASHDATA["sniffer"] as? Map<*, *>)?.get("enable").toString()

    fun setSniffer(mode: String) {
        val sniffer = CLASHDATA.getOrPut("sniffer") { mutableMapOf<String, Any>() }
        // 检查 sniffer 是否确实是 MutableMap 类型
        if (sniffer is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            val snifferMap = sniffer as MutableMap<String, Any>
            snifferMap["enable"] = mode
            saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
        } else {
            // 如果 sniffer 不是一个 MutableMap，则记录错误或抛出异常
            Log.i(TAG, "Error: 'sniffer' in CLASHDATA is not a MutableMap")
        }
    }

    val ipv6: Boolean
        get() = "true" == execRootCmd("grep 'ipv6=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setIpv6(mode: String): String {
        return execRootCmd("sed -i 's/ipv6=.*/ipv6=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val findProc: String
        get() = CLASHDATA["find-process-mode"].toString()

    fun setFindProc(mode: String) {
        CLASHDATA["find-process-mode"] = mode
        saveYamlToFile(CLASHDATA, ROOTYAMLPATH)
    }

    val clashType: String
        get() = execRootCmd("grep 'xclash_option=' /data/adb/box/settings.ini | sed 's/^.*=//' | sed 's/\"//g'")

    fun setClashType(mode: String): String {
        return execRootCmd("sed -i 's/xclash_option=.*/xclash_option=\"$mode\"/;' /data/adb/box/settings.ini")
    }

    val core: String
        get() = execRootCmd("grep 'bin_name=' /data/adb/box/settings.ini | sed 's/^.*=//g' | sed 's/\"//g'")

//    fun setCore(x: String): Boolean {
//        return execRootCmdSilent("sed -i 's/bin_name=.*/bin_name=$x/;' /data/adb/box/settings.ini") != -1
//    }

    var setCore: String = ""
        set(value) {
            field = value
            execRootCmd("sed -i 's/bin_name=.*/bin_name=$field/;' /data/adb/box/settings.ini")
        }

}