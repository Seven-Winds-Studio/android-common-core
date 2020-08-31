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

Step 2. Add the dependencies to module build.gradle

    apply plugin: 'kotlin-kapt'

	dependencies {
	        implementation 'com.github.seven-winds-studio:android-common-core:0.3.0'	        
            kapt 'com.github.bumptech.glide:compiler:4.11.0'            
            kapt 'com.arello-mobile:moxy-compiler:1.5.3'
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
