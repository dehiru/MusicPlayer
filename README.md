Музыкальный плеер для Android на Jetpack Compose.\n
В приложении применяется архитектурный паттерн MVVM.\n
Данные загружаются через API https://storage.googleapis.com/uamp/catalog.json\n
с помощью Retrofit и конвертируются в объекты Kotlin конвертером kotlinx.serialization Converter.\n
Для загрузки изображений используется Coil.\n
Информация о пользователе хранится в SharedPreferences.\n
