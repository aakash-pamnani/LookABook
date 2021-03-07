package com.techcolon.lookabook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private ArrayList<Book> bookList = new ArrayList<>();


    //
    public BookAdapter() {

    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList.clear();
        this.bookList.addAll(bookList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_singlebooklayout, parent, false);


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Book book = bookList.get(position);
        holder.name.setText(book.getTitleOfBook());
        holder.price.setText(book.getPrice());
        holder.semester.setText(book.getSemester());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,semester,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); // 
            name = itemView.findViewById(R.id.nametv);
            semester = itemView.findViewById(R.id.semestertv);
            price = itemView.findViewById(R.id.pricetv);


        }
        @Override
        public void onClick(View v) {
            Toast.makeText(itemView.getContext(), "You clicked "+ bookList.get(getLayoutPosition()-1).getTitleOfBook() , Toast.LENGTH_SHORT).show();
        }
    }
}