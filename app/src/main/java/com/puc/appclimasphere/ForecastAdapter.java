package com.puc.appclimasphere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puc.appclimasphere.model.DailyForecast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Importa as chaves para saber a unidade C ou F
import static com.puc.appclimasphere.ConfiguracaoActivity.UNITS_METRIC;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private final List<DailyForecast> forecastList;
    private final String unitSymbol;
    private final Context context;

    // Construtor
    public ForecastAdapter(Context context, List<DailyForecast> forecastList, String currentUnit) {
        this.context = context;
        this.forecastList = forecastList;
        this.unitSymbol = currentUnit.equals(UNITS_METRIC) ? "°C" : "°F";
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_item_layout, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        DailyForecast dayData = forecastList.get(position);

        // Define a Data (ex: 03/11, Seg)
        holder.tvDate.setText(formatDate(dayData.getTimestamp()));

        // Define a Temperatura Min/Max (ex: 25°C / 18°C)
        String tempFormat = context.getString(R.string.formato_temp_min_max); // "%.0f%s / %.0f%s"
        double minTemp = dayData.getTemp().getMinTemp();
        double maxTemp = dayData.getTemp().getMaxTemp();
        holder.tvTemp.setText(String.format(Locale.getDefault(), tempFormat, minTemp, unitSymbol, maxTemp, unitSymbol));

        // Define a Descrição e o Ícone
        if (dayData.getWeather() != null && !dayData.getWeather().isEmpty()) {
            String description = dayData.getWeather().get(0).getDescription();
            String iconId = dayData.getWeather().get(0).getIcon();

            holder.tvDescription.setText(description);
            holder.ivIcon.setImageResource(WeatherIconMapper.getIconResourceId(iconId));
        }
    }

    @Override
    public int getItemCount() {
        return forecastList != null ? forecastList.size() : 0;
    }

    /**
     * Converte um timestamp Unix (long) em uma data formatada (ex: "03/11, Seg").
     */
    private String formatDate(long timestamp) {
        // O timestamp da API vem em segundos, o Date do Java usa milissegundos
        Date date = new Date(timestamp * 1000L);
        // "dd/MM, EEE" (ex: 03/11, Seg)
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.formato_data_curta), Locale.getDefault());
        return sdf.format(date);
    }

    // Classe ViewHolder interna
    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvDate, tvDescription, tvTemp;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_item_icon);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvTemp = itemView.findViewById(R.id.tv_item_temp);
        }
    }
}
