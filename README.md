Музыкальный плеер для Android на Jetpack Compose.  
В приложении применяется архитектурный паттерн MVVM.  
Данные загружаются через API https://storage.googleapis.com/uamp/catalog.json  
с помощью Retrofit и конвертируются в объекты Kotlin конвертером kotlinx.serialization Converter.  
Для загрузки изображений используется Coil.  
Информация о пользователе хранится в SharedPreferences.  
