package br.edu.ifsp.scl.ads.pdm.bookspdm.ui

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.ads.pdm.bookspdm.R
import br.edu.ifsp.scl.ads.pdm.bookspdm.databinding.TileBookBinding
import br.edu.ifsp.scl.ads.pdm.bookspdm.model.Book

class BookAdapter(
    context: Context,
    private val bookList: MutableList<Book>
): ArrayAdapter<Book>(context, R.layout.tile_book, bookList) {

    private data class BookTileHolder(
        val titleTv: TextView,
        val firstAuthorTv: TextView,
        val editionTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var tbb: TileBookBinding

        // Pegar o livro que vai ser usado para preencher a célula
        val book = bookList[position]

        // Verificando se a célula já foi inflada ou não.
        var bookTile = convertView
        if (bookTile == null) {
            // Se não foi, infla uma nova célula
            tbb = TileBookBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            bookTile = tbb.root

            // Criar um holder para a nova célula
            val newBookTileHolder = BookTileHolder(tbb.titleTv, tbb.firstAuthorTv, tbb.editionTv)

            // Associar holder a nova célula
            bookTile.tag = newBookTileHolder
        }

        // Preenche os valores da célula com o novo livro
        val holder = bookTile.tag as BookTileHolder
        holder.let {
            it.titleTv.text = book.title
            it.firstAuthorTv.text = book.firstAuthor
            it.editionTv.text = book.edition.toString()
        }

        // Retorna a célula preenchida
        return bookTile
    }
}