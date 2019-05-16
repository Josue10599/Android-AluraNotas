package com.fulltime.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fulltime.ceep.R;
import com.fulltime.ceep.model.Nota;

import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_POSICAO;
import static com.fulltime.ceep.ui.activity.Constantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicao = POSICAO_INVALIDA;
    private EditText editTextDescricao;
    private EditText editTextTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle(R.string.titulo_formulario_insere_nota);
        comunicaComponentes();
        recebeDados();
    }

    private void recebeDados() {
        Intent recebeDados = getIntent();
        if (recebeDados.hasExtra(CHAVE_NOTA)) {
            setTitle(R.string.titulo_formulario_altera_nota);
            Nota nota = (Nota) recebeDados.getSerializableExtra(CHAVE_NOTA);
            posicao = recebeDados.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencheCampos(nota);
        }
    }

    private void preencheCampos(Nota nota) {
        editTextTitulo.setText(nota.getTitulo());
        editTextDescricao.setText(nota.getDescricao());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formulario_nota_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuConcluir(item)) {
            devolveDados();
            finish();
        }
        return true;
    }

    private boolean ehMenuConcluir(MenuItem item) {
        return item.getItemId() == R.id.formulario_nota_menu_concluir;
    }

    private void devolveDados() {
        Nota nota = pegaNota();
        Intent mandaDados = new Intent();
        mandaDados.putExtra(CHAVE_NOTA, nota);
        mandaDados.putExtra(CHAVE_POSICAO, posicao);
        setResult(Activity.RESULT_OK, mandaDados);
    }

    private Nota pegaNota() {
        return new Nota(getTitulo(), getDescricao());
    }

    private String getDescricao() {
        return editTextDescricao.getText().toString();
    }

    private void comunicaComponentes() {
        editTextDescricao = findViewById(R.id.formulario_nota_descricao);
        editTextTitulo = findViewById(R.id.formulario_nota_titulo);
    }

    private String getTitulo() {
        return editTextTitulo.getText().toString();
    }
}
