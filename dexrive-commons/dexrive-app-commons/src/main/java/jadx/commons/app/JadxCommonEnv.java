package jadx.commons.app;

import org.jetbrains.annotations.Nullable;

public class JadxCommonEnv {

	public static @Nullable String get(String varName, @Nullable String defValue) {
		String strValue = resolveBrandedEnv(varName);
		return isNullOrEmpty(strValue) ? defValue : strValue;
	}

	public static boolean getBool(String varName, boolean defValue) {
		String strValue = resolveBrandedEnv(varName);
		if (isNullOrEmpty(strValue)) {
			return defValue;
		}
		return strValue.equalsIgnoreCase("true");
	}

	public static int getInt(String varName, int defValue) {
		String strValue = resolveBrandedEnv(varName);
		if (isNullOrEmpty(strValue)) {
			return defValue;
		}
		return Integer.parseInt(strValue);
	}

	/**
	 * Prefer DEXRIVE_* names for the rebranded product, while continuing to
	 * accept inherited environment variables for existing scripts and plugins.
	 */
	private static @Nullable String resolveBrandedEnv(String varName) {
		if (varName.startsWith("JADX_")) {
			String brandedName = "DEXRIVE_" + varName.substring("JADX_".length());
			String value = System.getenv(brandedName);
			if (!isNullOrEmpty(value)) {
				return value;
			}
		}
		return System.getenv(varName);
	}

	private static boolean isNullOrEmpty(@Nullable String value) {
		return value == null || value.isEmpty();
	}
}
