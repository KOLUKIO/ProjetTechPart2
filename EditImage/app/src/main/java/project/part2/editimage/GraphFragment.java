package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Objects;

import static project.part2.editimage.Contrast.histogramRgb;

public class GraphFragment extends Fragment {

    // empty public constructor
    public GraphFragment() { }

    Bitmap bitmap;
    ImageView i;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_graph, container, false);

        i = Objects.requireNonNull(getActivity()).findViewById(R.id.imageView);
        bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // Allow to edit image
        i.setImageBitmap(bitmap);

        int[][] hist = histogramRgb(bitmap);
        int[] histRed = hist[0];
        int[] histGreen = hist[1];
        int[] histBlue = hist[2];

        ArrayList<BarEntry> barEntriesRed = new ArrayList<>();
        for(int i=0; i<256; i++){
            barEntriesRed.add(new BarEntry(i, histRed[i]));
        }
        ArrayList<BarEntry> barEntriesGreen = new ArrayList<>();
        for(int i=0; i<256; i++){
            barEntriesGreen.add(new BarEntry(i, histGreen[i]));
        }
        ArrayList<BarEntry> barEntriesBlue = new ArrayList<>();
        for(int i=0; i<256; i++){
            barEntriesBlue.add(new BarEntry(i, histBlue[i]));
        }

        BarChart barChart = view.findViewById(R.id.activity_graph_bar_graph);

        BarDataSet barDataSetRed = new BarDataSet(barEntriesRed, "Pixels red");
        barDataSetRed.setColor(Color.argb(87, 255, 0, 0));

        BarDataSet barDataSetGreen = new BarDataSet(barEntriesGreen, "Pixels green");
        barDataSetGreen.setColor(Color.argb(87, 0, 255, 0));

        BarDataSet barDataSetBlue = new BarDataSet(barEntriesBlue, "Pixels blue");
        barDataSetBlue.setColor(Color.argb(87, 0, 0, 255));

        BarData data = new BarData(barDataSetRed, barDataSetGreen, barDataSetBlue);
        barChart.setData(data);

        // vertical axis
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);

        // horizontal axis
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(255);

        barChart.setVisibleXRangeMinimum(6);
        barChart.invalidate();   // refresh

        return view;
    }


}
