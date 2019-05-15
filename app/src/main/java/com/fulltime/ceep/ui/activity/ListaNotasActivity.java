package com.fulltime.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fulltime.ceep.R;
import com.fulltime.ceep.dao.NotaDAO;
import com.fulltime.ceep.model.Nota;
import com.fulltime.ceep.ui.recycler.adapter.ListaNotaAdapter;
import com.fulltime.ceep.ui.recycler.adapter.listener.OnItemClickListener;

import java.util.List;
import java.util.Objects;

import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CHAVE_POSICAO;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.fulltime.ceep.ui.activity.Constantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static com.fulltime.ceep.ui.activity.Constantes.VALOR_PADRAO;

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
                vaiParaFormularioNota();
            }
        });
    }

    private void vaiParaFormularioNota() {
        Intent telaFormularioNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(telaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (ehResultadoComNota(requestCode, resultCode, data)) {
                salvaNota(data);
            } else if (ehRequisicaoNotaAlterada(requestCode) && ehResultadoNotaCriada(resultCode) && ehNota(data) && ehPosicao(data)) {
                alteraNota(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alteraNota(@NonNull Intent data) {
        Nota nota = pegaNotaNaIntent(data);
        int posicao = pegaPosicaoNaIntent(data);
        new NotaDAO().altera(posicao, nota);
        listaNotaAdapter.altera(posicao, nota);
    }

    private Nota pegaNotaNaIntent(@NonNull Intent data) {
        return (Nota) data.getSerializableExtra(CHAVE_NOTA);
    }

    private int pegaPosicaoNaIntent(@NonNull Intent data) {
        return data.getIntExtra(CHAVE_POSICAO, VALOR_PADRAO);
    }

    private boolean ehPosicao(Intent data) {
        return data.hasExtra(CHAVE_POSICAO);
    }

    private boolean ehRequisicaoNotaAlterada(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehRequisicaoInsereNota(requestCode)
                && ehResultadoNotaCriada(resultCode)
                && ehNota(data);
    }

    private boolean ehNota(@Nullable Intent data) {
        return Objects.requireNonNull(data).hasExtra(CHAVE_NOTA);
    }

    private boolean ehResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
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
        RecyclerView listView = findViewById(R.id.lista_notas_item);
        listView.setAdapter(listaNotaAdapter);
    }

    private void configuraAdapter(List<Nota> notas) {
        listaNotaAdapter = new ListaNotaAdapter(getApplicationContext(), notas);
        listaNotaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent telaFormularioNota = new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
                telaFormularioNota.putExtra(CHAVE_NOTA, nota);
                telaFormularioNota.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(telaFormularioNota, CODIGO_REQUISICAO_ALTERA_NOTA);
            }
        });
    }
}
