package main.kotlin

import main.kotlin.config.ZoomAuth
import main.kotlin.config.ZoomConfig
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.awt.Desktop
import java.net.URI
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


fun main() {
    val port = 8080
    val server = Server(port)
    val context = ServletContextHandler(ServletContextHandler.SESSIONS)
    server.handler = context

    // Добавляем обработчик для получения authorization_code
    context.addServlet(ServletHolder(object : HttpServlet() {
        override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
            val authorizationCode = req.getParameter("code") ?: ""
            println("\n🔑 Получен authorization_code: $authorizationCode")

            if (authorizationCode.isNotEmpty()) {
                val token = ZoomAuth.getAccessToken(
                    ZoomConfig.CLIENT_ID,
                    ZoomConfig.CLIENT_SECRET,
                    authorizationCode,
                    ZoomConfig.REDIRECT_URI
                )
                println("\n✅ Ваш Zoom access_token: $token")
            } else {
                println("⚠️ Ошибка: authorization_code не найден в запросе.")
            }

            resp.writer.println("Теперь вы можете вернуться в консоль.")
            server.stop()  // Останавливаем сервер после получения кода
        }
    }), "/callback")  // Указываем путь для обработки

    // Запускаем сервер в отдельном потоке
    Thread { server.start() }.start()
    println("🚀 Локальный сервер запущен на http://localhost:$port/callback")

    // Открываем браузер для аутентификации
    val authUrl = "https://zoom.us/oauth/authorize?response_type=code&client_id=${ZoomConfig.CLIENT_ID}&redirect_uri=${ZoomConfig.REDIRECT_URI}"
    println("🔗 Откройте эту ссылку в браузере и войдите в Zoom:")
    println(authUrl)

    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(authUrl))
    }
}
