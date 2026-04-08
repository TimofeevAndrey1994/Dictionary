# 📖 Dictionary

Простое Android-приложение для поиска английских слов.
Делаю как pet-проект, чтобы прокачать Android-разработку и собрать нормальное портфолио.

---

## 🚀 Что уже есть

* 🔍 Поиск слов через API
* 📱 UI на Jetpack Compose
* 🧠 Состояния экрана:

  * Loading / Error / Empty / Success
* ⏳ Debounce при вводе
* 🕓 История поиска
* ❌ Запрет некорректного ввода

---

## 🛠 Технологии

* Kotlin
* Jetpack Compose
* ViewModel
* Coroutines
* Flow / StateFlow / callbackFlow
* Retrofit
* SharedPreferences

---

## 🧠 Как устроено

Разделение на слои:

```id="p9s3mk"
data / domain / presentation / ui
```

* **data** — API + хранение
* **domain** — use case'ы
* **presentation** — ViewModel + состояния
* **ui** — Compose

---

## 🔄 Состояние экрана

* `StateFlow` как источник истины
* `SearchUiState` — всё состояние экрана
* `SearchResultState` — отдельно результат поиска

---

## 🔁 Про Flow

* **StateFlow** — состояние UI
* **Flow** — обычные операции
* **callbackFlow** — история поиска

```kotlin id="b7v2sd"
override fun getSearchHistory(): Flow<List<String>> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "SEARCH_HISTORY") {
            trySend(readHistory())
        }
    }

    prefs.registerOnSharedPreferenceChangeListener(listener)
    trySend(readHistory())

    awaitClose {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
```

---

## ⚙️ Как работает поиск

1. Ввод текста
2. debounce (cancel Job + delay)
3. запрос в API
4. обновление состояния
5. UI перерисовывается

---

## 🤔 Почему так сделал

**StateFlow вместо LiveData**
Compose с ним работает проще. Плюс понятная модель — одно состояние, один источник истины.

---

**Разделил состояние на SearchUiState и SearchResultState**
Сначала держал всё в одном — быстро стало неудобно.
Так проще управлять экраном и не путаться в состояниях.

---

**callbackFlow для истории поиска**
История хранится в SharedPreferences, там callback через listener.
Обернул в callbackFlow, чтобы получать обновления как Flow и не дергать вручную UI.

---

**Debounce через Job + delay**
Сделал вручную, чтобы лучше понять как это работает.
Позже можно заменить на Flow-операторы.

---

**Route + Screen**
Чтобы не смешивать UI и логику.
Route работает с ViewModel, Screen просто рисует.

---

**Пока без DI (Hilt)**
Можно было сразу подключить, но решил сначала собрать всё руками, чтобы лучше понимать зависимости.

---

**Repository + UseCase**
Даже для pet-проекта решил сразу делать нормально, чтобы потом не переписывать всё при усложнении.

---

**SharedPreferences для истории**
Просто и достаточно для текущей задачи.
Если будет сложнее логика — заменю на DataStore или БД.

---

## 📦 Техническое

* minSdk 24
* targetSdk 35
* Compose + Material3

---

## 🚧 Что дальше

* навигация
* DI (Hilt)
* экран деталей слова
* аудио
* улучшение хранения

---

## 👨‍💻 Автор

Andrey Timofeev
https://github.com/TimofeevAndrey1994
