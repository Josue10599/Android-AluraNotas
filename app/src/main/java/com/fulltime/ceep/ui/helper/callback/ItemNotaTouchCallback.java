package com.fulltime.ceep.ui.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fulltime.ceep.dao.NotaDAO;
import com.fulltime.ceep.ui.recycler.adapter.ListaNotaAdapter;

public class ItemNotaTouchCallback extends ItemTouchHelper.Callback {

    private final ListaNotaAdapter adapter;

    public ItemNotaTouchCallback(ListaNotaAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlagEsquerdaDireita = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int drafFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(drafFlags, swipeFlagEsquerdaDireita);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaPosicaoDasNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaPosicaoDasNotas(int posicaoItemSegurado, int posicaoItemSobreposto) {
        new NotaDAO().troca(posicaoItemSegurado, posicaoItemSobreposto);
        adapter.troca(posicaoItemSegurado, posicaoItemSobreposto);
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNota = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNota);
    }

    private void removeNota(int posicao) {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
