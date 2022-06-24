package xyz.xeonds.mirai.autoreply

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.message.data.MessageChain

fun messageHandler(msg: MessageChain): String {
    val table = loadWordTable()

    //关键字回复功能
    table.rule.forEach { rule ->
        if (rule.word in msg.contentToString()) {
            when (rule.mode) {
                "start-with" -> {
                    if (msg.contentToString().startsWith(rule.word)) {
                        return rule.reply
                    }
                }
                "end-with" -> {
                    if (msg.contentToString().endsWith(rule.word)) {
                        return rule.reply
                    }
                }
                //TODO：关键词列表：使用分号分割一列关键词，同时包含所有关键词则回复
                "contain" -> return rule.reply
                "equal"-> if (rule.word==msg.contentToString()){
                    return rule.reply
                }
                //TODO：变量：可以在回复中使用一些预定义变量，比如时间、自定义常量，原消息等，尝试支持正则
                //TODO：时间触发模式：发消息时如果在某一个时间区间内则回复
                //TODO：规则功能列表：可以将多种条件组合使用，达到灵活回复的效果
                //TODO：被at：当被at时回复一些内容
                //TODO：被戳：被戳时回复一些内容
            }
        }
    }
    //TODO：关键词添加功能。特定账号私聊机器人会进入关键词添加模式
    //TODO：规则文件导出导入功能
    //TODO：其他功能开关，例如复读机等
    return ""
}

@OptIn(ExperimentalSerializationApi::class)
fun loadWordTable(): DataTable {
    val repTable = PluginMain.resolveDataFile(PluginMain.dataFolder.absolutePath + "/reply-table-v1.json")
    if (!repTable.exists()) {
        repTable.writeText("{\"rule\":[]}")
    }
    return Json.decodeFromString(repTable.readText())
}