package mobi.sevenwinds.common.core.preferences

import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import mobi.sevenwinds.common.core.app.BaseApp
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObjectPref<T>(
    private val defaultSupplier: () -> T,
    override val key: String?,
    private val commitByDefault: Boolean,
    private val clazz: Class<T>
) : AbstractPref<T>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
        val string = preference.getString(key(property), null)
        return string?.let { json.readValue(it, clazz) } ?: defaultSupplier.invoke()
    }

    override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        editor.putString(key(property), value?.let { json.writeValueAsString(it) })
    }

    override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
        preference.edit().putString(key(property), value?.let { json.writeValueAsString(it) }).execute(commitByDefault)
    }

    private fun key(property: KProperty<*>) = key ?: property.name

    companion object {
        private val json = BaseApp.json

        fun <T> objectPref(
            clazz: Class<T>,
            defaultSupplier: () -> T,
            key: String? = null,
            commitByDefault: Boolean = KotPref.commitAllPropertiesByDefault
        ): ReadWriteProperty<KotprefModel, T> =
            ObjectPref(defaultSupplier, key, commitByDefault, clazz)
    }
}