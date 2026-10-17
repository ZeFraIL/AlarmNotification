# وصف تفصيلي للفئة: MainActivity

---

### 1. معلومات عامة
- **اسم الفئة:** `MainActivity`
- **النوع:** نشاط (`androidx.appcompat.app.AppCompatActivity`)
- **دور الفئة في التطبيق:** تعتبر `MainActivity` الشاشة الرئيسية لواجهة المستخدم (UI) في تطبيق AlarmNotification. وهي مسؤولة عن التفاعل مع المستخدم، واستقبال إدخالات التاريخ/الوقت أو عدد الثواني، والتحقق من صحة البيانات، وطلب الأذونات الضرورية، والتفاعل مع خدمة `AlarmManager` الخاصة بالنظام لجدولة التنبيهات.
- **التفاعل مع المكونات الأخرى:**
  - ترتبط بملف الواجهة `R.layout.activity_main`.
  - تتفاعل مع `ReceiverAfterTime` عبر إنشاء كائنات `Intent` و `PendingIntent`.
  - تعرض `DatePickerFragment` (`DialogFragment`) عند النقر على الزر المخصص.
  - تستخدم الثابت `NotificationChannel.CHANNEL_1_ID` عند تشغيل الإشعارات.

---

### 2. المتغيرات (حقول الفئة)

| الاسم | النوع | الغرض | أين تُستخدم |
|-------|-------|-------|-------------|
| `context` | `Context` | يحفظ سياق البيئة الخاص بالنشاط. | يُستخدم في رسائل `Toast` وإعادة إنشاء `Intent` والوصول لخدمات النظام. |
| `tvWhere` | `TextView` | يعرض نص التاريخ والوقت المحددين بشكل منسق. | يتم تحديثه في الدالة `choiceTime()`. |
| `bWhere` | `Button` | زر لفتح نوافذ اختيار التاريخ والوقت. | يتم ربطه في `initElements()` وتحديد المستمع في `onCreate()`. |
| `bSetAlarm` | `Button` | زر لضبط المنبه للتاريخ والوقت المحددين في التقويم. | يتم ربطه في `initElements()` وتحديد المستمع في `onCreate()`. |
| `bAddTimeAlarm` | `Button` | زر لضبط المنبه بعد عدد ثوانٍ محدد. | يتم ربطه في `initElements()` وتحديد المستمع في `onCreate()`. |
| `bNow` | `Button` | زر لإطلاق إشعار فوري. | يتم ربطه في `initElements()` وتحديد المستمع في `onCreate()`. |
| `bDateFragment` | `Button` | زر لعرض `DatePickerFragment`. | يتم ربطه في `initElements()` وتحديد المستمع في `onCreate()`. |
| `etTimeAfter` | `EditText` | حقل نصي يدخل فيه المستخدم عدد الثواني. | تُقرأ قيمته في `scheduleAlarmAfterSeconds()`. |
| `calendar` | `Calendar` | يخزن التاريخ والوقت المحددين بدقة للمنبه. | يتم تهيئته في `onCreate()` وتحديثه في `choiceDate()` و `choiceTime()`. |
| `PERMISSION_REQUEST_CODE` | `int` | ثابت عددي فريد (`100`) للتعرف على طلب الإذن. | يُستخدم في `checkPermissions()` و `onRequestPermissionsResult()`. |

---

### 3. دوال الفئة (Methods)

#### اسم الدالة: `onCreate(Bundle savedInstanceState)`
- **نوع الدالة:** `protected`
- **القيمة المرجعة:** `void` (لا ترجع شيئًا)
- **المعاملات:**

| الاسم | النوع | الوصف |
|-------|-------|-------|
| `savedInstanceState` | `Bundle` | يحتوي على بيانات حالة النشاط المحفوظة عند إعادة الفتح. |

- **ماذا تفعل الدالة (بالتفصيل):**
  1. تستدعي `super.onCreate(savedInstanceState)` للتهيئة الأساسية.
  2. تحدد تخطيط الواجهة باستخدام `setContentView(R.layout.activity_main)`.
  3. تهيئ `context = this` و `calendar = Calendar.getInstance()`.
  4. تستدعي `initElements()` لربط عناصر XML بمتغيرات جافا.
  5. تستدعي `checkPermissions()` للتحقق من أذونات الإشعارات.
  6. تضبط مستمعي النقرات للأزرار باستخدام تعابير لامبدا.
- **متى تُستدعى:** تلقائيًا بواسطة نظام أندرويد عند إنشاء النشاط.

---

#### اسم الدالة: `initElements()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تستخدم `findViewById()` للبحث عن كل عنصر واجهة في `activity_main.xml` وربطه بحقل الفئة المناسب.

---

#### اسم الدالة: `checkPermissions()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تتحقق مما إذا كان الجهاز يعمل بنظام Android 13 (API 33) أو أعلى. إذا كان كذلك، تتحقق من إذن `POST_NOTIFICATIONS` وتطلبه من المستخدم إذا لم يكن ممنوحًا.

