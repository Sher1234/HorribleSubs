package info.horriblesubs.sher.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import info.horriblesubs.sher.R;

@SuppressWarnings("all")
public class DialogX extends Dialog {

    private Button buttonPositive;
    private Button buttonNegative;
    private Button buttonNeutral;

    private TextView textViewTitle;
    private TextView textViewDescription;

    private final Context context;

    private ImageView imageView;

    private View viewDivider;

    public DialogX(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
        this.context = context;
        setContentView(R.layout.dialog_layout);
        setViewElements();
    }

    private void setViewElements() {
        imageView = findViewById(R.id.imageViewBanner);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);

        buttonPositive = findViewById(R.id.buttonPositive);
        buttonNegative = findViewById(R.id.buttonNegative);
        buttonNeutral = findViewById(R.id.buttonNeutral);

        viewDivider = findViewById(R.id.viewDivider);

        buttonPositive.setVisibility(View.GONE);
        buttonNegative.setVisibility(View.GONE);
        buttonNeutral.setVisibility(View.GONE);

        viewDivider.setVisibility(View.GONE);

        Glide.with(context).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView);
    }

    public DialogX positiveButton(String s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX negativeButton(String s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX neutralButton(String s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX setColouredButtons(boolean b) {
        if (b) {
            buttonPositive.setTextColor(R.color.colorSD);
            buttonNegative.setTextColor(R.color.colorHD);
            buttonNeutral.setTextColor(R.color.colorFHD);
        }
        return this;
    }

    private void showDivider() {
        viewDivider.setVisibility(View.VISIBLE);
    }

    public DialogX setTitle(String s) {
        if (s.length() > 15)
            textViewTitle.setTextSize((float) 17.5);
        else
            textViewTitle.setTextSize(22);
        textViewTitle.setText(s);
        return this;
    }

    public DialogX setDescription(String s) {
        textViewDescription.setText(s);
        this.setDescriptionGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    public DialogX setDescriptionGravity(int i) {
        textViewDescription.setGravity(i);
        return this;
    }
}