# תיאור מפורט של המחלקה: MainActivity

---

### 1. מידע כללי
- **שם המחלקה:** `MainActivity`
- **סוג:** Activity (`androidx.appcompat.app.AppCompatActivity`)
- **תפקיד המחלקה באפליקציה:** `MainActivity` מהווה את המסך הראשי של ממשק המשתמש (UI) באפליקציית AlarmNotification. המחלקה אחראית על אינטראקציה עם המשתמש, קבלת קלט תאריך/שעה או מספר שניות, אימות נתונים, בקשת הרשאות מנהל המערכת ותזמון התראות מול שירות `AlarmManager` של אנדרואיד.
- **אינטראקציה עם רכיבים אחרים:**
  - מקושרת לקובץ ה-XML `R.layout.activity_main`.
  - מתקשרת עם `ReceiverAfterTime` על ידי יצירת אובייקטי `Intent` ו-`PendingIntent`.
  - מציגה את `DatePickerFragment` (`DialogFragment`) בעת לחיצה על הלחצן המתאים.
  - משתמשת בקבוע `NotificationChannel.CHANNEL_1_ID` בעת הפעלת התראות.

---

### 2. משתנים (שדות המחלקה)

| שם | סוג | תפקיד | היכן בשימוש |
|-----|-----|-------|--------------|
| `context` | `Context` | שומר את ההקשר של הסביבה עבור ה-Activity. | בשימוש ב-`Toast`, יצירת `Intent` וגישה לשירותי מערכת. |
| `tvWhere` | `TextView` | מציג את המחרוזת המעוצבת של התאריך והשעה שנבחרו. | מתעדכן במתודה `choiceTime()`. |
| `bWhere` | `Button` | לחצן להפעלת דיאלוגים לבחירת תאריך ושעה. | מאותחל ב-`initElements()`, מאזין מוגדר ב-`onCreate()`. |
| `bSetAlarm` | `Button` | לחצן לקביעת התראה לתאריך ולשעה שנבחרו ביומן. | מאותחל ב-`initElements()`, מאזין מוגדר ב-`onCreate()`. |
| `bAddTimeAlarm` | `Button` | לחצן לקביעת התראה כעבור מספר שניות מוגדר. | מאותחל ב-`initElements()`, מאזין מוגדר ב-`onCreate()`. |
| `bNow` | `Button` | לחצן להפעלת התראה מיידית. | מאותחל ב-`initElements()`, מאזין מוגדר ב-`onCreate()`. |
| `bDateFragment` | `Button` | לחצן להצגת `DatePickerFragment`. | מאותחל ב-`initElements()`, מאזין מוגדר ב-`onCreate()`. |
| `etTimeAfter` | `EditText` | שדה טקסט שבו המשתמש מזין את מספר השניות. | נקרא במתודה `scheduleAlarmAfterSeconds()`. |
| `calendar` | `Calendar` | שומר את התאריך והשעה המדויקים שנבחרו להתראה. | מאותחל ב-`onCreate()`, מתעדכן ב-`choiceDate()` וב-`choiceTime()`. |
| `PERMISSION_REQUEST_CODE` | `int` | קבוע שלם ייחודי (`100`) לזיהוי בקשת ההרשאה. | בשימוש ב-`checkPermissions()` וב-`onRequestPermissionsResult()`. |

---

### 3. מתודות המחלקה

#### שם המתודה: `onCreate(Bundle savedInstanceState)`
- **סוג המתודה:** `protected`
- **ערך מוחזר:** `void` (אינו מחזיר ערך)
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `savedInstanceState` | `Bundle` | מכיל נתונים שמורים של מצב ה-Activity אם היא נפתחת מחדש. |

- **מה המתודה עושה (בפירוט):**
  1. קוראת ל-`super.onCreate(savedInstanceState)` לאתחול בסיסי.
  2. מגדירה את פריסת ה-UI באמצעות `setContentView(R.layout.activity_main)`.
  3. מאתחלת את `context = this` ואת `calendar = Calendar.getInstance()`.
  4. קוראת ל-`initElements()` לקישור אלמנטי ה-XML למשתני Java.
  5. קוראת ל-`checkPermissions()` לבדיקת הרשאות התראה.
  6. מגדירה מאזינים לכל הלחצנים באמצעות ביטויי למבדה.
- **מתי היא נקראת:** באופן אוטומטי על ידי מערכת אנדרואיד בעת יצירת ה-Activity.

---

#### שם המתודה: `initElements()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** משתמשת ב-`findViewById()` כדי לאתר כל רכיב ממשק בקובץ `activity_main.xml` ומקשרת אותו לשדות המחלקה.
- **מתי היא נקראת:** פעם אחת בתוך `onCreate()`.

---

#### שם המתודה: `checkPermissions()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** בודקת אם המכשיר מריץ Android 13 (API 33) ומעלה. אם כן, בודקת אם הרשאת `POST_NOTIFICATIONS` אושרה, ואם לא - מבקשת אותה מהמשתמש.

