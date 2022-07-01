package xyz.xeonds.mirai.autoreply

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(JvmPluginDescription(
    id = "xyz.xeonds.mirai.autoreply", name = "自动回复插件", version = "0.1.0"
) {
    author("xeonds@stu.xidian.edu.cn")
    info(
        """
            根据关键词自动触发回复。详情见Readme文档
        """.trimIndent()
    )
}) {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onEnable() {
        logger.info { "Auto-reply 已加载" }
        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            when (val res = messageHandler(message, group.id)) {
                "" -> {}
                else -> group.sendMessage(res)
            }
            return@subscribeAlways
        }
        eventChannel.subscribeAlways<FriendMessageEvent> {
            val table = loadWordTable()

            if (sender.id in table.admin && message.contentToString().startsWith("--")) {
                val res = message.contentToString().split("\n")[0].split(" ")
                when (res[0]) {
                    "--add-rule" -> {
                        table.rule.add(0, DataTable.Rule(res[1], res[2], res[3]))
                        saveWordTable(table)
                        sender.sendMessage("已添加回复规则")
                    }
                    "--import-config" -> {
                        val config = message.contentToString().split("\n", limit = 2)[1]
                        saveWordTable(Json.decodeFromString(config))
                        sender.sendMessage("已装载配置文件")
                    }
                    "--export-config" -> {
                        sender.sendMessage(Json.encodeToString(table))
                    }
                }
            }
            when (val res = messageHandler(message, friend.id)) {
                "" -> {}
                else -> sender.sendMessage(res)
            }
            return@subscribeAlways
        }
    }

    override fun onDisable() {
        logger.info { "Auto-reply 已卸载" }
    }
}
