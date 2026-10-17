# 📱 Android App Documentation: AlarmNotification

---

### 🧾 General Information
- **Project name:** AlarmNotification
- **Author(s):** Zeev Fraiman
- **Date:** February 2025
- **Language:** Java 8
- **Development environment:** Android Studio
- **Android version:** minSdk 26 (Android 8.0) / targetSdk 34 (Android 14)

---

### 🎯 Project Goal
- **Problem solved:** Provides a reliable mechanism for scheduling time-based local notifications and alarms that fire accurately even when the application is minimized, inactive, or closed.
- **Why this problem is important:** Android aggressively manages background processes to save battery. Using standard background threads or timers fails when the app is killed. Leveraging system-level `AlarmManager` and `NotificationManager` ensures guaranteed event delivery.
- **Target audience:** Users needing an efficient, lightweight local notification and alarm manager for reminders.

---

### 📌 App Requirements

**Functional requirements:**
1. Schedule a broadcast alarm after a specified delay in seconds.
2. Select a specific date (via `DatePickerDialog`) and time (via `TimePickerDialog`) to set a exact alarm.
3. Immediately trigger and display a system notification.
4. Launch a modular `DatePickerFragment` (`DialogFragment`) for date selection.
5. Handle system notification channel creation and notification posting with action intents.

**Non-functional requirements:**
1. **Performance:** Lightweight application with minimal CPU and memory footprint; zero battery drain while waiting for scheduled alarms (delegated to system `AlarmManager`).
2. **Usability:** Simple, clean single-screen interface with instant visual feedback via `Toast` and `TextView`.
3. **Reliability:** Compliant with modern Android safety and permission standards:
   - Notification Channels for Android 8.0+ (API 26+).
   - Safe exact alarm scheduling with `canScheduleExactAlarms()` for Android 12+ (API 31+).
   - Runtime `POST_NOTIFICATIONS` permission management for Android 13+ (API 33+).
   - Non-exported `BroadcastReceiver` (`android:exported="false"`) for security.

---

### 🧠 Overall Architecture
- **Chosen approach:** Event-Driven Component Architecture (`Activity` + `Application` + `BroadcastReceiver` + `DialogFragment`).
- **Why this approach was chosen:** For a local notification and alarm application, Android native components interfacing directly with OS services (`AlarmManager`, `NotificationManager`) offer optimal lifecycle management, maximum battery efficiency, and zero unnecessary overhead.
- **Main system components:**
  - `NotificationChannel` (Application class): Initializes system notification channels.
  - `MainActivity` (UI Controller): Manages user inputs and permission requests.
  - `ReceiverAfterTime` (BroadcastReceiver): Handles system wakeup events and posts notifications.
  - `DatePickerFragment` (DialogFragment): Reusable fragment for date selection.

---

### 🧩 UML Diagram

```
+------------------------------------+
|        NotificationChannel         |
|         (Application Class)        |
+------------------------------------+
                  | (creates channel on startup)
                  v
+------------------------------------+          shows           +------------------------------------+
|            MainActivity            | -----------------------> |         DatePickerFragment         |
|           (UI Controller)          |                          |          (DialogFragment)          |
+------------------------------------+                          +------------------------------------+
                  |
                  | schedules via AlarmManager
                  v
+------------------------------------+
|          ReceiverAfterTime         |
|        (BroadcastReceiver)         |
+------------------------------------+
                  |
                  | posts notification
                  v
+------------------------------------+
|        NotificationManager         |
|         (Android OS Service)       |
+------------------------------------+
```

**Architecture & Package Explanation:**
- Separation of concern: The UI layer (`MainActivity`) is strictly separated from background event handling (`ReceiverAfterTime`).
- Component isolation prevents memory leaks and ensures scalability (e.g., adding a local database or additional notification channels).

---

### 🧩 Detailed Class Descriptions

**📌 Class: NotificationChannel**
- **Role:** Application subclass (`android.app.Application`).
- **Responsibility:** App startup initialization. Registers notification channels with the Android System (`NotificationManager`) on devices running Android 8.0 (API 26) or higher.
- **Main methods:**
  - `onCreate()` — Entry point called when the application process starts.
  - `createNotificationChannels()` — Configures channel ID (`CHANNEL_1_ID`), channel name, importance level (`IMPORTANCE_HIGH`), and description.
- **Interaction with other classes:** Provides the static constant `CHANNEL_1_ID` referenced by `ReceiverAfterTime`.

**📌 Class: MainActivity**
- **Role:** Main UI Activity (`AppCompatActivity`).
- **Responsibility:** Binds UI components, handles user clicks, validates input, requests runtime permissions, and schedules alarms via `AlarmManager`.
- **Main methods:**
  - `onCreate()` — Initializes layout, `Calendar` instance, view listeners, and checks runtime permissions.
  - `initElements()` — Locates views using `findViewById()`.
  - `checkPermissions()` — Requests `POST_NOTIFICATIONS` permission on Android 13+.
  - `scheduleAlarmAfterSeconds()` — Reads text input, validates numeric input, and sets a delayed alarm.
  - `choiceDate()` — Opens `DatePickerDialog` with min/max constraints without mutating the base calendar.
  - `choiceTime()` — Opens `TimePickerDialog` (using `Calendar.HOUR_OF_DAY`) and updates `tvWhere`.
  - `scheduleSelectedAlarm()` — Schedules exact alarm for the chosen date and time.
  - `triggerImmediateNotification()` — Triggers immediate alarm broadcast.
  - `setExactAlarm()` — Safely schedules exact/idle alarms considering Android 12+ capabilities.

