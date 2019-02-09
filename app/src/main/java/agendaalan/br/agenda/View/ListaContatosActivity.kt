package agendaalan.br.agenda.View

import agendaalan.br.agenda.Model.Contato
import agendaalan.br.agenda.R
import agendaalan.br.agenda.R.id.whatsapp
import agendaalan.br.agenda.Repository.ContatoRepository
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu

import kotlinx.android.synthetic.main.activity_lista_contatos.*
import android.widget.ArrayAdapter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast

class ListaContatosActivity : AppCompatActivity() {

    private var contatoSelecionado:Contato? = null
    private var contatos = ArrayList<Contato>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatos);


        val myToolBar = toolbar
        myToolBar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(myToolBar)


        //val contatos = arrayOf("Maria", "José", "Carlos")

        //Listar
        contatos = ContatoRepository(this).findAll()
        val adapter  = ArrayAdapter(this, android.R.layout.simple_list_item_1,contatos)
        var listaContatos = lista
        listaContatos.setAdapter(adapter);

        lista.setOnItemClickListener { _, _, position, id ->
            val intent = Intent(this@ListaContatosActivity, CadastroActivity::class.java)
            intent.putExtra("contato", contatos?.get(position))
            startActivity(intent)
        }

        lista.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapter, view, posicao, id ->
            contatoSelecionado = contatos?.get(posicao)
            false
        }

    }

    override fun onResume() {
        super.onResume()
        contatos = ContatoRepository(this).findAll()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        carregaLista()
        registerForContextMenu(lista);

        return true
    }

    //onOptionsItemSelected.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.novo -> {
                Toast.makeText(this, "Novo", Toast.LENGTH_LONG).show()

                val intent = Intent(this, CadastroActivity::class.java)
                startActivity(intent)

                return false
            }

            R.id.sincronizar -> {
                Toast.makeText(this, "Enviar", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.receber -> {
                Toast.makeText(this, "Receber", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.mapa -> {
                Toast.makeText(this, "Mapa", Toast.LENGTH_LONG).show()
                return false
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        menuInflater.inflate(R.menu.menu_contato_contexto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.excluir -> {
                AlertDialog.Builder(this@ListaContatosActivity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar ?")
                        .setPositiveButton("Quero",
                                DialogInterface.OnClickListener { dialog, which ->
                                    ContatoRepository(this).delete(this.contatoSelecionado!!.id)
                                    carregaLista()
                                }).setNegativeButton("Nao", null).show()
                return false
            }

            R.id.enviasms -> {
                val intentSms = Intent(Intent.ACTION_VIEW)
                intentSms.data = Uri.parse("sms:" + contatoSelecionado?.telefone)
                intentSms.putExtra("sms_body", "Mensagem")
                item.intent = intentSms
                return false
            }

            R.id.enviaemail -> {
                val intentEmail = Intent(Intent.ACTION_SEND)
                intentEmail.type = "message/rfc822"
                intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(contatoSelecionado?.email!!))
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Teste de email")
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Corpo da mensagem")
                //item.setIntent(intentEmail);
                startActivity(Intent.createChooser(intentEmail, "Selecione a sua aplicação de Email"))
                return false
            }

            R.id.visualizarmapa -> {
                val gmmIntentUri = Uri.parse("geo:0,0?q=" + contatoSelecionado?.endereco)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
                return false
            }

            R.id.whatsapp -> {

                val intentWhatsapp = Intent(Intent.ACTION_SEND)
                intentWhatsapp.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                intentWhatsapp.setType("text/plain")
                intentWhatsapp.setPackage("com.whats app")
                startActivity(intentWhatsapp)

                return false

            }

            R.id.ligar -> {
                val intentLigar = Intent(Intent.ACTION_DIAL)
                intentLigar.data = Uri.parse("tel:" + contatoSelecionado?.telefone)
                item.intent = intentLigar
                return false
            }

             else -> return super.onContextItemSelected(item)
        }



    }

    private fun carregaLista() {

        this.contatos = ContatoRepository(this).findAll()
        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, this.contatos)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()
    }



}
