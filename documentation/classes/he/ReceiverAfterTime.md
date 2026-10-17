# תיאור מפורט של המחלקה: ReceiverAfterTime

---

### 1. מידע כללי
- **שם המחלקה:** `ReceiverAfterTime`
- **סוג:** מקבל שידורים (`android.content.BroadcastReceiver`)
- **תפקיד המחלקה באפליקציה:** `ReceiverAfterTime` משמשת כמקבל אירועי רקע. כשהשעון המעורר שתוזמן ב-`AlarmManager` מגיע לזמנו, אנדרואיד מעירה את ה-Receiver הזה. הוא לוכד את הסיגנל ומציג התראת מערכת בולטת עם לחצני פעולה.
- **אינטראקציה עם רכיבים אחרים:**
  - מוגדרת ב-`AndroidManifest.xml`.
  - מופעלת על ידי `AlarmManager` דרך `PendingIntent`.
  - משתמשת ב-`NotificationChannel.CHANNEL_1_ID` לשליחת התראות.
  - פותחת את `MainActivity` בעת לחיצה על ההתראה.

---

### 2. משתנים (שדות המחלקה)
- המחלקה אינה מכילה שדות (Receiver ללא מצב).

---

### 3. מתודות המחלקה

#### שם המתודה: `onReceive(Context context, Intent intent)`
- **סוג המתודה:** `public`
- **ערך מוחזר:** `void`
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `context` | `Context` | הקשר הריצה של ה-Receiver. |
| `intent` | `Intent` | ה-Intent הנכנס מ-`AlarmManager`. |

- **מה המתודה עושה:** מציגה הודעת `Toast` ("Alarm triggered!") וקוראת ל-`buildAndShowNotification(context)`.

---

#### שם המתודה: `buildAndShowNotification(Context context)`
- **סוג המתודה:** `private`
- **ערך מוחזר:** `void`
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `context` | `Context` | הקשר ליצירת ושליחת ההתראה. |

- **מה המתודה עושה:** מכינה את כותרת ותוכן ההתראה, יוצרת `Intent` לפתיחת `MainActivity`, בונה התראה באמצעות `NotificationCompat.Builder`, בודקת הרשאות ושולחת ב-`NotificationManager`.

---

### 4. מחזור חיים (BroadcastReceiver)
- `onReceive()` נקראת בעת קבלת השידור. לאחר סיומה, האובייקט מושמד על ידי המערכת.

---

### 5. אינטראקציה עם הממשק (UI)
- מציגה `Toast` והתראה קופצת בסרגל המצב של המכשיר.

---

### 6. אינטראקציה עם רכיבים אחרים
- נקראת מ-`AlarmManager`.
- מעבירה התראה ל-`NotificationManager`.
- פותחת את `MainActivity` בלחיצה.

---

### 7. לוגיקה כללית ותרחיש שימוש
1. מגיע זמן השעון המעורר.
2. המערכת מעירה את `ReceiverAfterTime`.
3. `onReceive()` בונה ושולחת התראה.
4. ההתראה מופיעה על המסך עם צליל/רטט.
5. המשתמש לוחץ על ההתראה ועובר ל-`MainActivity`.

---

### 8. הסבר במילים פשוטות
דמיינו את `ReceiverAfterTime` כ**צלצלן במגדל השעון**:
- הצלצלן ישן בשקט עד שהשעון המעורר מעיר אותו (`AlarmManager`).
- ברגע שהוא מתעורר (`onReceive`), הוא מצלצל בפעמון החזק (מציג התראה על המסך) ומוסר לך פתק המאפשר לפתוח את האפליקציה הראשית (`MainActivity`).
- משהושלמה המשימה, הצלצלן חוזר לנוח!
