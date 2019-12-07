package com.example.tideprediction;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    // Will hold parsed XML timetables
    ArrayList<TideItem> items;

    // Strings for mapping
    public static final String DATE = "Date";
    public static final String HIGH_OR_LOW = "HighLow";
    public static final String TIME = "Time";

    // Strings for formatting date and time rows
    public static final String DAY_FORMAT = " ";
    public static final String IN_BETWEEN_TIME_AND_HEIGHT = " - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creates activity and sets the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data access layer instantiation
        Dal dal = new Dal(this);

        // Parse florence timetable and store it in items
        items = dal.parseXmlFile("Florence_2019_tide_predictions.xml");

        // Hashmap ~= list of objects?
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        // Iterates through items, formats index and stores it in map
        for(TideItem item: items) {
            HashMap<String, String> map = new HashMap<>();
            map.put(DATE + DAY_FORMAT, (item.getDay() + DAY_FORMAT + item.getDate()));
            map.put(HIGH_OR_LOW + IN_BETWEEN_TIME_AND_HEIGHT + TIME, (item.getTime() + IN_BETWEEN_TIME_AND_HEIGHT + item.getHighlow()));

            // Commit map to data variable
            data.add(map);
        }

        // This is a bit unnecessary, but it makes the simple adapter call "cleaner" looking.
        int resource = R.layout.listview_item;
        String[] from = {DATE + DAY_FORMAT, HIGH_OR_LOW + IN_BETWEEN_TIME_AND_HEIGHT + TIME};
        int[] to = {R.id.item_prediction_date, R.id.item_prediction_time};

        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);

        ListView listView = findViewById(R.id.itemsListView);

        // Apply data hashmap to the listView widget
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setFastScrollEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "Tidal height is " + items.get(i).getPredInFt() + "ft", Toast.LENGTH_LONG).show();
    }
}
