package br.edu.ifsp.scl.ads.pdm.bookspdm.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.ads.pdm.bookspdm.R

class BookSqliteImpl(context: Context) : BookDao {
    companion object {
        private const val BOOK_DATABASE_FILE = "book"
        private const val BOOK_TABLE = "book"
        private const val TITLE_COLUMN = "title"
        private const val ISBN_COLUMN = "isbn"
        private const val FIRST_AUTHOR_COLUMN = "first_author"
        private const val PUBLISHER_COLUMN = "publisher"
        private const val EDITION_COLUMN = "edition"
        private const val PAGES_COLUMN = "pages"

        private const val CREATE_BOOK_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $BOOK_TABLE (" +
                    "$TITLE_COLUMN TEXT NOT NULL, " +
                    "$ISBN_COLUMN TEXT NOT NULL PRIMARY KEY, " +
                    "$FIRST_AUTHOR_COLUMN TEXT NOT NULL, " +
                    "$PUBLISHER_COLUMN TEXT NOT NULL, " +
                    "$EDITION_COLUMN INTEGER NOT NULL, " +
                    "$PAGES_COLUMN INTEGER NOT NULL );"
    }

    private val bookDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        BOOK_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
            bookDatabase.execSQL(CREATE_BOOK_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createBook(book: Book) =
        bookDatabase.insert(BOOK_TABLE, null, bookToContentValues(book))


    override fun retrieveBook(isbn: String): Book {
        /*val cursor = bookDatabase.rawQuery(
            "SELECT * FROM $BOOK_TABLE WHERE $ISBN_COLUMN = ?" ,
            arrayOf(isbn)
        )*/

        val cursor = bookDatabase.query(
            true,
            BOOK_TABLE,
            null,
            "$ISBN_COLUMN = ?",
            arrayOf(isbn),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
           cursorToBook(cursor)
        }
        else {
           Book()
        }
    }

    override fun retrieveBooks(): MutableList<Book> {
        val bookList = mutableListOf<Book>()

        val cursor = bookDatabase.rawQuery("SELECT * FROM $BOOK_TABLE", null)
        while (cursor.moveToNext()) {
            bookList.add(cursorToBook(cursor))
        }

        return bookList
    }

    override fun updateBook(book: Book) = bookDatabase.update(
        BOOK_TABLE,
        bookToContentValues(book),
        "$ISBN_COLUMN = ?",
        arrayOf(book.isbn)
    )

    override fun deleteBook(isbn: String) = bookDatabase.delete(
        BOOK_TABLE,
        "$ISBN_COLUMN = ?",
        arrayOf(isbn)
    )

    private fun bookToContentValues(book: Book) = ContentValues().apply {
        with(book) {
            put(TITLE_COLUMN, title)
            put(ISBN_COLUMN, isbn)
            put(FIRST_AUTHOR_COLUMN, firstAuthor)
            put(PUBLISHER_COLUMN, publisher)
            put(EDITION_COLUMN, edition)
            put(PAGES_COLUMN, pages)
        }
    }

    private fun cursorToBook(cursor: Cursor) = with(cursor) {
        Book(
            getString(getColumnIndexOrThrow(TITLE_COLUMN)),
            getString(getColumnIndexOrThrow(ISBN_COLUMN)),
            getString(getColumnIndexOrThrow(FIRST_AUTHOR_COLUMN)),
            getString(getColumnIndexOrThrow(PUBLISHER_COLUMN)),
            getInt(getColumnIndexOrThrow(EDITION_COLUMN)),
            getInt(getColumnIndexOrThrow(PAGES_COLUMN))
        )
    }
}