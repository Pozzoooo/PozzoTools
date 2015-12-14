package pozzo.apps.pozzotools;

import android.content.Context;

import java.util.HashMap;

/**
 * Some configs which should be persisted to better user experience.
 * May be relevant to no particular project.
 *
 * @author pozzo
 * @since 13/08/15.
 */
public class AndroidPrefs {
	//Duplicado no arquivos strings para fins de legibilidade.
	private static final String PREFS_FILE = "android_single";
	//Otimizacao para memoria
	private static HashMap<String, String> prefs;

	private static final String PREF_SEND_MOBILE = "sendOverMobileNetwork";

	static {
		prefs = new HashMap<>();
	}

	/**
	 * Limpa todas as configuracoes armazenadas referente a sessao.
	 */
	public static void clear(Context context) {
		context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
				.edit().clear().commit();
		prefs.clear();
	}

	/**
	 * Busca nas preferences.
	 */
	private static String getStringPref(Context ctx, String name) {
		if(ctx == null || name == null) {
			return null;
		}
		return ctx.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).getString(name, null);
	}

	/**
	 * Busca nas preferences em memoria ou persistidas dado chave passado.
	 */
	private static String getString(Context ctx, String name) {
		String value = prefs.get(name);
		if(value == null)
			value = getStringPref(ctx, name);
		return value;
	}

	/**
	 * Puts a single string to persist on memory xD.
	 */
	private static void putString(Context context, String name, String value) {
		context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
				.edit().putString(name, value).apply();
		prefs.put(name, value);
	}

	/**
	 * Check if user should receive a warn about network.
	 */
	public static boolean isShowMobileNetworkWarn(Context context) {
		return getString(context, PREF_SEND_MOBILE) == null;
	}

	/**
	 * Persist current user intesion about seeing network warn.
	 */
	public static void setShowMobileNetworkWarn(Context context, boolean isShowMobileNetworkWarn) {
		String isSend = isShowMobileNetworkWarn ? "" : null;
		putString(context, PREF_SEND_MOBILE, isSend);
	}
}
