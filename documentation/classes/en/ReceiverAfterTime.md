# Detailed Class Description: ReceiverAfterTime

---

### 1. General Information
- **Class Name:** `ReceiverAfterTime`
- **Type:** BroadcastReceiver (`android.content.BroadcastReceiver`)
- **Role in the Application:** `ReceiverAfterTime` acts as a background event listener. When an alarm scheduled by `AlarmManager` fires, Android wakes up this receiver. It is responsible for intercepting the broadcast event and displaying a heads-up system notification with action buttons.
- **Interaction with Other Components:**
  - Declared in `AndroidManifest.xml` as a `<receiver>`.
  - Triggered by `AlarmManager` via `PendingIntent`.
  - Uses `NotificationChannel.CHANNEL_1_ID` to post notifications.
  - Launches `MainActivity` when the user taps the notification or its action button.

---

### 2. Variables (Class Fields)
- This class contains no member fields (stateless BroadcastReceiver).

---

### 3. Class Methods

#### Method Name: `onReceive(Context context, Intent intent)`
- **Method Type:** `public`
- **Return Value:** `void`
- **Parameters:**

| Name | Type | Description |
|------|------|--------------|
| `context` | `Context` | The context in which the receiver is running. |
| `intent` | `Intent` | The Intent being received from `AlarmManager`. |

- **What the method does:**
  1. Shows a short `Toast` message ("Alarm triggered!").
  2. Calls `buildAndShowNotification(context)`.
- **When it is called:** Automatically invoked by Android OS when `AlarmManager` triggers the scheduled broadcast.
- **What is important to understand:** Must complete execution quickly (< 10 seconds), otherwise Android terminates it.

---

#### Method Name: `buildAndShowNotification(Context context)`
- **Method Type:** `private`
- **Return Value:** `void`
- **Parameters:**

| Name | Type | Description |
|------|------|--------------|
| `context` | `Context` | Application context used to build and post notification. |

- **What the method does:**
  1. Prepares notification title ("IMPORTANT!") and content message.
  2. Creates an `Intent` pointing to `MainActivity.class`.
  3. Wraps intent in `PendingIntent.getActivity()` with `FLAG_IMMUTABLE`.
  4. Builds a `NotificationCompat.Builder` using `CHANNEL_1_ID`, setting small icon, title, text, high priority, auto-cancel, red color, and action button ("Open App").
  5. Checks `POST_NOTIFICATIONS` permission on Android 13+.
  6. Obtains `NotificationManager` and calls `manager.notify(1, notification)`.
- **When it is called:** Called inside `onReceive()`.

---

### 4. Lifecycle (BroadcastReceiver)
- `onReceive()` is called when the broadcast intent arrives. Once `onReceive()` finishes, the receiver instance is disposed of by the system.

---

### 5. Interface Interaction (UI)
- Displays a `Toast` message and posts a heads-up Notification on the system status bar.

---

### 6. Interaction with Other Components
- Triggered by system `AlarmManager`.
- Posts to system `NotificationManager`.
- Opens `MainActivity` when clicked.

---

### 7. Overall Class Logic & Use Case
**Use Case Scenario:**
1. Scheduled alarm time arrives.
2. Android OS wakes up `ReceiverAfterTime`.
3. `onReceive()` executes and constructs notification.
4. Notification appears on screen with sound/vibration.
5. User taps notification to open `MainActivity`.

---

### 8. Explanation in Simple Words
Imagine `ReceiverAfterTime` as a **bell ringer in a clock tower**:
- The ringer sits quietly and sleeps until the alarm clock wakes him up (`AlarmManager`).
- As soon as he wakes up (`onReceive`), he rings the loud bell (displays notification on screen) and hands you a note allowing you to open the main app (`MainActivity`).
- Once the bell is rung, his job is done!
