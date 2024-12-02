package br.edu.ifsp.scl.ads.pdm.bookspdm.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.pdm.bookspdm.R
import br.edu.ifsp.scl.ads.pdm.bookspdm.databinding.ActivityBookBinding
import br.edu.ifsp.scl.ads.pdm.bookspdm.model.Book
import br.edu.ifsp.scl.ads.pdm.bookspdm.model.Constant
import br.edu.ifsp.scl.ads.pdm.bookspdm.model.Constant.BOOK

class BookActivity : AppCompatActivity() {
    private val abb: ActivityBookBinding by lazy {
        ActivityBookBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(abb.root)

        val receivedBook = intent.getParcelableExtra<Book>(BOOK)
        receivedBook?.let { book ->
            with(abb) {
                with(book) {
                    titleEt.setText(title)
                    isbnEt.setText(isbn)
                    isbnEt.isEnabled = false
                    firstAuthorEt.setText(firstAuthor)
                    publisherEt.setText(publisher)
                    editionEt.setText(edition.toString())
                    pagesEt.setText(pages.toString())
                }

            }
        }

        abb.toolbarIn.toolbar.let {
            it.subtitle = if (receivedBook == null ) "New book" else "Edit book"
            setSupportActionBar(it)
        }

        abb.run {
            saveBt.setOnClickListener {
                Book(
                    titleEt.text.toString(),
                    isbnEt.text.toString(),
                    firstAuthorEt.text.toString(),
                    publisherEt.text.toString(),
                    editionEt.text.toString().toInt(),
                    pagesEt.text.toString().toInt()
                ).let { book ->
                    Intent().apply {
                        putExtra(Constant.BOOK, book)
                        setResult(RESULT_OK, this)
                        finish()
                    }
                }
            }
        }
    }
}