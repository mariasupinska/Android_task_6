package pl.edu.pb.libraryapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract BookDao bookDao();

    static BookDatabase getDatabase(final Context context) {
        if ( databaseInstance == null ) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), BookDatabase.class, "book_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }

        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                BookDao dao = databaseInstance.bookDao();
                Book book1 = new Book("Hunger Games", "Suzanne Collins");
                Book book2 = new Book("Six of Crows", "Leigh Bardugo");
                Book book3 = new Book("Crooked Kingdom", "Leigh Bardugo");
                Book book4 = new Book("Harry Potter", "J.K. Rowling");
                Book book5 = new Book("Divergent", "Veronica Roth");

                dao.insert(book1);
                dao.insert(book2);
                dao.insert(book3);
                dao.insert(book4);
                dao.insert(book5);
            });
        }
    };
}
