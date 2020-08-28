package mobi.sevenwinds.common.core.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.module.kotlin.KotlinModule

abstract class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        Kotpref.init(this)
    }

    companion object {
        lateinit var instance: BaseApp
            private set

        val json: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(object : PropertyNamingStrategy() { //skip boolean fields renaming
                override fun nameForGetterMethod(
                    config: MapperConfig<*>?,
                    method: AnnotatedMethod?,
                    defaultName: String?
                ): String {
                    if (method?.hasReturnType() == true
                        && method.rawReturnType == Boolean::class.java
                        && method.name?.startsWith("is") == true
                    ) {
                        return method.name
                    }

                    return super.nameForGetterMethod(config, method, defaultName)
                }
            })
    }
}