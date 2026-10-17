# Detailed Class Description: MainActivity

---

### 1. General Information
- **Class Name:** `MainActivity`
- **Type:** Activity (`androidx.appcompat.app.AppCompatActivity`)
- **Role in the Application:** `MainActivity` serves as the primary User Interface (UI) screen of the AlarmNotification app. It is responsible for interacting with the user, taking date/time or seconds inputs, validating inputs, requesting necessary runtime permissions, and interacting with the system's `AlarmManager` service to schedule notifications.
- **Interaction with Other Components:**
  - Connects to `R.layout.activity_main` for its visual design.
  - Interacts with `ReceiverAfterTime` by creating `Intent` and `PendingIntent` instances aimed at this BroadcastReceiver.
  - Displays `DatePickerFragment` (`DialogFragment`) when the user taps the fragment button.
  - Uses `NotificationChannel.CHANNEL_1_ID` implicitly when triggering notifications.

---

### 2. Variables (Class Fields)

| Name | Type | Purpose | Where it is used |
|------|------|---------|-------------------|
| `context` | `Context` | Holds the environment context of the activity. | Used in `Toast` displays, `Intent` creations, and system service access. |
| `tvWhere` | `TextView` | Displays the formatted date and time string selected by the user. | Updated in `choiceTime()`. |
| `bWhere` | `Button` | Button to initiate date and time selection dialogs. | Bound in `initElements()`, listener set in `onCreate()`. |
| `bSetAlarm` | `Button` | Button to schedule an alarm for the selected calendar date/time. | Bound in `initElements()`, listener set in `onCreate()`. |
| `bAddTimeAlarm` | `Button` | Button to schedule an alarm after a user-defined number of seconds. | Bound in `initElements()`, listener set in `onCreate()`. |
| `bNow` | `Button` | Button to trigger an immediate notification. | Bound in `initElements()`, listener set in `onCreate()`. |
| `bDateFragment` | `Button` | Button to show the `DatePickerFragment`. | Bound in `initElements()`, listener set in `onCreate()`. |
| `etTimeAfter` | `EditText` | Text input box where the user enters the number of seconds. | Read in `scheduleAlarmAfterSeconds()`. |
| `calendar` | `Calendar` | Stores the exact date and time chosen for the alarm. | Initialized in `onCreate()`, updated in `choiceDate()` and `choiceTime()`. |
| `PERMISSION_REQUEST_CODE` | `int` | Unique integer constant (`100`) identifying the runtime permission request. | Used in `checkPermissions()` and `onRequestPermissionsResult()`. |

---

### 3. Class Methods

#### Method Name: `onCreate(Bundle savedInstanceState)`
- **Method Type:** `protected`
- **Return Value:** `void` (returns nothing)
- **Parameters:**

| Name | Type | Description |
|------|------|--------------|
| `savedInstanceState` | `Bundle` | Contains saved activity state data if reopened after configuration change. |

- **What the method does (in detail):**
  1. Calls `super.onCreate(savedInstanceState)` to perform standard Activity initialization.
  2. Sets the UI view layout using `setContentView(R.layout.activity_main)`.
  3. Initializes `context = this` and `calendar = Calendar.getInstance()`.
  4. Calls `initElements()` to bind all XML views to Java variables.
  5. Calls `checkPermissions()` to request notification permissions if needed.
  6. Sets click listeners on all buttons using lambda expressions.
- **When it is called:** Called automatically by the Android OS when the Activity is created.
- **What is important to understand:** Initializing `calendar` here guarantees that it is never `null`, preventing `NullPointerException` crashes.

---

#### Method Name: `initElements()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Uses `findViewById()` to locate each UI element in `activity_main.xml` and assigns them to class fields (`tvWhere`, `bWhere`, `bSetAlarm`, `bAddTimeAlarm`, `bNow`, `bDateFragment`, `etTimeAfter`).
- **When it is called:** Called once inside `onCreate()`.
- **What is important to understand:** Must be called after `setContentView()` so that view IDs can be located.

---

#### Method Name: `checkPermissions()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Checks if the device runs Android 13 (API level 33) or higher (`Build.VERSION_CODES.TIRAMISU`). If so, checks if `POST_NOTIFICATIONS` permission is granted. If not, prompts the user with a permission dialog.
- **When it is called:** Called in `onCreate()`.
- **What is important to understand:** Required for Android 13+ compliance; without runtime permission, system notifications are blocked.

---

#### Method Name: `scheduleAlarmAfterSeconds()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:**
  1. Reads string from `etTimeAfter`.
  2. Validates that the input is non-empty and a positive integer using `Integer.parseInt()`.
  3. Calculates target time in milliseconds: `System.currentTimeMillis() + (seconds * 1000L)`.
  4. Creates an `Intent` pointing to `ReceiverAfterTime.class`.
  5. Wraps the intent in a `PendingIntent.getBroadcast()`.
  6. Calls `setExactAlarm()` to schedule the alarm.