**📌 Class: ReceiverAfterTime**
- **Role:** Broadcast Receiver (`android.content.BroadcastReceiver`).
- **Responsibility:** Intercepts alarm intents broadcasted by `AlarmManager` and displays system notifications.
- **Main methods:**
  - `onReceive()` — Invoked by the system when the alarm time is reached. Displays a Toast and calls notification builder.
  - `buildAndShowNotification()` — Constructs `NotificationCompat.Builder` with `PendingIntent` and posts it to `NotificationManager`.

**📌 Class: DatePickerFragment**
- **Role:** Dialog Fragment (`androidx.fragment.app.DialogFragment`).
- **Responsibility:** Reusable dialog fragment displaying a system date picker.
- **Main methods:**
  - `onCreateDialog()` — Instantiates `DatePickerDialog` with current date parameters.
  - `onDateSet()` — Handles the selected date callback.

---

### 🔄 App Workflow Diagram

```
[ User Action ]
       │
       ├──────> [ Input Seconds ] ──> Validate > 0 ──> AlarmManager.setExact() ──> [ Wait ] ──> BroadcastReceiver ──> Notification
       │
       ├──────> [ Select Date & Time ] ──> DatePickerDialog ──> TimePickerDialog ──> Update Calendar ──> Set Alarm ──> Notification
       │
       └──────> [ Immediate Notification ] ──> AlarmManager (Instant) ──> BroadcastReceiver ──> Notification
```

---

### 🎨 UI/UX Analysis
- **Design approach:** Clean, functional single-screen vertical layout using `LinearLayout`.
- **Principles applied:**
  - **Simplicity:** Clear button labels indicating specific actions.
  - **Feedback:** Immediate feedback provided through `Toast` messages and `TextView` status updates.
  - **Error Prevention:** Input validation prevents crashes on invalid or empty text inputs.
- **Future Improvements:**
  - Transition to Material Design 3 components (`MaterialDatePicker`, `MaterialTimePicker`).
  - Adaptive layout adjustments for dark theme and various screen resolutions.

---

### ⚙️ Working with Threads
- **UI Thread (Main Thread):** All user interactions, dialog presentations, and view updates occur on the main thread.
- **Background Operations:** Alarm timing is delegated to the Android OS kernel via `AlarmManager`. The app process does not need to stay alive in the background, preventing CPU consumption and battery drain.
- **ANR & Memory Leak Prevention:**
  - No blocking operations or heavy loops on the UI thread.
  - No static references to `Activity` context.
  - PendingIntents use `FLAG_IMMUTABLE` for system security and lifecycle independence.

---

### 💾 Working with Data
- **Data storage:** In-memory state maintained within `Calendar` instance during activity lifecycle.
- **Why this method was chosen:** The app targets real-time event triggering rather than persistent storage.
- **Data Integrity:** `Calendar` instance is safely initialized on startup, cloned when calculating date constraints (`setMaxDate`), and validated before scheduling alarms.

---

### 🌐 Working with the Network
- **Network usage:** None. The application operates 100% offline. Local notifications do not require internet access, ensuring full privacy and zero bandwidth usage.

---

### 🔐 Security
- **Receiver Export Control:** `ReceiverAfterTime` is declared with `android:exported="false"` in `AndroidManifest.xml` to prevent unauthorized external apps from triggering notifications.
- **PendingIntent Security:** `PendingIntent` objects utilize `PendingIntent.FLAG_IMMUTABLE` to prevent intent modification or hijacking.
- **Minimal Permissions:** Only strictly required permissions (`POST_NOTIFICATIONS`, `USE_EXACT_ALARM`, `SCHEDULE_EXACT_ALARM`) are declared.

---

### 🧪 Testing
- **Unit Tests (`ExampleUnitTest`):** Validates date/time parsing and basic logic calculations.
- **Instrumentation Tests (`ExampleInstrumentedTest`):** Verifies application context launching on an Android emulator or device.
- **Manual QA:** Verified input validation, permission dialogs, date/time dialog cancellation, and notification delivery across Android 8.0 through Android 14.

---

### 🐞 Error Handling
- **Empty / Non-numeric Input:** Validated in `scheduleAlarmAfterSeconds()` with `try-catch (NumberFormatException)` and `Toast` feedback.
- **Permission Denied:** Checked dynamically before posting notifications (`checkSelfPermission`).
- **Exact Alarm Restrictions:** Uses `alarmManager.canScheduleExactAlarms()` on API 31+ with safe fallback to `setAndAllowWhileIdle()`.

---

### ⚡ Performance
- **Resource Usage:** Minimal APK size and memory footprint (< 15MB RAM active usage).
- **Battery Optimization:** Relies on system alarm queue (`RTC_WAKEUP`), allowing the device to sleep deep until the alarm trigger time.

---

### 🚀 Extension Possibilities
1. **Boot Receiver (`RECEIVE_BOOT_COMPLETED`):** Reschedule active alarms after device restart using SQLite/Room database persistence.
2. **Custom Notification Sounds & Vibration:** Allow users to choose custom sound assets (`R.raw.*`) or vibration patterns.
3. **Repeating Alarms:** Support daily, weekly, or custom interval recurring alarms.
4. **Jetpack Compose UI:** Modernize the user interface using Jetpack Compose and Material 3 design guidelines.
