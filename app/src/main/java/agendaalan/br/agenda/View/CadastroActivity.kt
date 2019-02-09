package agendaalan.br.agenda.View
import agendaalan.br.agenda.Model.Contato
import agendaalan.br.agenda.R
import agendaalan.br.agenda.Repository.ContatoRepository
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.text.SimpleDateFormat
import java.util.*


class CadastroActivity : AppCompatActivity() {


    var cal = Calendar.getInstance()
    var datanascimento : Button? = null
    var contato : Contato? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val myChildToolbar = toolbar_child
        setSupportActionBar(myChildToolbar)

        // Get a support ActionBar corresponding to this toolbar
        val ab = supportActionBar

        // Enable the Up button
        //para exibir o seta para voltar no actionbar
        ab!!.setDisplayHomeAsUpEnabled(true)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        datanascimento = txtDatanascimento
        datanascimento!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {


                DatePickerDialog(this@CadastroActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

    btnCadastro?.setOnClickListener {
            /*
            var contato = Contato(

                    contato?.id,
                    null,
                    txtNome?.text.toString(),
                    txtEndereco?.text.toString(),
                    txtTelefone?.text.toString().toLong(),
                    cal.timeInMillis,
                    txtEmail?.text.toString(),
                    txtSite?.text.toString())
            */

            contato?.nome = txtNome?.text.toString()
            contato?.endereco = txtEndereco?.text.toString()
            contato?.telefone = txtTelefone?.text.toString().toLong()
            //contato?.dataNascimento = cal.timeInMillis
            contato?.email = txtEmail?.text.toString()
            contato?.site = txtSite?.text.toString()

            if(contato?.id == 0L)
                ContatoRepository(this).create(contato!!)
            else
                ContatoRepository(this).update(contato!!)



            Toast.makeText(this, "Sucesso", Toast.LENGTH_LONG).show()
            this.finish()
        }

    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        if (intent != null) {
            if (intent.getSerializableExtra("contato") != null) {
                contato = intent.getSerializableExtra("contato") as Contato

                txtNome?.setText(contato?.nome)
                txtEndereco?.setText(contato?.endereco)
                txtTelefone.setText(contato?.telefone.toString())

                /*
                if (contato?.dataNascimento != null) {
                    datanascimento?.setText(dateFormatter?.format(Date(contato?.dataNascimento!!)))
                } else {
                    datanascimento?.setText(dateFormatter?.format(Date()))
                }
                */

                txtEmail.setText(contato?.email)
                txtSite?.setText(contato?.site)
            } else {
                contato = Contato()
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        datanascimento!!.text = sdf.format(cal.getTime())
    }

}
