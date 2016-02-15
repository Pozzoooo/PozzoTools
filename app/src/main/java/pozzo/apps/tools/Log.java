package pozzo.apps.tools;

/**
 * This class is just an example.
 * As it does not sound useful without Mint or Acra and I dont wanna import them in here.
 * TODO check a classforname and decide when to use Mint or Acra
 *
 * @author Luiz Gustavo Pozzo
 * @since 09/02/16
 */
class Log {
	/**
	 * Just a debug message.
	 */
	public static void d(String msg) {
		if(BuildConfig.DEBUG)
			System.out.println(msg);
	}

	/**
	 * Log an exception to our server.
	 */
	public static void e(Exception e) {
//		Mint.logException(e);
		if(BuildConfig.DEBUG)
			e.printStackTrace();
	}

	/**
	 * Log an exception with extra fields.
	 */
	public static void e(Exception e, String key, String value) {
//		Mint.logExceptionMessage(key, value, e);
		if(BuildConfig.DEBUG) {
			System.out.println(key + ": " + value);
			e.printStackTrace();
		}
	}

	/**
	 * Anything you to log to our server.
	 */
	public static void event(String event) {
//		Mint.logEvent(event);
		if(BuildConfig.DEBUG)
			System.out.println(event);
	}

	/**
	 * So we can follow the track back to the root.
	 */
	public static void breadcrumb(String breadcrumb) {
//		Mint.leaveBreadcrumb(breadcrumb);
		if(BuildConfig.DEBUG)
			System.out.println("breadcrumb: " + breadcrumb);
	}
}
