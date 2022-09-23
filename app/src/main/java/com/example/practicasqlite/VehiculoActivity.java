package com.example.practicasqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VehiculoActivity extends AppCompatActivity {
    EditText jetplaca,jetmarca,jetmodelo,jetprecio;
    CheckBox jcbactivo;
    ClsOpenHelper admin=new    ClsOpenHelper (this,"Concesionario.db", null,1);
    String placa,marca,modelo,precio;
    long resp;
    int sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        getSupportActionBar().hide();

        jetmarca=findViewById(R.id.etmarca);
        jetmodelo=findViewById(R.id.etmodelo);
        jetplaca=findViewById(R.id.etplaca);
        jetprecio=findViewById(R.id.etprecio);
        jcbactivo=findViewById(R.id.cbactivo);
        sw=0;
    }


    public void Guardar(View view){
        placa=jetplaca.getText().toString();
        marca=jetmarca.getText().toString();
        modelo=jetmodelo.getText().toString();
        precio=jetprecio.getText().toString();

        if(placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || precio.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa",placa);
            registro.put("modelo",modelo);
            registro.put("marca",marca);
            registro.put("precio",Integer.parseInt(precio));
            if(sw==0){
                resp=db.insert("TblVehiculo", null, registro);
            }else{
                resp=db.update("TblVehiculo", registro, "placa" + placa + "", null);
                sw=0;
            }
            if(resp>0){
                Toast.makeText(this, "¡Registro Guardado con Éxito!", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "¡Error guardando el registro!", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Consultar(View view){
        placa=jetplaca.getText().toString();
        if(placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblVehiculo where placa='"+placa+ "'", null);
            if(fila.moveToNext()){
                sw=1;
                jetmarca.setText(fila.getString(1));
                jetmodelo.setText(fila.getString(2));
                jetprecio.setText(fila.getString(3));
                if(fila.getString(4).equals("si")){
                    jcbactivo.setChecked(true);
                }else{
                    jcbactivo.setChecked(false);
                }
            }else{
                Toast.makeText(this, "Vehículo no registrado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }

}

    public void Anular(View view){
        placa=jetplaca.getText().toString();
        if (sw==0){
            Toast.makeText(this, "Primero debe consultar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblVehiculo",registro,"placa='" + placa + "'",null);
            if (resp > 0){
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }


    private void Limpiar_campos(){
        jetplaca.setText("");
        jetprecio.setText("");
        jetmodelo.setText("");
        jetmarca.setText("");
        jcbactivo.setChecked(false);
        jetplaca.requestFocus();
        sw=0;
    }

}