---

#### שם המתודה: `scheduleAlarmAfterSeconds()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** קוראת את הטקסט מ-`etTimeAfter`, בודקת תקינות מספר שניות חיובי, מחשבת את זמן היעד במילישניות, ומבצעת תזמון ב-`setExactAlarm()`.
- **מתי היא נקראת:** בעת לחיצה על הלחצן `bAddTimeAlarm`.

---

#### שם המתודה: `scheduleSelectedAlarm()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** יוצרת `PendingIntent` עבור `ReceiverAfterTime.class` בזמן `calendar.getTimeInMillis()` ומעבירה אותו ל-`setExactAlarm()`.
- **מתי היא נקראת:** בעת לחיצה על הלחצן `bSetAlarm`.

---

#### שם המתודה: `triggerImmediateNotification()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** מתזמנת התראה ל-`System.currentTimeMillis()`, מה שגורם להפעלה מיידית של `ReceiverAfterTime`.
- **מתי היא נקראת:** בעת לחיצה על הלחצן `bNow`.

---

#### שם המתודה: `setExactAlarm(long triggerAtMillis, PendingIntent pendingIntent)`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `triggerAtMillis` | `long` | זמן היעד המדויק במילישניות. |
| `pendingIntent` | `PendingIntent` | אובייקט ה-Intent שמודרך להשלח בזמן ההתראה. |

- **מה המתודה עושה:** מקבלת את שירות `AlarmManager`. ב-Android 12+ בודקת `canScheduleExactAlarms()`. אם מותר, קוראת ל-`setExactAndAllowWhileIdle()`, ואחרת ל-`setAndAllowWhileIdle()`.

---

#### שם המתודה: `choiceDate()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** מציגה `DatePickerDialog`. משכפלת את אובייקט ה-`calendar` לחישוב תאריך מקסימלי ללא שינוי התאריך שנבחר. לאחר בחירת תאריך, מעדכנת את `calendar` וקוראת ל-`choiceTime()`.

---

#### שם המתודה: `choiceTime()`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:** ללא
- **מה המתודה עושה:** מציגה `TimePickerDialog`. מגדירה `Calendar.HOUR_OF_DAY` ו-`Calendar.MINUTE` ומציגה מחרוזת מעודכנת ב-`tvWhere`.

---

#### שם המתודה: `onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)`
- **סוג המתודה:** `public`
- **ערך מוחזר:** `void`
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `requestCode` | `int` | קוד בקשת ההרשאה (`100`). |
| `permissions` | `String[]` | מערך ההרשאות שבוקשו. |
| `grantResults` | `int[]` | מערך תוצאות תשובת המשתמש. |

- **מה המתודה עושה:** בודקת אם הרשאת ההתראות אושרה ומציגה הודעת `Toast`.

---

### 4. מחזור חיים (Activity)
- `onCreate()`: נקראת בעת הפעלת ה-Activity.
- `onStart()` / `onResume()`: ה-Activity הופכת לפעילה וגלויה.
- `onPause()` / `onStop()`: ה-Activity עוברת לרקע.
- `onDestroy()`: ה-Activity מושמדת מהזיכרון.

---

### 5. אינטראקציה עם הממשק (UI)
- רכיבי UI: `EditText` (`etTimeAfter`), `TextView` (`tvWhere`), לחצני `Button`.
- קישור באמצעות `findViewById()`.
- טיפול באירועים באמצעות מאזיני למבדה (`setOnClickListener`).

---

### 6. אינטראקציה עם רכיבים אחרים
- **AlarmManager:** שירות מערכת לתזמון שעונים מעוררים.
- **ReceiverAfterTime:** מקבל השידורים מהשעון המעורר.
- **DatePickerFragment:** נקרא כפרגמנט דיאלוג.

---

### 7. לוגיקה כללית ותרחיש שימוש
**תרחיש שימוש:**
1. המשתמש פותח את האפליקציה.
2. לוחץ על "Select date and time" (`bWhere`).
3. בוחר תאריך ושעה בדיאלוגים.
4. לוחץ על "Set Alarm" (`bSetAlarm`).
5. `MainActivity` רושמת את השעון המעורר ב-`AlarmManager`.
6. בזמן שנבחר `ReceiverAfterTime` מקבל את הסיגנל ומציג התראה.

---

### 8. הסבר במילים פשוטות
דמיינו את `MainActivity` כ**דלפק קבלה במלון**:
- פקיד הקבלה מברך אותך בכניסה (`onCreate`).
- על הדלפק יש לחצנים שונים לבקשות (להעיר אותך בעוד 10 שניות, לבחור תאריך בלוח שנה, או להשמיע פעמון מיד).
- כשתגיד לפקיד מתי תרצה להתעורר, הוא ירשום זאת ויעביר את ההודעה ל**מערכת השעונים האוטומטית של המלון** (`AlarmManager`). גם אם הפקיד הולך הביתה, המערכת האוטומטית תעיר אותך בזמן!