- **When it is called:** When `bAddTimeAlarm` is clicked.
- **What is important to understand:** Uses a `try-catch` block for `NumberFormatException` to prevent crashes when input is invalid or empty.

---

#### Method Name: `scheduleSelectedAlarm()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Creates a `PendingIntent` for `ReceiverAfterTime.class` using `calendar.getTimeInMillis()` as the trigger time and passes it to `setExactAlarm()`.
- **When it is called:** When `bSetAlarm` is clicked.
- **What is important to understand:** Formats and displays a Toast confirmation message with the scheduled time.

---

#### Method Name: `triggerImmediateNotification()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Schedules an alarm for `System.currentTimeMillis()`, causing `ReceiverAfterTime` to trigger immediately.
- **When it is called:** When `bNow` is clicked.

---

#### Method Name: `setExactAlarm(long triggerAtMillis, PendingIntent pendingIntent)`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:**

| Name | Type | Description |
|------|------|--------------|
| `triggerAtMillis` | `long` | Exact target time in epoch milliseconds. |
| `pendingIntent` | `PendingIntent` | Wrapped intent to broadcast when the alarm fires. |

- **What the method does:** Retrieves `AlarmManager` from system services. On Android 12+ (API 31+), checks `canScheduleExactAlarms()`. If granted, calls `setExactAndAllowWhileIdle()`; otherwise falls back to `setAndAllowWhileIdle()`.
- **When it is called:** Called internally by scheduling methods.
- **What is important to understand:** Prevents `SecurityException` crashes on Android 12+.

---

#### Method Name: `choiceDate()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Displays a `DatePickerDialog`. Clones `calendar` to calculate `setMaxDate()` (1 year ahead) without modifying the selected calendar instance. On date selection, updates `calendar` fields and calls `choiceTime()`.
- **When it is called:** When `bWhere` is clicked.

---

#### Method Name: `choiceTime()`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:** None
- **What the method does:** Displays a `TimePickerDialog`. Sets `Calendar.HOUR_OF_DAY` and `Calendar.MINUTE` on `calendar` and formats the string into `tvWhere`.
- **When it is called:** Called automatically after a date is selected in `choiceDate()`.

---

#### Method Name: `onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)`
- **Method Type:** `public`
- **Return Value:** `void`
- **Parameters:**

| Name | Type | Description |
|------|------|--------------|
| `requestCode` | `int` | Integer code sent with permission request (`100`). |
| `permissions` | `String[]` | Array of requested permissions. |
| `grantResults` | `int[]` | Array of results (`PERMISSION_GRANTED` or `PERMISSION_DENIED`). |

- **What the method does:** Checks if `PERMISSION_REQUEST_CODE` was granted and shows a Toast confirmation.
- **When it is called:** Automatically invoked by Android OS after user answers the permission prompt.

---

### 4. Lifecycle (Activity)
- `onCreate()`: Called when Activity is launched. Sets layout, initializes fields, binds views, checks permissions, and registers button listeners.
- `onStart()` / `onResume()`: Activity becomes visible and interactive.
- `onPause()` / `onStop()`: Activity moves to background.
- `onDestroy()`: Activity is destroyed and memory is freed.

---

### 5. Interface Interaction (UI)
- Connected via `setContentView(R.layout.activity_main)` and `findViewById()`.
- UI controls: `EditText` (`etTimeAfter`), `TextView` (`tvWhere`), `Button`s (`bAddTimeAlarm`, `bWhere`, `bSetAlarm`, `bNow`, `bDateFragment`).
- Event handling via lambda click listeners (`setOnClickListener`).

---

### 6. Interaction with Other Components
- **System AlarmManager:** Schedules pending intents to wake up CPU and broadcast events.
- **ReceiverAfterTime:** Target receiver for alarm broadcasts.
- **DatePickerFragment:** Launched as a DialogFragment.

---

### 7. Overall Class Logic & Use Case
**Use Case Scenario:**
1. User opens the app.
2. Taps "Select date and time" (`bWhere`).
3. Picks date and time in the pop-up dialogs.
4. Taps "Set Alarm" (`bSetAlarm`).
5. `MainActivity` schedules the alarm with `AlarmManager`.
6. At the selected time, `ReceiverAfterTime` receives the broadcast and displays a notification.

---

### 8. Explanation in Simple Words
Imagine `MainActivity` as the **reception desk of a hotel**:
- It greets the guest (user) when they walk in (`onCreate`).
- It has buttons on the counter for different requests (set alarm in 10 seconds, pick a date/time on a calendar, or ring a bell now).
- When you tell the reception desk when you want to wake up, it writes down your request and passes it to the hotel's **automated alarm system** (`AlarmManager`). Even if the receptionist goes home, the automated alarm system will wake you up!
