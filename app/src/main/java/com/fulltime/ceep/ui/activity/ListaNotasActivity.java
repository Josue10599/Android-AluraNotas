package com.fulltime.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fulltime.ceep.R;
import com.fulltime.ceep.dao.NotaDAO;
import com.fulltime.ceep.model.Nota;
import com.fulltime.ceep.ui.helper.callback.itemNotaTouchCallback;
import com.fulltime.ceep.ui.recycler.adapter.ListaNotaAdapter;
import com.fulltime.ceep.ui.recycler.adapter.listener.OnItemClickListener;

import java.util.List;
import java.util.Objects;

import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_POSICAO;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    ListaNotaAdapter listaNotaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        configuraAdapter(pegaTodasNotas());
        configuraRecyclerView();
        configuraInsereNotas();
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO notaDAO = new NotaDAO();
        for (int i = 0; i < 10; i++) {
            notaDAO.insere(new Nota("Título " + (i + 1), "Descrição " + (i + 1)));
        }
        return notaDAO.todos();
    }

    private void configuraInsereNotas() {
        TextView textViewInsereNotas = findViewById(R.id.lista_notas_insere_notas);
        textViewInsereNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNota_Criar();
            }
        });
    }

    private void vaiParaFormularioNota_Criar() {
        Intent telaFormularioNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(telaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (ehResultadoInsereNota(requestCode, data)) {
                if (ehResultadoOk(resultCode)) {
                    salvaNota(data);
                }
            } else if (ehResultadoAlteraNota(requestCode, data)) {
                if (ehResultadoOk(resultCode)) {
                    alteraNota(data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean ehResultadoAlteraNota(int requestCode, @NonNull Intent data) {
        return ehRequisicaoNotaAlterada(requestCode) && ehNota(data) && ehPosicao(data);
    }

    private void alteraNota(@NonNull Intent data) {
        Nota nota = pegaNotaNaIntent(data);
        int posicao = pegaPosicaoNaIntent(data);
        if (posicao > POSICAO_INVALIDA) {
            altera(nota, posicao);
        } else {
            Toast.makeText(this, R.string.erro_altera_nota, Toast.LENGTH_LONG).show();
        }
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        listaNotaAdapter.altera(posicao, nota);
    }

    private Nota pegaNotaNaIntent(@NonNull Intent data) {
        return (Nota) data.getSerializableExtra(CHAVE_NOTA);
    }

    private int pegaPosicaoNaIntent(@NonNull Intent data) {
        return data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
    }

    private boolean ehPosicao(Intent data) {
        return data.hasExtra(CHAVE_POSICAO);
    }

    private boolean ehRequisicaoNotaAlterada(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehRequisicaoInsereNota(requestCode)
                && ehNota(data);
    }

    private boolean ehNota(@Nullable Intent data) {
        return Objects.requireNonNull(data).hasExtra(CHAVE_NOTA);
    }

    private boolean ehResultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void salvaNota(@NonNull Intent data) {
        Nota nota = pegaNotaNaIntent(data);
        new NotaDAO().insere(nota);
        listaNotaAdapter.adicionaNota(nota);
    }

    private void configuraRecyclerView() {
        RecyclerView recyclerViewListaNotas = findViewById(R.id.lista_notas_item);
        recyclerViewListaNotas.setAdapter(listaNotaAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new itemNotaTouchCallback(listaNotaAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerViewListaNotas);
    }

    private void configuraAdapter(List<Nota> notas) {
        listaNotaAdapter = new ListaNotaAdapter(getApplicationContext(), notas);
        listaNotaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNota_Alterar(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNota_Alterar(Nota nota, int posicao) {
        Intent telaFormularioNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        telaFormularioNota.putExtra(CHAVE_NOTA, nota);
        telaFormularioNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(telaFormularioNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
