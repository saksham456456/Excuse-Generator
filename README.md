# Savage Excuse Generator 🕶️🔥

Savage Excuse Generator is a viral, highly interactive Android application designed to provide witty, relatable, and "savage" excuses for various global life situations. Whether you're dodging a school assignment, a strict boss, or just being "mentally unavailable" for a clingy ex, this app has you covered with over a thousand handcrafted comebacks.

## 🚀 The Massive Revamp Features

We completely rebuilt the application from the ground up to turn a simple text-generator into an engaging, viral powerhouse:

*   **1000+ Localized Excuses:** Over a thousand unique excuses spanning 9 massive modern categories (e.g., Introvert Problems, Toxic Savage, Strict Boss, Tech Issues). All powered by a dynamic Python generation script that builds into a robust local JSON payload for offline usage.
*   **Dynamic Personalization:** The app learns your Name and Persona during onboarding, injecting your identity directly into the excuses (e.g., *"Sorry, [Name] is currently mentally unavailable"*).
*   **Swipe-to-Generate (Addiction Engine):** Implemented using Android's `ViewPager2`, enabling users to swipe continuously up and down through their excuses, paired with satisfying haptic feedback vibrations.
*   **Virality Engine (Share as Image):** Share excuses not just as plain text! Our new export engine converts your favorite comebacks into highly polished, beautiful "Quote Cards" (Bitmaps). Share them directly to Instagram Stories or WhatsApp with full app branding.
*   **My Stash (Retention Engine):** Never lose a savage comeback. Save and heart your favorite excuses to a localized Room database and revisit them anytime in your custom "My Stash" dashboard.
*   **Custom Onboarding & Profile Flow:** Start by choosing your 'Vibe' (Student, Professional, Introvert) to let the app know who you are. Edit your profile details, username, and upload an Avatar via the integrated media picker at any time.

## 🛠️ Architecture & Tech Stack

The app was architected to be safe, fast, heavily responsive, and completely crash-free while operating strictly offline:
*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: XML Layouts scaling perfectly to any mobile device natively using `ConstraintLayout` and `ScrollView`.
*   **Architecture**: Modern MVVM (Model-View-ViewModel) paired with the Repository Pattern.
*   **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) mapping entities and AndroidX DataStore for user preferences tracking.
*   **Async Operations**: Database querying and heavy image generation processes are handled completely off the Main Thread via Kotlin Coroutines.
*   **Minimum SDK**: API 24 (Android 7.0)
*   **Target SDK**: API 36

## 📂 Project Structure

*   `MainActivity.kt`: Contains the core logic for the ViewPager2 addiction engine, BottomSheet category filtering, and UI interactions.
*   `OnboardingActivity.kt` / `ProfileActivity.kt`: Drives the user configuration, avatar picking, and Datastore writing.
*   `MyStashActivity.kt`: Handles the RecyclerView and rendering logic for saved database excuses.
*   `ShareUtils.kt`: Background coroutine-powered rendering tool to export XML views natively as shareable PNG Bitmaps.
*   `data/`: Houses the complete Room SQLite Database abstraction, DAOs, Repositories, and the `UserPreferencesRepository`.
*   `generate_excuses.py`: The build script logic utilized to assemble the massive `excuses.json` payload inside the `/assets` directory.

## 📸 How to Play

1.  **Onboard:** Launch the app, enter your name, and choose your persona.
2.  **Filter & Swipe:** Use the filter button at the top to select a category. Swipe up and down endlessly to read new, savage excuses.
3.  **Heart It:** Tap the star to save the best ones to "My Stash".
4.  **Go Viral:** Tap "Share Image" to generate a Quote Card and share it out!
5.  **Edit Identity:** Click the Profile icon top-left to configure your Username, Name, or Profile Display Picture.

## 🤝 Contributing

Got a better excuse? Edit the `generate_excuses.py` script locally, re-run the payload, or fork the repository and open a PR!

---
*Made for those who always have an answer.* 😉
