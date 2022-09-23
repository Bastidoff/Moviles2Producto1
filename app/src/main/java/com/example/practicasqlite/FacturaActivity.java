package com.example.practicasqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {
    EditText jetcodigo, jetfecha, jetplaca;
    TextView jtvmarca, jtvmodelo, jtvvalor;
    CheckBox jcactivo;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    long resp;
    String codigo, fecha, placa,marca,modelo,valor;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        getSupportActionBar().hide();
        jetcodigo=findViewById(R.id.etcodigo);
        jetfecha=findViewById(R.id.etfecha);
        jetplaca=findViewById(R.id.etplaca);
        jtvmarca=findViewById(R.id.tvmarca);
        jtvmodelo=findViewById(R.id.tvmodelo);
        jtvvalor=findViewById(R.id.tvvalor);
        jcactivo=findViewById(R.id.cbactivo);
        sw=0;
    }

    public void Buscar(View view){
        placa=jetplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblVehiculo where placa='" + placa + "'",null);
            if(fila.moveToNext()){
                if(fila.getString(4).equals("si")) {
                    sw = 1;
                    jtvmarca.setText(fila.getString(1));
                    jtvmodelo.setText(fila.getString(2));
                    jtvvalor.setText(fila.getString(3));
                }
                else{
                    Toast.makeText(this, "El vehículo no está disponible", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "El vehículo no existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Guardar(View view){
        codigo=jetcodigo.getText().toString();
        fecha=jetfecha.getText().toString();
        placa=jetplaca.getText().toString();
        if(sw==0){
            Toast.makeText(this, "Primero debe buscar el vehículo", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            if(codigo.isEmpty() || fecha.isEmpty()){
                Toast.makeText(this, "El código de la factura y la fecha son obligatorios", Toast.LENGTH_SHORT).show();
            }
            else{
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("cod_factura",codigo);
                registro.put("fecha",fecha);
                registro.put("placa",placa);
                resp = db.insert("TblFactura",null,registro);
                if(resp > 0){
                    Toast.makeText(this, "¡¡Factura guardada con éxito!!", Toast.LENGTH_SHORT).show();
                    ContentValues registroVehiculo = new ContentValues();
                    registroVehiculo.put("activo","no");
                    db.update("TblVehiculo", registroVehiculo, "placa='" + placa + "'", null);
                    Limpiar_campos();
                }
                else{
                    Toast.makeText(this, "Error al guardar la factura", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        }

    }

    public void Consultar(View view){
        codigo=jetcodigo.getText().toString();
        if(codigo.isEmpty()){
            Toast.makeText(this, "El código de la factura es requerida", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblFactura where cod_factura='" + codigo + "'",null);
            if(fila.moveToNext()){
                sw=1;
                jetfecha.setText(fila.getString(1));
                jetplaca.setText(fila.getString(2));
                if (fila.getString(3).equals("si")){
                    jcactivo.setChecked(true);
                }
                else{
                    jcactivo.setChecked(false);
                }
                //consulta a la tabla vehículo para poder mostrar la información del vehículo
                placa = jetplaca.getText().toString();
               Cursor filaVehiculo = db.rawQuery("select * from TblVehiculo where placa='" + placa + "'",null);
               if(filaVehiculo.moveToNext()){
                   jtvmarca.setText(filaVehiculo.getString(1));
                   jtvmodelo.setText(filaVehiculo.getString(2));
                   jtvvalor.setText(filaVehiculo.getString(3));
               }

            }
            else{
                Toast.makeText(this, "Factura no existe", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void AnularF(View view){
        codigo=jetcodigo.getText().toString();
        if (sw==0){
            Toast.makeText(this, "Primero debe consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblFactura",registro,"cod_factura='" + codigo + "'",null);
            if (resp > 0){
                Toast.makeText(this, "Factura anulada", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error anulando la factura", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void CancelarF(View view){
        Limpiar_campos();
    }

    public void RegresarF(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    public void Limpiar_campos(){
        jetcodigo.setText("");
        jetfecha.setText("");
        jetplaca.setText("");
        jtvmarca.setText("");
        jtvmodelo.setText("");
        jtvvalor.setText("");
    }

}