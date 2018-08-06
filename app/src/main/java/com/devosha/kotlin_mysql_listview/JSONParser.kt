package com.devosha.kotlin_mysql_listview


import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

@Suppress("DEPRECATION")

class JSONParser(private var c: Context, private var jsonData: String, private var myListView: ListView) : AsyncTask<Void, Void, Boolean>() {

    private lateinit var pd: ProgressDialog
    private var quotes = ArrayList<Quote>()


    class Quote(private var quote_text:String, private var quote_author: String, private var quote_category: String,private var author_image_url: String) {

        fun getQuoteText(): String {
            return quote_text
        }

        fun getQuoteAuthor(): String {
            return quote_author
        }

        fun getQuoteCategory(): String {
            return quote_category
        }
        fun getAuthorImageURL(): String {
            return author_image_url
        }
    }
    class MrAdapter(private var c: Context, private var quotes: ArrayList<Quote>) : BaseAdapter() {

        override fun getCount(): Int {
            return quotes.size
        }

        override fun getItem(pos: Int): Any {
            return quotes[pos]
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        /*
        Inflate row_model.xml and return it
         */
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false)
            }

            val authorTxt = convertView!!.findViewById(R.id.authorTxt) as TextView
            val quoteTxt = convertView.findViewById(R.id.quoteTxt) as TextView
            val categoryTxt = convertView.findViewById(R.id.categoryTxt) as TextView
            val authorImageView = convertView.findViewById(R.id.authorImageView) as ImageView

            val quote = this.getItem(i) as Quote

            authorTxt.text = quote.getQuoteAuthor()
            categoryTxt.text = quote.getQuoteCategory()
            quoteTxt.text = quote.getQuoteText()
            if(quote.getAuthorImageURL() != null && quote.getAuthorImageURL().length > 0)
            {
                Picasso.get().load("http://192.168.12.2/php/quotika/"+quote.getAuthorImageURL()).placeholder(R.drawable.placeholder).into(authorImageView);
            }else {
                Toast.makeText(c, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.placeholder).into(authorImageView);
            }


            convertView.setOnClickListener { Toast.makeText(c,quote.getQuoteAuthor(),Toast.LENGTH_SHORT).show() }

            return convertView
        }
    }

    /*
    Parse JSON data
     */
    private fun parse(): Boolean {
        try {
            val ja = JSONArray(jsonData)
            var jo: JSONObject

            quotes.clear()
            var quote: Quote

            for (i in 0 until ja.length()) {
                jo = ja.getJSONObject(i)

                val quote_author = jo.getString("author")
                val quote_text = jo.getString("quote")
                val quote_category = jo.getString("category")
                val author_image_url = jo.getString("author_image_url")

                quote = Quote(quote_text,quote_author,quote_category,author_image_url)
                quotes.add(quote)
            }

            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }
    override fun onPreExecute() {
        super.onPreExecute()

        pd = ProgressDialog(c)
        pd.setTitle("Parse JSON")
        pd.setMessage("Parsing...Please wait")
        pd.show()
    }

    override fun doInBackground(vararg voids: Void): Boolean? {
        return parse()
    }

    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)

        pd.dismiss()
        if (isParsed!!) {
            //BIND
            myListView.adapter = MrAdapter(c, quotes)
        } else {
            Toast.makeText(c, "Unable To Parse that data. ARE YOU SURE IT IS VALID JSON DATA? JsonException was raised. Check Log Output.", Toast.LENGTH_LONG).show()
            Toast.makeText(c, "THIS IS THE DATA WE WERE TRYING TO PARSE :  "+ jsonData, Toast.LENGTH_LONG).show()
        }
    }
    //end

}

