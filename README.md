[![](https://jitpack.io/v/seven-winds-studio/android-common-core.svg)](https://jitpack.io/#seven-winds-studio/android-common-core)
# android-common-core

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
		  repositories {
			  ...
			  maven { url 'https://jitpack.io' }
		  }
	  }  

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.seven-winds-studio:android-common-core:0.2.0'
	}

## Included libraries:

Architecture
* Moxy - MVP
* Cicerone - Navigation

Utility
* Glide - Images
* KotPref - Settings
* RxJava - Rx
* Jackson - JSON
* Inline Activity Result
