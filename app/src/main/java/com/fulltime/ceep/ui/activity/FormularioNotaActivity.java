package com.fulltime.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fulltime.ceep.R;
import com.fulltime.ceep.model.Nota;

import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formulario_nota_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuConcluir(item)) {
            adicionaNota();
            finish();
        }
        return true;
    }

    private boolean ehMenuConcluir(MenuItem item) {
        return item.getItemId() == R.id.formulario_nota_menu_concluir;
    }

    private void adicionaNota() {
        Nota nota = pegaNota();
        Intent devolveNota = new Intent();
        devolveNota.putExtra(CHAVE_NOTA, nota);
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, devolveNota);
    }

    private Nota pegaNota() {
        Nota nota = new Nota(getTitulo(), getDescricao());
        return nota;
    }

    private String getDescricao() {
        EditText editTextDescricao = findViewById(R.id.formulario_nota_descricao);
        String descricao = editTextDescricao.getText().toString();
        return descricao;
    }

    private String getTitulo() {
        EditText editTextTitulo = findViewById(R.id.formulario_nota_titulo);
        String titulo = editTextTitulo.getText().toString();
        return titulo;
    }
}
