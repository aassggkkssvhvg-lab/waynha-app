# دليل بناء وتثبيت تطبيق «وينها؟» على هواتف أندرويد (APK Build Guide)

تطبيق **«وينها؟»** هو تطبيق Android أصلي (Android Native) بالكامل مبني باستخدام:
- **Language**: Kotlin 2.0.0
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + StateFlow + Kotlin Coroutines
- **Compatibility**: Android 7.0 (API 24) فما فوق وصولاً إلى Android 15 (API 35)

---

## الطريقة 1: البناء عبر Android Studio (الأسهل والموصى بها)

1. افتح برنامج **Android Studio** (نسخة Ladybug أو Hedgehog أو أحدث).
2. اختر **File -> Open** وحدد مجلد `android` من هذا المشروع.
3. انتظر ثوانٍ حتى يقوم Android Studio بمزامنة ملفات Gradle تلقائياً (**Sync Project with Gradle Files**).
4. من القائمة العلوية اضغط:
   ```
   Build -> Build Bundle(s) / APK(s) -> Build APK(s)
   ```
5. بمجرد اكتمال البناء، ستظهر رسالة بالأسفل تضغط فيها على **locate**، أو ستجد ملف الـ APK الجاهز مباشرة في المسار:
   ```
   android/app/build/outputs/apk/debug/app-debug.apk
   ```
6. انقل ملف `app-debug.apk` إلى هاتفك عبر كابل USB أو تيليجرام/واتساب، واضغط عليه لتثبيته فوراً!

---

## الطريقة 2: البناء المباشر عبر سطر الأوامر (Terminal / Command Line)

تأكد من تثبيت **JDK 17** على جهازك، ثم نفّذ الأمر التالي:

### على أنظمة Linux / macOS:
```bash
cd android
chmod +x gradlew
./gradlew assembleDebug
```

### على نظام Windows (Command Prompt أو PowerShell):
```cmd
cd android
gradlew.bat assembleDebug
```

### مسار ملف الـ APK الناتج:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

لتثبيته مباشرة على الهاتف المتصل عبر USB (مع تفعيل خيارات المطور و USB Debugging):
```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

---

## الطريقة 3: بناء الـ APK سحابياً عبر GitHub Actions (بدون تثبيت أي برامج على حاسوبك)

إذا لم يكن لديك Android Studio أو Java على جهازك، يمكنك إنشاء مستودع (Repository) على GitHub ووضع ملف العمل هذا:

ملف: `.github/workflows/build-apk.yml`
```yaml
name: Build Android APK
on: [push, workflow_dispatch]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x android/gradlew
      - name: Build Debug APK
        run: cd android && ./gradlew assembleDebug
      - name: Upload APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: waynha-app-debug
          path: android/app/build/outputs/apk/debug/app-debug.apk
```
سيقوم GitHub ببناء ملف الـ APK تلقائياً في السحابة وتوفير رابط مباشر لتحميله وتثبيته على أي هاتف أندرويد.

---

## مميزات هذا المشروع الأصلي:
1. **دعم اللغة العربية والإنجليزية**: واجهة عربية أصيلة مع زر تبديل اللغة.
2. **الموقع الجغرافي**: يدعم GPS للبحث عن أقرب الفنيين لموقعك، مع موقع افتراضي رئيسي (بغداد، العراق) يشمل كافة المحافظات والمناطق.
3. **الاتصال والخرائط**: اتجاهات Google Maps بروابط `geo:` ومكالمات هاتفية مباشرة بنقرة واحدة.
4. **شامل ومكتمل**: لا ينقصه أي ملف تصريح أو مورد أو حزمة Kotlin.
