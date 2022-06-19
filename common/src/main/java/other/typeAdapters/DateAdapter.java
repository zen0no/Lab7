package other.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAdapter extends TypeAdapter<Date> {

    public SimpleDateFormat formatter;


    public DateAdapter(){
        formatter = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss", Locale.ENGLISH);
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {

        if (value == null) {
            out.nullValue();
            return;
        }
        String time = formatter.format(value);
        out.value(time);
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String time = in.nextString();
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }
}
