package com.xteam.sample.utility;

import android.content.Context;

import com.google.gson.Gson;
import com.xteam.sample.dagger.AppConf;
import com.xteam.sample.models.Warehouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Amit on 10/19/15.
 */

/*
Retrofit is not allowing to parse NDJSON response.
CustomConverter class is overriding the basic JSON
Parsing mechanism
 */
public class CustomConverter implements Converter {

    private Context mContext;
    private AppConf mAppConf;

    public CustomConverter(Context context, AppConf appConf){
        this.mContext = context;
        this.mAppConf = appConf;
    }

    @Override
    public List<Warehouse> fromBody(TypedInput body, Type type) throws ConversionException {
        List<Warehouse> warehouses = null;
        try {
            warehouses = fromStream(body.in());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return warehouses;

    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }

    // Custom method to convert stream from request to string
    private  List<Warehouse> fromStream(InputStream in) throws IOException {
        String line;
        List<Warehouse> warehouses = new ArrayList<Warehouse>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        while ((line = reader.readLine()) != null) {
            Gson gson = new Gson();
            if(mAppConf.getGolbalData() && (gson.fromJson(line, Warehouse.class).getStock() > 0)){
                warehouses.add(gson.fromJson(line, Warehouse.class));
            }else if(!mAppConf.getGolbalData()){
                warehouses.add(gson.fromJson(line, Warehouse.class));
            }
        }
        return warehouses;
    }
}
