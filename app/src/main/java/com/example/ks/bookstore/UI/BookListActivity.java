package com.example.ks.bookstore.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ks.bookstore.Account.AppData;
import com.example.ks.bookstore.Book;
import com.example.ks.bookstore.Networking.GetMyBooksTask;
import com.example.ks.bookstore.Networking.GetMyWishList;
import com.example.ks.bookstore.R;
import com.example.ks.bookstore.RecylerAdapters.BookAdapter;
import com.example.ks.bookstore.Utility.Constants;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private RecyclerView bookListRecycler;
    private GridLayoutManager gridLayoutManager;
    private BookAdapter bookAdapter;
    private static ArrayList<Book>myBooks=new ArrayList<>();
    private static ArrayList<Book>myWishList=new ArrayList<>();
    private GetMyBooksTask myBooksTask;
    private GetMyWishList wishListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        bookListRecycler =findViewById(R.id.bookListRecycler);
        final int resolve;
        resolve=getIntent().getIntExtra("resolve",-1);
        myBooks.clear();
        myWishList.clear();

        if (resolve== Constants.MY_BOOKS){
            bookAdapter=new BookAdapter(myBooks,Constants.MY_BOOKS);
            myBooksTask=new GetMyBooksTask(new GetMyBooksTask.GetMyBooksListener() {
                @Override
                public void onMyBooksReceived(ArrayList<Book> result) {
                    myBooks.addAll(result);
                    bookAdapter.notifyDataSetChanged();
                }

                @Override
                public void onMyBooksNotReceived() {

                }
            });
            myBooksTask.execute(AppData.getServerId(this));
        }else if (resolve==Constants.WISH_LIST_BOOKS){
            bookAdapter=new BookAdapter(myWishList,Constants.WISH_LIST_BOOKS);
            wishListTask =new GetMyWishList(new GetMyWishList.GetMyWishListListener() {
                @Override
                public void onMyWishListReceived(ArrayList<Book> result) {
                    myWishList.addAll(result);
                    bookAdapter.notifyDataSetChanged();
                }

                @Override
                public void onMyWishListNotReceived() {

                }
            });
            wishListTask.execute(AppData.getServerId(this));
        }
        gridLayoutManager =new GridLayoutManager(this,1);
        bookListRecycler.setLayoutManager(gridLayoutManager);
        bookListRecycler.setAdapter(bookAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        if (myBooksTask!=null&&myBooksTask.getStatus()!=AsyncTask.Status.FINISHED)
            myBooksTask.cancel(true);

        if (wishListTask !=null&& wishListTask.getStatus()!=AsyncTask.Status.FINISHED)
            wishListTask.cancel(true);

        myBooksTask=null;
        wishListTask =null;
    }

    public static ArrayList<Book> getMyBooks() {
        return myBooks;
    }

    public static ArrayList<Book> getMyWishList() {
        return myWishList;
    }
}
