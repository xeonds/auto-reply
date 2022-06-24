package xyz.xeonds.mirai.autoreply

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataTable(@SerialName("rule") val rule: List<Rule>) {
    @Serializable
    data class Rule(
        @SerialName("mode") val mode: String,
        @SerialName("word") val word: String,
        @SerialName("reply") val reply: String
    )
}
