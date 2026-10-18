package id.m4zkiplay.nativuidemo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Switch;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
    private LinearLayout root;
    private TextView status;
    private TextView practiceBanner;
    private TextView demoMarker;
    private Switch statusToggle;
    private Switch markerToggle;
    private Switch practiceToggle;
    private Switch animationToggle;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("demo_settings", MODE_PRIVATE);
        getWindow().setStatusBarColor(Color.rgb(8, 24, 44));
        getWindow().setNavigationBarColor(Color.rgb(8, 24, 44));

        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(26), dp(22), dp(26));
        root.setBackgroundColor(Color.rgb(10, 22, 38));
        scroll.addView(root);
        setContentView(scroll);

        root.addView(text("NATIVE UI DEMO", 26, true, Color.WHITE));
        root.addView(text("Demo interaktif lokal untuk menguji kontrol Android. Tidak terhubung ke Mobile Legends, tidak membaca memori aplikasi lain, dan tidak memakai layanan eksternal.", 14, false, Color.rgb(190, 205, 222)));
        addSpace(18);

        practiceBanner = text("MODE LATIHAN AKTIF — DEMO LOKAL", 14, true, Color.rgb(255, 204, 128));
        practiceBanner.setPadding(dp(12), dp(10), dp(12), dp(10));
        practiceBanner.setBackgroundColor(Color.rgb(60, 49, 32));
        root.addView(practiceBanner);

        addSpace(12);
        demoMarker = text("◆  OBJEK CONTOH  •  TARGET A", 16, true, Color.rgb(112, 220, 190));
        demoMarker.setPadding(dp(14), dp(16), dp(14), dp(16));
        demoMarker.setBackgroundColor(Color.rgb(22, 55, 65));
        root.addView(demoMarker);

        addSpace(12);
        status = text("", 15, true, Color.rgb(112, 220, 190));
        root.addView(status);

        statusToggle = addToggle("Panel status", "Tampilkan atau sembunyikan ringkasan status kontrol.");
        markerToggle = addToggle("Penanda objek demo", "Tampilkan atau sembunyikan kartu objek contoh di aplikasi ini.");
        practiceToggle = addToggle("Mode latihan", "Tampilkan atau sembunyikan label mode latihan lokal.");
        animationToggle = addToggle("Animasi antarmuka", "Aktifkan animasi sederhana ketika penanda objek ditampilkan.");

        statusToggle.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("status", checked).apply();
            status.setVisibility(checked ? View.VISIBLE : View.GONE);
            refreshStatus();
        });
        markerToggle.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("marker", checked).apply();
            if (checked && animationToggle.isChecked()) {
                demoMarker.setVisibility(View.VISIBLE);
                Animation fade = new AlphaAnimation(0.15f, 1.0f);
                fade.setDuration(350);
                demoMarker.startAnimation(fade);
            } else {
                demoMarker.clearAnimation();
                demoMarker.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
            refreshStatus();
        });
        practiceToggle.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("practice", checked).apply();
            practiceBanner.setVisibility(checked ? View.VISIBLE : View.GONE);
            refreshStatus();
        });
        animationToggle.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("animation", checked).apply();
            if (!checked) demoMarker.clearAnimation();
            refreshStatus();
        });

        statusToggle.setChecked(prefs.getBoolean("status", true));
        markerToggle.setChecked(prefs.getBoolean("marker", true));
        practiceToggle.setChecked(prefs.getBoolean("practice", false));
        animationToggle.setChecked(prefs.getBoolean("animation", true));

        addSpace(18);
        root.addView(text("Semua kontrol bekerja hanya di layar demo ini. Pengaturan disimpan secara lokal di perangkat.", 13, false, Color.rgb(190, 205, 222)));
        refreshStatus();
    }

    private Switch addToggle(String label, String description) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setBackgroundColor(Color.rgb(22, 40, 60));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cp.topMargin = dp(12);
        root.addView(card, cp);

        Switch toggle = new Switch(this);
        toggle.setText(label);
        toggle.setTextColor(Color.WHITE);
        toggle.setTextSize(16);
        card.addView(toggle);

        TextView detail = text(description, 13, false, Color.rgb(190, 205, 222));
        LinearLayout.LayoutParams detailParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        detailParams.topMargin = dp(5);
        card.addView(detail, detailParams);
        return toggle;
    }

    private void refreshStatus() {
        if (status == null || statusToggle == null || markerToggle == null || practiceToggle == null || animationToggle == null) return;
        int enabled = (statusToggle.isChecked() ? 1 : 0)
            + (markerToggle.isChecked() ? 1 : 0)
            + (practiceToggle.isChecked() ? 1 : 0)
            + (animationToggle.isChecked() ? 1 : 0);
        status.setText("Status: " + enabled + " dari 4 kontrol demo aktif");
    }

    private TextView text(String value, int size, boolean bold, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private void addSpace(int heightDp) {
        TextView space = new TextView(this);
        root.addView(space, new LinearLayout.LayoutParams(1, dp(heightDp)));
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
