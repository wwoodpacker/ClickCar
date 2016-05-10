package com.taxi.clickcar.GeoData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.searchviewclickcar.suggestions.model.SearchSuggestion;

/**
 * Created by Назар on 22.04.2016.
 */
public class GeoSuggestion implements SearchSuggestion {
    private String text;

    public GeoSuggestion(Parcel source){
        this.text=source.readString();
    }
    public GeoSuggestion(String _text){text=_text;}
    @Override
    public String getBody() {
        return text;
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }


    public static final Creator<GeoSuggestion> CREATOR= new Creator<GeoSuggestion>() {
        @Override
        public GeoSuggestion createFromParcel(Parcel source) {
            return new GeoSuggestion(source);
        }

        @Override
        public GeoSuggestion[] newArray(int size) {
            return new GeoSuggestion[size];
        }
    };
}
