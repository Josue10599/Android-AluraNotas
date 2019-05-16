package com.fulltime.ceep.ui.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fulltime.ceep.R;
import com.fulltime.ceep.model.Nota;
import com.fulltime.ceep.ui.recycler.adapter.listener.OnItemClickListener;

import java.util.Collections;
import java.util.List;

public class ListaNotaAdapter extends RecyclerView.Adapter<ListaNotaAdapter.NotaViewHolder> {
/** Foi atribuito que o RecyclerView.Adapter irá trabalhar com a classe abstrata NotaViewHolder criada
 * nesta mesma classe (por isso a nomenclatura ListaNotaAdapter.NotaViewHolder). Desta forma estamos
 * utilizando o ViewHolder que implementamos e otimizando mais a perfomace de nossa aplicação, pois
 * desta forma podemos realizar boas práticas de implementação não repetindo as mesmas ações.
 * */

    private List<Nota> notas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ListaNotaAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaNotaAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nota, parent,false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotaAdapter.NotaViewHolder holder, int position) {
        Nota nota = notas.get(position);
        holder.bindNota(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void adicionaNota(Nota nota) {
        notas.add(nota);
        notifyDataSetChanged();
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        notas.remove(position);
        notifyDataSetChanged();
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyDataSetChanged();
    }

    // Classe abstrata
    class NotaViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitulo;
        private final TextView textViewDescricao;
        private Nota nota;

        NotaViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.item_nota_titulo);
            textViewDescricao = itemView.findViewById(R.id.item_nota_descricao);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(nota, getAdapterPosition());
                }
            });
        }

        private void bindNota(Nota nota) {
            this.nota = nota;
            textViewTitulo.setText(nota.getTitulo());
            textViewDescricao.setText(nota.getDescricao());
        }
    }
    // Classe abstrata
}
