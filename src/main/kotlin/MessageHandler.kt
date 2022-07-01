package xyz.xeonds.mirai.autoreply

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.message.data.MessageChain

fun messageHandler(msg: MessageChain, id: Long): String {
    val table = loadWordTable()
    table.rule.forEach { rule ->
        when (rule.mode) {
            "start-with" -> if (msg.contentToString().startsWith(rule.word)) return rule.reply
            "end-with" -> if (msg.contentToString().endsWith(rule.word)) return rule.reply
            //TODO：关键词列表：使用分号分割一列关键词，同时包含所有关键词则回复
            "contain" -> if (rule.word in msg.contentToString()) return rule.reply
            "equal" -> if (rule.word == msg.contentToString()) return rule.reply
            //TODO：变量：可以在回复中使用一些预定义变量，比如时间、自定义常量，原消息等，尝试支持正则
            //TODO：时间触发模式：发消息时如果在某一个时间区间内则回复
            //TODO：规则功能列表：可以将多种条件组合使用，达到灵活回复的效果
            //TODO：被at：当被at时回复一些内容
            //TODO：被戳：被戳时回复一些内容
            "repeat" ->
                //仅当群/用户id为预设id时复读
                if (rule.reply.toLong() == id && Repeater.execute(
                        msg.contentToString(), rule.word.toInt(), id    //复读内容以及触发复读次数
                    )
                ) return msg.contentToString()
        }
    }
    return ""
}

@OptIn(ExperimentalSerializationApi::class)
fun loadWordTable(): DataTable {
    val repTable = PluginMain.resolveDataFile(PluginMain.dataFolder.absolutePath + "/reply-table-v1.json")
    if (!repTable.exists()) {
        repTable.writeText("{\"rule\":[],\"admin\":[]}")
    }
    return Json.decodeFromString(repTable.readText())
}

@OptIn(ExperimentalSerializationApi::class)
fun saveWordTable(table: DataTable) {
    val repTable = PluginMain.resolveDataFile(PluginMain.dataFolder.absolutePath + "/reply-table-v1.json")

    repTable.writeText(Json.encodeToString(table))
}

/**
 * Repeater:实现自动回复的单例类
 */
object Repeater {
    private var counter = mutableMapOf<Long, Int>()
    private var sentence = mutableMapOf<Long, String>()

    fun execute(msg: String, cnt: Int, id: Long): Boolean {
        if (msg == sentence[id]) {
            counter[id] = counter[id]!! + 1
            return cnt == counter[id]
        } else {
            counter[id] = 1
            sentence[id] = msg
        }
        return false
    }
}