---

#### اسم الدالة: `scheduleAlarmAfterSeconds()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تقرأ النص من `etTimeAfter` وتتحقق من صحته كعدد موجب، ثم تحسب الوقت المستهدف بالملي ثانية وتستدعي `setExactAlarm()`.

---

#### اسم الدالة: `scheduleSelectedAlarm()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تنشئ `PendingIntent` لـ `ReceiverAfterTime.class` بالوقت المحدد في `calendar.getTimeInMillis()` وتمرره إلى `setExactAlarm()`.

---

#### اسم الدالة: `triggerImmediateNotification()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تضبط المنبه على الوقت الحالي `System.currentTimeMillis()` مما يؤدي لتشغيل الإشعار فورًا.

---

#### اسم الدالة: `setExactAlarm(long triggerAtMillis, PendingIntent pendingIntent)`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:**

| الاسم | النوع | الوصف |
|-------|-------|-------|
| `triggerAtMillis` | `long` | الوقت المستهدف بالملي ثانية. |
| `pendingIntent` | `PendingIntent` | كائن النية المعلقة المرسل عند حلول الوقت. |

- **ماذا تفعل الدالة:** تحصل على خدمة `AlarmManager`. في Android 12+ تتحقق من `canScheduleExactAlarms()` وتضبط المنبه بأمان.

---

#### اسم الدالة: `choiceDate()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تعرض `DatePickerDialog`. تقوم باستنساخ كائن `calendar` لحساب الحد الأقصى للتاريخ دون تعديل التقويم المحدد، ثم تدعو `choiceTime()`.

---

#### اسم الدالة: `choiceTime()`
- **نوع الدالة:** `private`
- **القيمة المرجعة:** `void`
- **المعاملات:** لا يوجد
- **ماذا تفعل الدالة:** تعرض `TimePickerDialog`. تضبط `Calendar.HOUR_OF_DAY` و `Calendar.MINUTE` وتعرض النص المنسق في `tvWhere`.

---

#### اسم الدالة: `onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)`
- **نوع الدالة:** `public`
- **القيمة المرجعة:** `void`
- **المعاملات:**

| الاسم | النوع | الوصف |
|-------|-------|-------|
| `requestCode` | `int` | رمز طلب الإذن (`100`). |
| `permissions` | `String[]` | مصفوفة الأذونات المطلوبة. |
| `grantResults` | `int[]` | مصفوفة نتائج إجابة المستخدم. |

- **ماذا تفعل الدالة:** تتحقق من منح إذن الإشعارات وتعرض رسالة `Toast`.

---

### 4. دورة الحياة (Activity)
- `onCreate()`: تُستدعى عند بدء النشاط.
- `onStart()` / `onResume()`: يصبح النشاط مرئيًا وتفاعليًا.
- `onPause()` / `onStop()`: ينتقل النشاط إلى الخلفية.
- `onDestroy()`: يتم تدمير النشاط وتحرير الذاكرة.

---

### 5. التفاعل مع الواجهة (UI)
- عناصر الواجهة: `EditText` (`etTimeAfter`) و `TextView` (`tvWhere`) والأزرار `Button`.
- الربط عبر `findViewById()`.
- معالجة الأحداث عبر مستمعي لامبدا (`setOnClickListener`).

---

### 6. التفاعل مع المكونات الأخرى
- **AlarmManager:** خدمة النظام لجدولة المنبهات.
- **ReceiverAfterTime:** مستقبل البث الصادر عند حلول المنبه.
- **DatePickerFragment:** يستدعى كنافذة حوار.

---

### 7. المنطق العام وسيناريو الاستخدام
1. يفتح المستخدم التطبيق.
2. ينقر على "Select date and time" (`bWhere`).
3. يحدد التاريخ والوقت في النوافذ المنبثقة.
4. ينقر على "Set Alarm" (`bSetAlarm`).
5. يقوم `MainActivity` بجدولة المنبه لدى `AlarmManager`.
6. في الوقت المحدد، يستلم `ReceiverAfterTime` البث ويعرض الإشعار.

---

### 8. الشرح بكلمات بسيطة
تخيل `MainActivity` مثل **مكتب الاستقبال في فندق**:
- يرحب بك الموظف عند دخولك (`onCreate`).
- يحتوي المكتب على أزرار لطلبات مختلفة (إيقاظك بعد 10 ثوانٍ، اختيار تاريخ في التقويم، أو الرن فورًا).
- عندما تخبر الموظف بالوقت الذي تريد الاستيقاظ فيه، يسجل طلبك ويمرره إلى **نظام المنبه الآلي بالفندق** (`AlarmManager`). حتى لو ذهب الموظف للبيت، سيعمل المنبه الآلي ويوقظك في الوقت المحدد!
