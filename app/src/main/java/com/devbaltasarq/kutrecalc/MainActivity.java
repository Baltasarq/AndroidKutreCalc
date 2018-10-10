package com.devbaltasarq.kutrecalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public final static String LogTag = MainActivity.class.getSimpleName();

    /** Los operadores disponibles. */
    enum Operacion { Suma, Resta, Multiplica, Divide };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btCalc = this.findViewById( R.id.btCalc );
        final EditText edOp1 = this.findViewById( R.id.edOp1 );
        final EditText edOp2 = this.findViewById( R.id.edOp2 );
        final RadioButton rbSuma = this.findViewById( R.id.rbSuma );
        final RadioButton rbResta = this.findViewById( R.id.rbResta );
        final RadioButton rbMultiplica = this.findViewById( R.id.rbMultiplica );
        final RadioButton rbDivide = this.findViewById( R.id.rbDivide );

        // Establecer los gestores de eventos
        // this::onChangeOpr es equivalente a (v) -> this.onChangeOpr( v )
        rbSuma.setOnClickListener( this::onChangeOpr );
        rbResta.setOnClickListener( this::onChangeOpr );
        rbMultiplica.setOnClickListener( this::onChangeOpr );
        rbDivide.setOnClickListener( this::onChangeOpr );
        btCalc.setOnClickListener( (v) -> calcula() );

        // Establecer el operador por defecto
        this.operacion = Operacion.Suma;
        rbSuma.setChecked( true );

        // Gestor de eventos para los campos de entrada para operandos
        TextWatcher cambiosEnOperandos = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.calcula();
            }
        };

        edOp1.addTextChangedListener( cambiosEnOperandos );
        edOp2.addTextChangedListener( cambiosEnOperandos );
    }

    /** En caso de que se haga click en alguno de los botones de radio.
      * @param v el radio button en el que se hizo click.
      */
    private void onChangeOpr(View v)
    {
        final RadioButton rbSuma = this.findViewById( R.id.rbSuma );
        final RadioButton rbResta = this.findViewById( R.id.rbResta );
        final RadioButton rbMultiplica = this.findViewById( R.id.rbMultiplica );
        final RadioButton rbDivide = this.findViewById( R.id.rbDivide );
        final RadioButton rbSeleccionado = (RadioButton) v;
        final RadioButton[] botones = { rbSuma, rbResta, rbMultiplica, rbDivide };

        rbSuma.setChecked( false );
        rbResta.setChecked( false );
        rbMultiplica.setChecked( false );
        rbDivide.setChecked( false );

        rbSeleccionado.setChecked( true );
        this.operacion = Operacion.values()[
                            Arrays.asList( botones ).indexOf( rbSeleccionado ) ];

        this.calcula();

        Log.i( LogTag, "Opr seleccionada: " + this.operacion );
        return;
    }

    /** Aplica el operador a los operandos. */
    private void calcula()
    {
        final EditText edOp1 = this.findViewById( R.id.edOp1 );
        final EditText edOp2 = this.findViewById( R.id.edOp2 );
        final TextView lblResultado = this.findViewById( R.id.lblResultado );

        String strOp1 = edOp1.getText().toString();
        String strOp2 = edOp2.getText().toString();
        double op1 = 0;
        double op2 = 0;
        double res = 0;

        try {
            op1 = Double.parseDouble( strOp1 );
            op2 = Double.parseDouble( strOp2 );

            switch( this.operacion ) {
                case Suma:
                    res = op1 + op2;
                    break;
                case Resta:
                    res = op1 - op2;
                    break;
                case Multiplica:
                    res = op1 * op2;
                    break;
                case Divide:
                    res = op1 / op2;
                    break;
                default:
                    throw new ArithmeticException( "operador no soportado" );
            }
        } catch(NumberFormatException exc) {
            final String mensaje = "Error al convertir: " + exc.getMessage();

            Log.e( LogTag, mensaje );
            Toast.makeText( this, mensaje, Toast.LENGTH_LONG ).show();

        } catch(ArithmeticException exc) {
            final String mensaje = "Error al calcular: " + exc.getMessage();

            Log.e( LogTag, mensaje );
            Toast.makeText( this, mensaje, Toast.LENGTH_LONG ).show();
        }

        lblResultado.setText( Double.toString( res ) );
    }

    /** El operador seleccionado. */
    private Operacion operacion;
}
