package com.polarj;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties( prefix = "polarj")
public class PolarjAppConfig
{
    // 文件都是含有时间戳的名字保存，可以随时删除。
    private @Getter @Setter String temporaryFilePath;

    // 把需要保存的文件放到一下文件夹，使用UserAccount里的email地址作为下一级文件夹
    // 例如，admin@test.com的用户，其文件保存在permanentFilePath/admin_test.com下
    // 主要是对应数据库中的FileInfo中的文件
    // 需要备份
    private @Getter @Setter String permanentFilePath;

    private @Getter @Setter String frontendBaseURL;

}
