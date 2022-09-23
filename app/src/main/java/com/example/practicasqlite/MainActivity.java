package com.example.practicasqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Vehiculo(View view){
        Intent intVehiculo = new Intent(this,VehiculoActivity.class);
        startActivity(intVehiculo);
    }

    public void Factura(View view){
        Intent intFactura = new Intent(this,FacturaActivity.class);
        startActivity(intFactura);
    }
}