package id.m4zkiplay.nativuidemo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Switch;

public class MainActivity extends Activity {
    private LinearLayout root;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        root.addView(text("Demo lokal untuk menguji kontrol UI Android. Tidak terhubung ke Mobile Legends, tidak membaca memori aplikasi lain, dan tidak memakai layanan eksternal.", 14, false, Color.rgb(190, 205, 222)));
        addSpace(22);
        status = text("Status: semua fitur demo nonaktif", 15, true, Color.rgb(112, 220, 190));
        root.addView(status);

        addToggle("Panel status", "Menampilkan status kontrol lokal.");
        addToggle("Penanda objek demo", "Simulasi penanda visual di layar aplikasi ini.");
        addToggle("Mode latihan", "Mengaktifkan label latihan lokal.");
        addToggle("Animasi antarmuka", "Mengaktifkan animasi sederhana pada tampilan demo.");

        addSpace(18);
        root.addView(text("Catatan: tombol hanya mengubah UI aplikasi demo ini. Aplikasi tidak membuat overlay sistem dan tidak mengubah atau mengendalikan aplikasi lain.", 13, false, Color.rgb(255, 204, 128)));
    }

    private void addToggle(String label, String description) {
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

        toggle.setOnCheckedChangeListener((button, checked) -> {
            refreshStatus();
            if ("Animasi antarmuka".equals(label)) {
                card.setAlpha(checked ? 1.0f : 0.92f);
            }
        });
    }

    private void refreshStatus() {
        int enabled = 0;
        for (int i = 0; i < root.getChildCount(); i++) {
            if (root.getChildAt(i) instanceof LinearLayout) {
                LinearLayout child = (LinearLayout) root.getChildAt(i);
                for (int j = 0; j < child.getChildCount(); j++) {
                    if (child.getChildAt(j) instanceof Switch && ((Switch) child.getChildAt(j)).isChecked()) {
                        enabled++;
                    }
                }
            }
        }
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
