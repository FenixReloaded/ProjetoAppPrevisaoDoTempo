package com.puc.appclimasphere;

/**
 * Classe utilitária para mapear os códigos de ícone da API OpenWeather
 * para os recursos drawable (ícones) do aplicativo.
 */
public class WeatherIconMapper {

    /**
     * Converte um código de ícone da API (ex: "01d", "10n") para o ID do recurso Drawable.
     * @param iconId O código do ícone fornecido pela API.
     * @return O ID do recurso drawable (ex: R.drawable.ic_01d2x).
     */
    public static int getIconResourceId(String iconId) {
        if (iconId == null) {
            return R.drawable.ic_01d2x; // Padrão (Sol)
        }

        boolean isNight = iconId.endsWith("n");
        int iconResId;

        switch (iconId) {
            case "01d": iconResId = R.drawable.ic_01d2x; break;
            case "01n": iconResId = R.drawable.ic_01n2x; break;
            case "02d": iconResId = R.drawable.ic_02d2x; break;
            case "02n": iconResId = R.drawable.ic_02n2x; break;
            case "03d": iconResId = R.drawable.ic_03d2x; break;
            case "03n": iconResId = R.drawable.ic_03n2x; break;
            case "04d": iconResId = R.drawable.ic_04d2x; break;
            case "04n": iconResId = R.drawable.ic_04n2x; break;
            case "09d": iconResId = R.drawable.ic_09d2x; break;
            case "09n": iconResId = R.drawable.ic_09n2x; break;
            case "10d": iconResId = R.drawable.ic_10d2x; break;
            case "10n": iconResId = R.drawable.ic_10n2x; break;
            case "11d": iconResId = R.drawable.ic_11d2x; break;
            case "11n": iconResId = R.drawable.ic_11n2x; break;
            case "13d": iconResId = R.drawable.ic_13d2x; break;
            case "13n": iconResId = R.drawable.ic_13n2x; break;
            case "50d": iconResId = R.drawable.ic_50d2x; break;
            case "50n": iconResId = R.drawable.ic_50n2x; break;
            default:
                // Fallback
                iconResId = isNight ? R.drawable.ic_01n2x : R.drawable.ic_01d2x;
                break;
        }
        return iconResId;
    }
}
