What is RobotLib?
==
RobotLib is a library for programming FTC robots. Unlike FRC, FTC only contains 
a very minimal SDK. This library aims to address this issue. This library was
inspired by the Java FRC SDK, which can be found [here](https://github.com/wpilibsuite/allwpilib/tree/master/wpilibj). 

Installation
--
1. Clone this repository to some known location.
2. Open Android Studio to your ftc_app.
3. Press File -> New -> Import Module.
4. Select wherever you cloned this repository. (You may get an error; this is okay, ignore it.)
5. Press Finish.
6. Right click the TeamCode module in the Project Browser. Press Open Module Settings.
7. Go to the Dependencies tab.
8. Press the plus in the top right, and select Module Dependency.
9. Then select `:robotlib`, and press OK twice.
10. You should have RobotLib installed! If you have any questions, ask in the programming channel of the [FTC Discord](https://discord.gg/8v3cbkj)!

Documentation
--
The documentation for RobotLib is written in the [KDoc](https://kotlinlang.org/docs/reference/kotlin-doc.html) format and is compiled into a series of webpages with [Dokka](https://github.com/Kotlin/dokka). The documentation can be found compiled [here](https://jdroids.github.io/robotlib/).

Samples
--
The samples for robotlib can be found [here](https://github.com/JDroids/robotlib-examples).
