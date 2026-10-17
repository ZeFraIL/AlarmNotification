# תיאור מפורט של המחלקה: DatePickerFragment

---

### 1. מידע כללי
- **שם המחלקה:** `DatePickerFragment`
- **סוג:** דיאלוג פרגמנט (`androidx.fragment.app.DialogFragment` המממש `DatePickerDialog.OnDateSetListener`)
- **תפקיד המחלקה באפליקציה:** `DatePickerFragment` הינה רכיב דיאלוג מודולרי המציג חלון קופץ לבחירת תאריך מתוך לוח שנה.
- **אינטראקציה עם רכיבים אחרים:**
  - מופעלת וממומשת מתוך `MainActivity` בעת לחיצה על `bDateFragment`.

---

### 2. משתנים (שדות המחלקה)
- המחלקה אינה מכילה שדות.

---

### 3. מתודות המחלקה

#### שם המתודה: `onCreateDialog(Bundle savedInstanceState)`
- **סוג המתודה:** `public`
- **ערך מוחזר:** `Dialog` (אובייקט `DatePickerDialog`)
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `savedInstanceState` | `Bundle` | מצב שמור של הדיאלוג. |

- **מה המתודה עושה:** מקבלת שנה, חודש ויום נוכחיים מ-`Calendar.getInstance()` ויוצרת ומחזירה `DatePickerDialog`.

---

#### שם המתודה: `onDateSet(DatePicker view, int year, int month, int dayOfMonth)`
- **סוג המתודה:** `public`
- **ערך מוחזר:** `void`
- **פרמטרים:**

| שם | סוג | תיאור |
|-----|-----|-------|
| `view` | `DatePicker` | תצוגת בחירת התאריך. |
| `year` | `int` | השנה שנבחרה. |
| `month` | `int` | החודש שנבחר (0-11). |
| `dayOfMonth` | `int` | היום שנבחר בחודש. |

- **מה המתודה עושה:** מציגה הודעת `Toast` עם התאריך שנבחר (`dayOfMonth/month+1/year`).

---

### 4. מחזור חיים (Fragment)
- `onCreateDialog()`: יוצרת את הדיאלוג. מושמדת בעת סגירת החלון הקופץ.

---

### 5. אינטראקציה עם הממשק (UI)
- מציגה חלון דיאלוג לבחירת תאריך.

---

### 6. אינטראקציה עם רכיבים אחרים
- מופעלת מ-`MainActivity`.

---

### 7. לוגיקה כללית ותרחיש שימוש
1. המשתמש לוחץ על "Select date in fragment".
2. `DatePickerFragment` פותחת לוח שנה.
3. המשתמש בוחר תאריך ולוחץ על OK.
4. נקראת `onDateSet()` ומוצגת הודעת `Toast`.

---

### 8. הסבר במילים פשוטות
דמיינו את `DatePickerFragment` כ**לוח שנה שולחני קופץ**:
- כשאתה מבקש לראות לוח שנה (`bDateFragment`), המחלקה מגישה לך חלון קופץ של לוח שנה המכוון להיום.
- אתה מצביע על תאריך, ולוח השנה מאשר את בחירתך!
