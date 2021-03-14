package com.techcolon.lookabook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ParentViewHolder> {

    private ArrayList<Book> bookList = new ArrayList<>();


    //
    public BookAdapter() {

    }


    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_singlebooklayout, parent, false);
            return new DataViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_progressbar, parent, false);
            return new ProgressViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {


        if (holder instanceof DataViewHolder) {
            final Book book = bookList.get(position);
            ((DataViewHolder) holder).name.setText(book.getTitleOfBook());
            ((DataViewHolder) holder).price.setText(book.getPrice());
            ((DataViewHolder) holder).semester.setText(book.getSemester());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (bookList.get(position) != null)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    public void addNullData() {
        bookList.add(null);
        notifyItemInserted(bookList.size() - 1);
    }

    public void removeNull() {
        if (bookList.get(bookList.size() - 1) == null) {
            bookList.remove(bookList.size() - 1);
            notifyItemRemoved(bookList.size());
        }
    }

    public void addData(ArrayList<Book> newData) {
        bookList.addAll(newData);
        notifyDataSetChanged();
    }

    public String getLastKey() {
        if (bookList.get(getItemCount() - 1) != null)
            return bookList.get(getItemCount() - 1).getBookID();
        else
            return bookList.get(getItemCount() - 2).getBookID();
    }


    public class ParentViewHolder extends RecyclerView.ViewHolder {


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);


        }

    }

    public class DataViewHolder extends ParentViewHolder {

        TextView name, semester, price;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "You clicked " + bookList.get(getLayoutPosition()).getTitleOfBook(), Toast.LENGTH_SHORT).show();

                }
            });
            name = itemView.findViewById(R.id.nametv);
            semester = itemView.findViewById(R.id.semestertv);
            price = itemView.findViewById(R.id.pricetv);
        }

    }

    public class ProgressViewHolder extends ParentViewHolder {

        ProgressBar progressbar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            progressbar = itemView.findViewById(R.id.progressbar);
        }
    }
}