# Auto-reply

Mirai 自动回复插件。 使用Kotlin构建。

## 特性

- 基于`json`的关键字&回复字词库
- 借助`Kotlin`实现，代码简洁，性能优秀

## 用法

在插件目录下提供`reply-table-v1.json`。

`json`结构如下：

>下面是V1版本的json文件，后续应该不会发生过大的改变

```json
{
  "rule":[
      {
        "mode": "start-with",
        "word": "114514",
        "reply": "1919810"
      },{
        "mode": "contain",
        "word": "先辈",
        "reply": "*察觉*"
      }
  ]
}
```

如上。其中回复规则部分在rule下面的数组中。数组中每个元素都由三个键值对组成，三个键分别表示了回复规则，期待的关键字和回复的内容。

- mode 即触发回复的模式。

当前版本只支持下列功能：

|     关键字      | 功能        |
|:------------:|:----------|
| `start-with` | 以...开头    |
|  `end-with`  | 以...结束    |
|  `contain`   | 包含...关键字  |
|   `equal`    | 消息内容等于关键字 |

后续会支持更多关键字，也欢迎在issue。

- word 即触发回复的关键字/规则。
- reply 即回复的内容。

## 管理员工具

在0.2.0中添加。管理员账号列表需手动在`reply-table-v1.json`的`admin`中设置。注意账号是数字id，而非字符串。

用法如下：

|  功能  | 指令                               |
|------|:---------------------------------|
| 添加规则 | `--add-rule [规则] [关键字] [回复]`     |
| 导入配置 | `--import-config[换行]在新的行中粘贴配置文件` |
| 导出配置 | `--export-config`                |

其中的中括号不是指令的一部分，仅仅是标识作用。

## TODO
- 关键词列表：使用分号分割一列关键词，同时包含所有关键词则回复
- 变量：可以在回复中使用一些预定义变量，比如时间、自定义常量，原消息等，尝试支持正则
- 时间触发模式：发消息时如果在某一个时间区间内则回复
- 规则功能列表：可以将多种条件组合使用，达到灵活回复的效果
- 被at：当被at时回复一些内容
- 被戳：被戳时回复一些内容
- 关键词添加功能。特定账号私聊机器人会进入关键词添加模式
- 规则文件导出导入功能
- 其他功能开关，例如复读机等