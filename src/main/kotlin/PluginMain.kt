package xyz.xeonds.mirai.autoreply

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.xeonds.mirai.autoreply",
        name = "自动回复插件",
        version = "0.1.0"
    ) {
        author("xeonds@stu.xidian.edu.cn")
        info(
            """
            根据关键词自动触发回复。详情见Readme文档
        """.trimIndent()
        )
    }
) {
    override fun onEnable() {
        logger.info { "Auto-reply loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"
        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            when (val res = messageHandler(message)) {
                "" -> {}
                else -> group.sendMessage(res)
            }
            return@subscribeAlways
        }
        eventChannel.subscribeAlways<FriendMessageEvent> {
            when (val res = messageHandler(message)) {
                "" -> {}
                else -> sender.sendMessage(res)
            }
            return@subscribeAlways
        }
    }

    override fun onDisable() {
        logger.info { "Auto-reply unloaded." }
    }
}
