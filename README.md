# RanaBot

> *おもしれー女~*

RanaBot is a simple QQ bot built with Spring Boot 3.0, fully compatible with OneBot API.

## Features

- **Easy plugin development with annotation-based support**
    - 使用 `@RanaPlugin`注解快速开发插件
- **Plugin management using HTTP API** 
    - 使用 HTTP API 动态管理插件
- **Support for NapCat(Onebot API)**
    - 支持 NapCat(Onebot API)

## Discussion

[QQ群](https://qm.qq.com/q/hcEjybPC94)

## Getting Started

1. Clone the repository
2. edit application.yml (mainly about your NapCat)
3. run `mvn clean package`
4. run `RanaBotApplication`
5. ライブやろ！

> *tips*: default openAPI address is `http://ip:9999/rana-bot/doc.html`

## Contributing

1. Fork it
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create new Pull Request
6. 一生、バンドしてくれる？

## Plugin Development

1. run `mvn clean install -pl rana-bot-plugin -am` on rana-bot project root
2. create a new project with `pom.xml` & depend on `rana-bot-plugin`
```
<dependencies>
        <dependency>
            <groupId>com.sosorin</groupId>
            <artifactId>rana-bot-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
4. create a new class with `@RanaPlugin` annotation & extends `AbstractPlugin`
5. package your plugin using maven
6. move your plugin jar to main project's `plugins` directory (or you can set the path `bot.plugin-dir` in `application.yml`)

## Thanks

- [NapCat](https://github.com/NapNeko/NapCatQQ)
- [Onebot](https://github.com/howmanybots/onebot)

## License

[MIT](LICENSE)

**使用本项目时，请确保遵守当地的法律法规。**