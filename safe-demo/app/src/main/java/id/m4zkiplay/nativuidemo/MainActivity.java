package id.m4zkiplay.nativuidemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
    private LinearLayout root;
    private TextView status;
    private TextView practiceBanner;
    private TextView demoMarker;
    private TextView pingValue;
    private Switch statusToggle, markerToggle, practiceToggle, animationToggle, effectsToggle;
    private SharedPreferences prefs;
    private AnimatedHeroBanner heroBanner;
    private int demoPings = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("demo_settings", MODE_PRIVATE);
        demoPings = prefs.getInt("demo_pings", 0);
        getWindow().setStatusBarColor(Color.rgb(5, 12, 28));
        getWindow().setNavigationBarColor(Color.rgb(5, 12, 28));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(18), dp(16), dp(24));
        root.setBackgroundColor(Color.rgb(7, 15, 30));
        scroll.addView(root);
        setContentView(scroll);

        root.addView(text("MAZKIP ARENA", 26, true, Color.WHITE));
        root.addView(text("GAME UI LAB  •  MODE DEMO LOKAL", 11, true, Color.rgb(90, 224, 255)));
        addSpace(12);

        heroBanner = new AnimatedHeroBanner(this);
        root.addView(heroBanner, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(220)));

        addSpace(14);
        practiceBanner = panelText("✦  MODE LATIHAN AKTIF  •  SIMULASI LOKAL", 13, Color.rgb(255, 210, 116), Color.rgb(48, 37, 29));
        root.addView(practiceBanner);

        addSpace(10);
        demoMarker = panelText("◆  OBJEK CONTOH: TARGET A     •     JARAK DEMO: 12 m", 13, Color.rgb(100, 255, 210), Color.rgb(16, 54, 59));
        root.addView(demoMarker);

        addSpace(12);
        status = text("", 14, true, Color.rgb(100, 255, 210));
        status.setPadding(dp(4), dp(6), dp(4), dp(6));
        root.addView(status);

        root.addView(section("KONTROL SIMULASI"));
        statusToggle = addToggle("Panel status", "Tampilkan ringkasan kontrol yang aktif.", true);
        markerToggle = addToggle("Penanda objek demo", "Tampilkan atau sembunyikan penanda target contoh.", true);
        practiceToggle = addToggle("Mode latihan", "Tampilkan atau sembunyikan banner latihan.", false);
        animationToggle = addToggle("Animasi karakter", "Gerakkan aura, partikel, dan karakter banner.", true);
        effectsToggle = addToggle("Efek visual", "Aktifkan atau nonaktifkan partikel dan kilau banner.", true);

        root.addView(section("AKSI CEPAT"));
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(actions, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(48)));
        TextView ping = actionButton("PING DEMO");
        TextView reset = actionButton("RESET");
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(0, dp(44), 1);
        ap.rightMargin = dp(6);
        actions.addView(ping, ap);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, dp(44), 1);
        rp.leftMargin = dp(6);
        actions.addView(reset, rp);

        pingValue = text("Aktivitas demo tersimpan: " + demoPings, 12, false, Color.rgb(190, 205, 222));
        root.addView(pingValue);

        root.addView(section("TENTANG DEMO"));
        root.addView(text("Ini adalah antarmuka game fiksi yang interaktif. Semua kontrol bekerja di aplikasi ini saja; tidak terhubung ke game lain, tidak membaca memori aplikasi lain, dan tidak mengklaim data pertandingan nyata.", 12, false, Color.rgb(160, 177, 200)));

        statusToggle.setOnCheckedChangeListener((button, checked) -> {
            save("status", checked);
            status.setVisibility(checked ? View.VISIBLE : View.GONE);
            refreshStatus();
        });
        markerToggle.setOnCheckedChangeListener((button, checked) -> {
            save("marker", checked);
            demoMarker.setVisibility(checked ? View.VISIBLE : View.GONE);
            refreshStatus();
        });
        practiceToggle.setOnCheckedChangeListener((button, checked) -> {
            save("practice", checked);
            practiceBanner.setVisibility(checked ? View.VISIBLE : View.GONE);
            refreshStatus();
        });
        animationToggle.setOnCheckedChangeListener((button, checked) -> {
            save("animation", checked);
            heroBanner.setAnimationEnabled(checked);
            refreshStatus();
        });
        effectsToggle.setOnCheckedChangeListener((button, checked) -> {
            save("effects", checked);
            heroBanner.setEffectsEnabled(checked);
            refreshStatus();
        });

        statusToggle.setChecked(prefs.getBoolean("status", true));
        markerToggle.setChecked(prefs.getBoolean("marker", true));
        practiceToggle.setChecked(prefs.getBoolean("practice", false));
        animationToggle.setChecked(prefs.getBoolean("animation", true));
        effectsToggle.setChecked(prefs.getBoolean("effects", true));
        heroBanner.setAnimationEnabled(animationToggle.isChecked());
        heroBanner.setEffectsEnabled(effectsToggle.isChecked());
        ping.setOnClickListener(v -> {
            demoPings++;
            prefs.edit().putInt("demo_pings", demoPings).apply();
            pingValue.setText("Aktivitas demo tersimpan: " + demoPings + "  •  aksi lokal berhasil");
            pingValue.setTextColor(Color.rgb(100, 255, 210));
        });
        reset.setOnClickListener(v -> {
            demoPings = 0;
            prefs.edit().putInt("demo_pings", 0).apply();
            pingValue.setText("Aktivitas demo tersimpan: 0  •  sudah direset");
            refreshStatus();
        });
        refreshStatus();
    }

    private Switch addToggle(String label, String description, boolean defaultValue) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(13), dp(10), dp(13), dp(10));
        card.setBackgroundColor(Color.rgb(17, 31, 51));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cp.topMargin = dp(8);
        root.addView(card, cp);
        Switch toggle = new Switch(this);
        toggle.setText(label);
        toggle.setTextColor(Color.WHITE);
        toggle.setTextSize(15);
        card.addView(toggle);
        TextView detail = text(description, 12, false, Color.rgb(166, 184, 207));
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dp.topMargin = dp(3);
        card.addView(detail, dp);
        toggle.setChecked(prefs.getBoolean(keyFor(label), defaultValue));
        return toggle;
    }

    private String keyFor(String label) {
        if (label.equals("Panel status")) return "status";
        if (label.equals("Penanda objek demo")) return "marker";
        if (label.equals("Mode latihan")) return "practice";
        if (label.equals("Animasi karakter")) return "animation";
        return "effects";
    }

    private void save(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    private void refreshStatus() {
        if (status == null || statusToggle == null || markerToggle == null || practiceToggle == null || animationToggle == null || effectsToggle == null) return;
        int enabled = (statusToggle.isChecked() ? 1 : 0) + (markerToggle.isChecked() ? 1 : 0)
            + (practiceToggle.isChecked() ? 1 : 0) + (animationToggle.isChecked() ? 1 : 0)
            + (effectsToggle.isChecked() ? 1 : 0);
        status.setText("● SISTEM DEMO   " + enabled + "/5 KONTROL AKTIF   •   PING: " + demoPings);
        heroBanner.invalidate();
    }

    private TextView section(String value) {
        TextView t = text(value, 12, true, Color.rgb(90, 224, 255));
        t.setPadding(dp(2), dp(18), dp(2), dp(3));
        return t;
    }

    private TextView panelText(String value, int size, int color, int background) {
        TextView t = text(value, size, true, color);
        t.setPadding(dp(12), dp(13), dp(12), dp(13));
        t.setBackgroundColor(background);
        return t;
    }

    private TextView actionButton(String label) {
        TextView t = text(label, 13, true, Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setBackgroundColor(Color.rgb(20, 83, 113));
        t.setClickable(true);
        t.setFocusable(true);
        return t;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (heroBanner != null) heroBanner.resumeAnimation();
    }

    @Override
    protected void onPause() {
        if (heroBanner != null) heroBanner.pauseAnimation();
        super.onPause();
    }

    private static class AnimatedHeroBanner extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final long start = SystemClock.uptimeMillis();
        private boolean animationEnabled = true;
        private boolean effectsEnabled = true;

        AnimatedHeroBanner(Activity activity) {
            super(activity);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        void setAnimationEnabled(boolean enabled) { animationEnabled = enabled; invalidate(); }
        void setEffectsEnabled(boolean enabled) { effectsEnabled = enabled; invalidate(); }
        void resumeAnimation() { invalidate(); }
        void pauseAnimation() { }

        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            float time = (SystemClock.uptimeMillis() - start) / 1000f;
            float wave = animationEnabled ? (float)Math.sin(time * 1.6f) : 0f;
            paint.setShader(new LinearGradient(0, 0, w, h, new int[]{Color.rgb(9, 28, 62), Color.rgb(22, 34, 77), Color.rgb(8, 14, 32)}, null, Shader.TileMode.CLAMP));
            c.drawRoundRect(0, 0, w, h, 18, 18, paint);
            paint.setShader(null);

            // Energy rings and stars are original, locally drawn artwork.
            float cx = w * 0.72f, cy = h * 0.53f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1.5f);
            paint.setColor(Color.argb(90, 73, 220, 255));
            c.drawCircle(cx, cy, h * (0.24f + (animationEnabled ? 0.018f * (float)Math.sin(time * 2f) : 0)), paint);
            c.drawCircle(cx, cy, h * 0.34f, paint);
            paint.setStyle(Paint.Style.FILL);
            if (effectsEnabled) {
                for (int i = 0; i < 18; i++) {
                    float px = (float)((i * 47 + time * (animationEnabled ? 18 : 0)) % Math.max(1, w));
                    float py = (float)((i * 29 + Math.sin(time + i) * 10 + h) % h);
                    paint.setColor(Color.argb(100 + (i % 5) * 25, 73, 220, 255));
                    c.drawCircle(px, py, 1.5f + (i % 3), paint);
                }
            }

            // Stylized original fantasy hero silhouette, not a game character replica.
            float heroX = w * 0.72f + wave * 3f;
            float heroY = h * 0.50f;
            paint.setColor(Color.rgb(16, 24, 47));
            Path cloak = new Path();
            cloak.moveTo(heroX - w * 0.13f, heroY + h * 0.37f);
            cloak.lineTo(heroX - w * 0.09f, heroY - h * 0.02f);
            cloak.lineTo(heroX, heroY - h * 0.15f);
            cloak.lineTo(heroX + w * 0.09f, heroY - h * 0.02f);
            cloak.lineTo(heroX + w * 0.14f, heroY + h * 0.37f);
            cloak.close();
            c.drawPath(cloak, paint);
            paint.setColor(Color.rgb(38, 57, 91));
            c.drawCircle(heroX, heroY - h * 0.18f, h * 0.105f, paint);
            paint.setColor(Color.rgb(90, 224, 255));
            Path visor = new Path();
            visor.moveTo(heroX - h * 0.07f, heroY - h * 0.19f);
            visor.lineTo(heroX + h * 0.07f, heroY - h * 0.19f);
            visor.lineTo(heroX + h * 0.045f, heroY - h * 0.15f);
            visor.lineTo(heroX - h * 0.045f, heroY - h * 0.15f);
            visor.close();
            c.drawPath(visor, paint);
            paint.setColor(Color.rgb(45, 95, 137));
            Path shoulder = new Path();
            shoulder.moveTo(heroX - w * 0.10f, heroY - h * 0.04f);
            shoulder.lineTo(heroX - w * 0.19f, heroY + h * 0.08f);
            shoulder.lineTo(heroX - w * 0.11f, heroY + h * 0.12f);
            shoulder.close();
            c.drawPath(shoulder, paint);
            paint.setColor(Color.rgb(65, 167, 212));
            paint.setStrokeWidth(3f);
            c.drawLine(heroX + w * 0.13f, heroY - h * 0.16f, heroX + w * 0.18f, heroY + h * 0.27f, paint);
            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.WHITE);
            paint.setTextSize(Math.max(16, h * 0.095f));
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            c.drawText("ARENA // HERO LAB", w * 0.055f, h * 0.25f, paint);
            paint.setTextSize(Math.max(10, h * 0.055f));
            paint.setColor(Color.rgb(130, 220, 255));
            c.drawText("ORIGINAL FANTASY CHARACTER", w * 0.06f, h * 0.36f, paint);
            paint.setTextSize(Math.max(10, h * 0.052f));
            paint.setColor(Color.rgb(190, 205, 230));
            c.drawText(animationEnabled ? "ANIMATION: LIVE" : "ANIMATION: PAUSED", w * 0.06f, h * 0.48f, paint);
            if (animationEnabled) postInvalidateDelayed(33);
        }
    }
}
