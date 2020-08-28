package mobi.sevenwinds.common.core.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mobi.sevenwinds.common.core.ui.BaseActivity
import mobi.sevenwinds.common.core.ui.cicerone.MyRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

abstract class BaseApp : Application() {
    lateinit var cicerone: Cicerone<Router>
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        Kotpref.init(this)

        cicerone = Cicerone.create(MyRouter())
        registerActivityLifecycleCallbacks(ApplicationLifecycleHandler(this))
    }

    fun getNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder

    fun getRouter() = cicerone.router as MyRouter

    companion object {
        lateinit var instance: BaseApp
            private set

        var currentActivity: BaseActivity? = null
            internal set

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