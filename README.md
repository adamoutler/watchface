CASUALWear
==================
synchronize this repository and open in Android Studio.

The Java code in this application is 100% open source and free.  The artwork and layouts are unlicensed.  You should entirely replace the artwork and layouts if you plan to use this source code for your own projects.

####The project consists of three parts.
**/wear/src/main/java/com.casual_dev.CASUALWatch/digital**
The wear/digital portion of this app provides methods and classes pertaining to digital watches with flexible layouts.  Two layouts are used, one for dim and one for awake.  There are three classes, DigitalWatchfaceActivity defines/declares layout/view items and handles time changes. DigitalWatchfaceActions handles transitions and special handlign for view items.  DigitalWatchfaceApp is used to launch the activity while inheriting the properties from the activity and actions.

**/wear/src/main/java/com.casual_dev.CASUALWatch/analog**
The wear/analog portion fo this app provides methods and classes pertaining to analog watches and allows replacement of background and hands using PNG resource changes.  The images are changed smoothly by use of fading betwen awake and dim modes. The layout structure is similar to digital.

**/mobile/src/main/java/com.casual_dev.CASUALWatch**
This is a single view application which provides an easy way to remove the app. Remove the launcher intent from the mobile/src/main/AndroidManifest.xml to disable this single-view app if it's determined to be unnecessary for your project
